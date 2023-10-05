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

import com.github.flipzeus.FlipDirection;
import com.github.flipzeus.ImageFlipper;
import com.isseiaoki.simplecropview.CropImageView;
import com.photo.collagemaker.R;
import com.photo.collagemaker.databinding.FragmentRotateBinding;

public class RotateFragment extends DialogFragment  {
    private static final String TAG = "CropFragments";
    private Bitmap bitmap;
    public OnCropPhoto onCropPhoto;

    FragmentRotateBinding binding;

    public interface OnCropPhoto {
        void finishCrop(Bitmap bitmap);
    }
    @SuppressLint("WrongConstant")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        binding = FragmentRotateBinding.inflate(layoutInflater, viewGroup, false);

        binding.cropImageView.setCropMode(CropImageView.CropMode.FREE);
        binding.relativeLayoutLoading.setVisibility(View.GONE);


        binding.relativeLayoutRotate.setOnClickListener(view -> binding.cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D));
        binding.relativeLayoutVFlip.setOnClickListener(view -> ImageFlipper.flip(binding.cropImageView, FlipDirection.VERTICAL));
        binding.relativeLayoutHFlip.setOnClickListener(view -> ImageFlipper.flip(binding.cropImageView, FlipDirection.HORIZONTAL));
        binding.imageViewSaveRotate.setOnClickListener(view -> new OnSaveCrop().execute(new Void[0]));
        binding.imageViewCloseRotate.setOnClickListener(view -> dismiss());
        return binding.getRoot();
    }
    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static RotateFragment show(@NonNull AppCompatActivity appCompatActivity, OnCropPhoto onCropPhoto2, Bitmap bitmap2) {
        RotateFragment cropDialogFragment = new RotateFragment();
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
