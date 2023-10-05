package com.photo.collagemaker.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.photo.collagemaker.R;
import com.photo.collagemaker.adapters.PhotoGridAdapter;
import com.photo.collagemaker.adapters.DirectoryListAdapter;
import com.photo.collagemaker.databinding.FragmentPhotoPickerBinding;
import com.photo.collagemaker.entity.Photo;
import com.photo.collagemaker.entity.PhotoDirectory;
import com.photo.collagemaker.picker.AndroidLifecycleUtils;
import com.photo.collagemaker.picker.ImageCaptureManager;
import com.photo.collagemaker.picker.MediaStoreHelper;
import com.photo.collagemaker.picker.PermissionsUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoPickerFragment extends Fragment {
    public static int COUNT_MAX = 4;
    private static final String EXTRA_CAMERA = "camera";
    private static final String EXTRA_COLUMN = "column";
    private static final String EXTRA_COUNT = "count";
    private static final String EXTRA_GIF = "gif";
    private static final String EXTRA_ORIGIN = "origin";

    public int SCROLL_THRESHOLD = 30;
    private ImageCaptureManager captureManager;
    int column;

    public List<PhotoDirectory> directories;
    private DirectoryListAdapter listAdapter;

    public ListPopupWindow listPopupWindow;

    public RequestManager mGlideRequestManager;
    private ArrayList<String> originalPhotos;

    public PhotoGridAdapter photoGridAdapter;

    public static PhotoPickerFragment newInstance(boolean z, boolean z2, boolean z3, int i, int i2, ArrayList<String> arrayList) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_CAMERA, z);
        bundle.putBoolean(EXTRA_GIF, z2);
        bundle.putBoolean("PREVIEW_ENABLED", z3);
        bundle.putInt("column", i);
        bundle.putInt(EXTRA_COUNT, i2);
        bundle.putStringArrayList("origin", arrayList);
        PhotoPickerFragment photoPickerFragment = new PhotoPickerFragment();
        photoPickerFragment.setArguments(bundle);
        return photoPickerFragment;
    }

    FragmentPhotoPickerBinding binding;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentPhotoPickerBinding.inflate(layoutInflater, viewGroup, false);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(column, 1);
        staggeredGridLayoutManager.setGapStrategy(2);
        binding.recyclerViewPhotos.setLayoutManager(staggeredGridLayoutManager);
        binding.recyclerViewPhotos.setAdapter(photoGridAdapter);
        binding.recyclerViewPhotos.setItemAnimator(new DefaultItemAnimator());

        listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setOnDismissListener(() -> binding.imageViewIcon.setImageResource(R.drawable.ic_arrow_up));
        listPopupWindow.setWidth(-1);
        listPopupWindow.setAnchorView(binding.linearLayoutWrapperFolder);
        listPopupWindow.setAdapter(listAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setDropDownGravity(80);
        listPopupWindow.setOnItemClickListener((adapterView, view, i, j) -> {
            listPopupWindow.dismiss();
            binding.textViewFolder.setText((directories.get(i)).getName());
            photoGridAdapter.setCurrentDirectoryIndex(i);
            photoGridAdapter.notifyDataSetChanged();
        });
        photoGridAdapter.setOnClickListener(view -> {
            if (PermissionsUtils.checkCameraPermission(PhotoPickerFragment.this) && PermissionsUtils.checkWriteStoragePermission((Fragment) PhotoPickerFragment.this)) {
                openCamera();
            }
        });
        binding.linearLayoutWrapperFolder.setOnClickListener(view -> {
            if (listPopupWindow.isShowing()) {
                listPopupWindow.dismiss();
                binding.imageViewIcon.setImageResource(R.drawable.ic_arrow_up);
            } else if (!getActivity().isFinishing()) {
                adjustHeight();
                binding.imageViewIcon.setImageResource(R.drawable.ic_arrow_up);
                listPopupWindow.show();
            }
        });
        binding.recyclerViewPhotos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                if (Math.abs(i2) > SCROLL_THRESHOLD) {
                    mGlideRequestManager.pauseRequests();
                } else {
                    resumeRequestsIfNotDestroyed();
                }
            }

            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                if (i == 0) {
                    resumeRequestsIfNotDestroyed();
                }
            }
        });
        return binding.getRoot();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        mGlideRequestManager = Glide.with(this);
        directories = new ArrayList();
        originalPhotos = getArguments().getStringArrayList("origin");
        column = getArguments().getInt("column", 3);
        boolean z = getArguments().getBoolean(EXTRA_CAMERA, true);
        boolean z2 = getArguments().getBoolean("PREVIEW_ENABLED", true);
        photoGridAdapter = new PhotoGridAdapter(getActivity(), mGlideRequestManager, directories, originalPhotos, column);
        photoGridAdapter.setShowCamera(z);
        photoGridAdapter.setPreviewEnable(z2);
        listAdapter = new DirectoryListAdapter(mGlideRequestManager, directories);
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean("SHOW_GIF", getArguments().getBoolean(EXTRA_GIF));
        MediaStoreHelper.getPhotoDirs(getActivity(), bundle2, list -> {
            directories.clear();
            directories.addAll(list);
            photoGridAdapter.notifyDataSetChanged();
            listAdapter.notifyDataSetChanged();
            adjustHeight();
        });
        captureManager = new ImageCaptureManager(getActivity());
    }

    public void onResume() {
        super.onResume();
    }




    public void openCamera() {
        try {
            startActivityForResult(captureManager.dispatchTakePictureIntent(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException e2) {
            Log.e("PhotoPickerFragment", "No Activity Found to handle Intent", e2);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1 && i2 == -1) {
            if (captureManager == null) {
                captureManager = new ImageCaptureManager(getActivity());
            }
            captureManager.galleryAddPic();
            if (directories.size() > 0) {
                String currentPhotoPath = captureManager.getCurrentPhotoPath();
                PhotoDirectory photoDirectory = directories.get(0);
                photoDirectory.getPhotos().add(0, new Photo(currentPhotoPath.hashCode(), currentPhotoPath));
                photoDirectory.setCoverPath(currentPhotoPath);
                photoGridAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (iArr.length > 0 && iArr[0] == 0) {
            if ((i == 1 || i == 3) && PermissionsUtils.checkWriteStoragePermission((Fragment) this) && PermissionsUtils.checkCameraPermission((Fragment) this)) {
                openCamera();
            }
        }
    }

    public PhotoGridAdapter getPhotoGridAdapter() {
        return photoGridAdapter;
    }

    public void onSaveInstanceState(Bundle bundle) {
        captureManager.onSaveInstanceState(bundle);
        super.onSaveInstanceState(bundle);
    }

    public void onViewStateRestored(Bundle bundle) {
        captureManager.onRestoreInstanceState(bundle);
        super.onViewStateRestored(bundle);
    }

    public void adjustHeight() {
        if (listAdapter != null) {
            int count = listAdapter.getCount();
            if (count >= COUNT_MAX) {
                count = COUNT_MAX;
            }
            if (listPopupWindow != null) {
                listPopupWindow.setHeight(count * getResources().getDimensionPixelOffset(R.dimen.picker_item_directory_height));
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (directories != null) {
            for (PhotoDirectory next : directories) {
                next.getPhotoPaths().clear();
                next.getPhotos().clear();
                next.setPhotos(null);
            }
            directories.clear();
            directories = null;
        }
    }


    public void resumeRequestsIfNotDestroyed() {
        if (AndroidLifecycleUtils.canLoadImage(this)) {
            mGlideRequestManager.resumeRequests();
        }
    }
}
