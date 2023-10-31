package com.photo.collagemaker.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.FilterAdapter;
import com.photo.collagemaker.assets.FilterCodeAsset;
import com.photo.collagemaker.databinding.FragmentFilterBinding;
import com.photo.collagemaker.listener.FilterListener;
import com.photo.collagemaker.utils.FilterUtils;

import java.util.Arrays;
import java.util.List;

public class FilterFragment extends DialogFragment implements FilterListener {
    private static final String TAG = "FilterFragment";

    public Bitmap bitmap;
    private List<Bitmap> lstFilterBitmap;

    public OnFilterSavePhoto onFilterSavePhoto;

    public interface OnFilterSavePhoto {
        void onSaveFilter(Bitmap bitmap);
    }

    FragmentFilterBinding binding;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentFilterBinding.inflate(layoutInflater, viewGroup, false);

        binding.recyclerViewFilterAll.setAdapter(new FilterAdapter(lstFilterBitmap, this, getContext(), Arrays.asList(FilterCodeAsset.ALL_FILTERS)));
        binding.imageViewPreview.setImageBitmap(bitmap);
        binding.textViewTitle.setText("FILTER");
        binding.imageViewSaveFilter.setOnClickListener(view -> {
            onFilterSavePhoto.onSaveFilter(((BitmapDrawable) binding.imageViewPreview.getDrawable()).getBitmap());
            dismiss();
        });
        binding.imageViewCloseFilter.setOnClickListener(view -> dismiss());

        return binding.getRoot();
    }

    public void setOnFilterSavePhoto(OnFilterSavePhoto onFilterSavePhoto2) {
        this.onFilterSavePhoto = onFilterSavePhoto2;
    }

    public void setLstFilterBitmap(List<Bitmap> list) {
        this.lstFilterBitmap = list;

    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static FilterFragment show(@NonNull AppCompatActivity appCompatActivity, OnFilterSavePhoto onFilterSavePhoto2, Bitmap bitmap2, List<Bitmap> list) {
        FilterFragment filterDialogFragment = new FilterFragment();
        filterDialogFragment.setBitmap(bitmap2);
        filterDialogFragment.setOnFilterSavePhoto(onFilterSavePhoto2);
        filterDialogFragment.setLstFilterBitmap(list);
        filterDialogFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return filterDialogFragment;
    }

    public void onDestroy() {
        super.onDestroy();
        lstFilterBitmap.clear();
    }



    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(layoutParams);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xFF000000)); // Set your desired color
        }
    }

    public void onFilterSelected(String str) {
        new LoadBitmapWithFilter().execute(new String[]{str});
    }

    class LoadBitmapWithFilter extends AsyncTask<String, Bitmap, Bitmap> {
        LoadBitmapWithFilter() {}

        public Bitmap doInBackground(String... strArr) {
            return FilterUtils.getBitmapWithFilter(bitmap, strArr[0]);
        }

        public void onPostExecute(Bitmap bitmap) {
            binding.imageViewPreview.setImageBitmap(bitmap);
        }
    }

}
