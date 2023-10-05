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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.adapters.DodgeAdapter;
import com.photo.collagemaker.assets.EffectCodeAsset;
import com.photo.collagemaker.databinding.FragmentFilterBinding;
import com.photo.collagemaker.listener.DodgeListener;
import com.photo.collagemaker.utils.FilterUtils;

import java.util.Arrays;
import java.util.List;

public class DodgeFragment extends DialogFragment implements DodgeListener {
    private static final String TAG = "DodgeFragment";

    public Bitmap bitmap;
    private List<Bitmap> lstFilterBitmap;

    public OnFilterSavePhoto onFilterSavePhoto;


    public interface OnFilterSavePhoto {
        void onSaveFilter(Bitmap bitmap);
    }

    FragmentFilterBinding binding;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {

        binding = FragmentFilterBinding.inflate(layoutInflater, viewGroup, false);



        binding.recyclerViewFilterAll.setAdapter(new DodgeAdapter(lstFilterBitmap, this, getContext(), Arrays.asList(EffectCodeAsset.DODGE_EFFECTS)));

        binding.imageViewPreview.setImageBitmap(bitmap);

        binding.textViewTitle.setText("Burn");
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

    public static DodgeFragment show(@NonNull AppCompatActivity appCompatActivity, OnFilterSavePhoto onFilterSavePhoto2, Bitmap bitmap2, List<Bitmap> list) {
        DodgeFragment filterDialogFragment = new DodgeFragment();
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
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
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
