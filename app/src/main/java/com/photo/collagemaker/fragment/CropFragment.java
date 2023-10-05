package com.photo.collagemaker.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.adapters.AspectAdapter;
import com.isseiaoki.simplecropview.CropImageView;
import com.photo.collagemaker.databinding.FragmentCropBinding;
import com.steelkiwi.cropiwa.AspectRatio;

public class CropFragment extends DialogFragment implements AspectAdapter.OnNewSelectedListener {
    private static final String TAG = "CropFragment";
    private Bitmap bitmap;
    public OnCropPhoto onCropPhoto;
    private FragmentCropBinding binding;

    @SuppressLint("WrongConstant")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        binding = FragmentCropBinding.inflate(layoutInflater, viewGroup, false);

        AspectAdapter aspectRatioPreviewAdapter = new AspectAdapter(getActivity());
        aspectRatioPreviewAdapter.setListener(this);

        binding.recyclerViewRatio.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        binding.recyclerViewRatio.setAdapter(aspectRatioPreviewAdapter);
        binding.cropImageView.setCropMode(CropImageView.CropMode.FREE);

        binding.imageViewSaveCrop.setOnClickListener(view -> new OnSaveCrop().execute());
        binding.relativeLayoutLoading.setVisibility(View.GONE);
        binding.imageViewCloseCrop.setOnClickListener(view -> dismiss());

        return binding.getRoot();
    }


    public interface OnCropPhoto {
        void finishCrop(Bitmap bitmap);
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static CropFragment show(@NonNull AppCompatActivity appCompatActivity, OnCropPhoto onCropPhoto2, Bitmap bitmap2) {
        CropFragment cropDialogFragment = new CropFragment();
        cropDialogFragment.setBitmap(bitmap2);
        cropDialogFragment.setOnCropPhoto(onCropPhoto2);
        cropDialogFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return cropDialogFragment;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void setOnCropPhoto(OnCropPhoto onCropPhoto2) {
        this.onCropPhoto = onCropPhoto2;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }


    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        binding.cropImageView.setImageBitmap(bitmap);
    }

    public void onNewAspectRatioSelected(AspectRatio aspectRatio) {
        if (aspectRatio.getWidth() == 10 && aspectRatio.getHeight() == 10) {
            binding.cropImageView.setCropMode(CropImageView.CropMode.FREE);
        } else {
            binding.cropImageView.setCustomRatio(aspectRatio.getWidth(), aspectRatio.getHeight());
        }
    }

    class OnSaveCrop extends AsyncTask<Void, Bitmap, Bitmap> {
        OnSaveCrop() {
        }

        public void onPreExecute() {
            mLoading(true);
        }

        public Bitmap doInBackground(Void... voidArr) {
            return binding.cropImageView.getCroppedBitmap();
        }

        public void onPostExecute(Bitmap bitmap) {
            mLoading(false);
            onCropPhoto.finishCrop(bitmap);
            dismiss();
        }
    }

    public void mLoading(boolean z) {
        if (z) {
            getActivity().getWindow().setFlags(16, 16);
            binding.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        getActivity().getWindow().clearFlags(16);
        binding.relativeLayoutLoading.setVisibility(View.GONE);
    }
}
