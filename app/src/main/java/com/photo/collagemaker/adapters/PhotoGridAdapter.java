package com.photo.collagemaker.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.photo.collagemaker.R;
import com.photo.collagemaker.entity.Photo;
import com.photo.collagemaker.entity.PhotoDirectory;
import com.photo.collagemaker.event.OnItemCheckListener;
import com.photo.collagemaker.picker.AndroidLifecycleUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoGridAdapter extends SelectableAdapter<PhotoGridAdapter.PhotoViewHolder> {
    private int columnNumber;
    private RequestManager glide;
    private boolean hasCamera;
    private int imageSize;

    public View.OnClickListener onClickListener;

    public OnItemCheckListener onItemCheckListener;
    private boolean previewEnable;

    public PhotoGridAdapter(Context context, RequestManager requestManager, List<PhotoDirectory> list) {
        this.onItemCheckListener = null;
        this.onClickListener = null;
        this.hasCamera = true;
        this.previewEnable = true;
        this.columnNumber = 3;
        this.photoDirectories = list;
        this.glide = requestManager;
        setColumnNumber(context, columnNumber);
    }

    public PhotoGridAdapter(Context context, RequestManager requestManager, List<PhotoDirectory> list, ArrayList<String> arrayList, int i) {
        this(context, requestManager, list);
        setColumnNumber(context, i);
        selectedPhotos = new ArrayList();
        if (arrayList != null) {
            selectedPhotos.addAll(arrayList);
        }
    }

    private void setColumnNumber(Context context, int i) {
        columnNumber = i;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        imageSize = displayMetrics.widthPixels / i;
    }

    public int getItemViewType(int i) {
        return (!showCamera() || i != 0) ? 101 : 100;
    }

    public PhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        PhotoViewHolder photoViewHolder = new PhotoViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_photo, viewGroup, false));
        if (i == 100) {
            photoViewHolder.image_view_selected.setVisibility(View.GONE);
            photoViewHolder.image_view_picker.setScaleType(ImageView.ScaleType.CENTER);
            photoViewHolder.image_view_picker.setOnClickListener(view -> {
                if (onClickListener != null) {
                    onClickListener.onClick(view);
                }
            });
        }
        return photoViewHolder;
    }

    public void onBindViewHolder(final PhotoViewHolder photoViewHolder, int i) {
        final Photo photo;
        if (getItemViewType(i) == 101) {
            List<Photo> currentPhotos = getCurrentPhotos();
            if (showCamera()) {
                photo = currentPhotos.get(i - 1);
            } else {
                photo = currentPhotos.get(i);
            }
            if (AndroidLifecycleUtils.canLoadImage(photoViewHolder.image_view_picker.getContext())) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.centerCrop().dontAnimate().override(imageSize, imageSize).placeholder(R.drawable.background_gray);
                glide.setDefaultRequestOptions(requestOptions).load(new File(photo.getPath())).thumbnail(0.5f).into(photoViewHolder.image_view_picker);
            }
            boolean isSelected = isSelected(photo);
            photoViewHolder.image_view_selected.setSelected(isSelected);
            photoViewHolder.image_view_picker.setSelected(isSelected);
            photoViewHolder.image_view_picker.setOnClickListener(view -> onItemCheckListener.onItemCheck(photoViewHolder.getAdapterPosition(), photo, getSelectedPhotos().size() + (isSelected(photo) ? -1 : 1)));
            return;
        }
        photoViewHolder.image_view_picker.setImageResource(R.drawable.black_border);
    }

    public int getItemCount() {
        int size = photoDirectories.size() == 0 ? 0 : getCurrentPhotos().size();
        return showCamera() ? size + 1 : size;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_view_picker;

        public View image_view_selected;

        public PhotoViewHolder(View view) {
            super(view);
            image_view_picker = view.findViewById(R.id.image_view_picker);
            image_view_selected = view.findViewById(R.id.image_view_selected);
            image_view_selected.setVisibility(View.GONE);
        }
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener2) {
        onItemCheckListener = onItemCheckListener2;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ArrayList<String> getSelectedPhotoPaths() {
        ArrayList<String> arrayList = new ArrayList<>(getSelectedItemCount());
        for (String add : selectedPhotos) {
            arrayList.add(add);
        }
        return arrayList;
    }

    public void setShowCamera(boolean z) {
        hasCamera = z;
    }

    public void setPreviewEnable(boolean z) {
        previewEnable = z;
    }

    public boolean showCamera() {
        return hasCamera && currentDirectoryIndex == 0;
    }

    public void onViewRecycled(PhotoViewHolder photoViewHolder) {
        glide.clear(photoViewHolder.image_view_picker);
        super.onViewRecycled(photoViewHolder);
    }
}
