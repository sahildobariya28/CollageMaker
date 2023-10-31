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
import android.view.WindowManager;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.photo.collagemaker.R;
import com.photo.collagemaker.assets.MosaicAsset;
import com.photo.collagemaker.adapters.MosaicAdapter;
import com.photo.collagemaker.databinding.FragmentMosaicBinding;
import com.photo.collagemaker.utils.FilterUtils;

public class MosaicFragment extends DialogFragment implements MosaicAdapter.MosaicChangeListener {
    private static final String TAG = "MosaicFragment";
    public Bitmap adjustBitmap;
    public Bitmap bitmap;
    public MosaicListener mosaicListener;

    public interface MosaicListener {
        void onSaveMosaic(Bitmap bitmap);
    }

    FragmentMosaicBinding binding;

    @SuppressLint("WrongConstant")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {

        binding = FragmentMosaicBinding.inflate(layoutInflater, viewGroup, false);

        binding.mosaicView.setImageBitmap(bitmap);
        binding.mosaicView.setMosaicItem(new MosaicAdapter.MosaicItem(R.drawable.background_blur, 0, MosaicAdapter.BLUR.BLUR));
        adjustBitmap = FilterUtils.getBlurImageFromBitmap(bitmap);
        binding.imageViewBackground.setImageBitmap(adjustBitmap);
        binding.relativeLayoutLoading.setVisibility(View.GONE);
        binding.recyclerViewMoasic.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        binding.recyclerViewMoasic.setAdapter(new MosaicAdapter(getContext(), this));
        binding.imageViewSaveMosaic.setOnClickListener(view -> new SaveMosaicView().execute());
        binding.imageViewCloseMosaic.setOnClickListener(view -> dismiss());
        binding.seekbarMoasic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                binding.mosaicView.setBrushBitmapSize(i + 25);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                binding.mosaicView.updateBrush();
            }
        });
        binding.imageViewUndo.setOnClickListener(view -> binding.mosaicView.undo());
        binding.imageViewRedo.setOnClickListener(view -> binding.mosaicView.redo());
        return binding.getRoot();
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static MosaicFragment show(@NonNull AppCompatActivity appCompatActivity, Bitmap bitmap2, Bitmap bitmap3, MosaicListener mosaicDialogListener2) {
        MosaicFragment mosaicDialog = new MosaicFragment();
        mosaicDialog.setBitmap(bitmap2);
        mosaicDialog.setAdjustBitmap(bitmap3);
        mosaicDialog.setMosaicListener((MosaicListener) mosaicDialogListener2);
        mosaicDialog.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return mosaicDialog;
    }

    public void setMosaicListener(MosaicListener mosaicListener) {
        this.mosaicListener = mosaicListener;
    }

    public void setAdjustBitmap(Bitmap bitmap2) {
        this.adjustBitmap = bitmap2;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
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

    public void onDestroy() {
        super.onDestroy();
        bitmap.recycle();
        bitmap = null;
        adjustBitmap.recycle();
        adjustBitmap = null;
    }

    public void onStop() {
        super.onStop();
    }

    public void onSelected(MosaicAdapter.MosaicItem mosaicItem) {
        if (mosaicItem.mode == MosaicAdapter.BLUR.BLUR) {
            adjustBitmap = FilterUtils.getBlurImageFromBitmap(bitmap);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        } else if (mosaicItem.mode == MosaicAdapter.BLUR.MOSAIC) {
            adjustBitmap = MosaicAsset.getMosaic(bitmap);
            binding.imageViewBackground.setImageBitmap(adjustBitmap);
        }
        binding.mosaicView.setMosaicItem(mosaicItem);
    }

    class SaveMosaicView extends AsyncTask<Void, Bitmap, Bitmap> {
        SaveMosaicView() {
        }

        public void onPreExecute() {
            mLoading(true);
        }

        public Bitmap doInBackground(Void... voidArr) {
            return binding.mosaicView.getBitmap(bitmap, adjustBitmap);
        }

        public void onPostExecute(Bitmap bitmap) {
            mLoading(false);
            mosaicListener.onSaveMosaic(bitmap);
            dismiss();
        }
    }

    public void mLoading(boolean isShowing) {
        if (isShowing) {
            binding.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        binding.relativeLayoutLoading.setVisibility(View.GONE);
    }

}
