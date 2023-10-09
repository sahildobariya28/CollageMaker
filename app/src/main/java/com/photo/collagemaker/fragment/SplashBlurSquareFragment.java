package com.photo.collagemaker.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.photo.collagemaker.R;
import com.photo.collagemaker.adapters.SplashSquareAdapter;
import com.photo.collagemaker.databinding.FragmentSplashSquareBinding;
import com.photo.collagemaker.assets.StickersAsset;
import com.photo.collagemaker.custom_view.SplashSticker;
import com.photo.collagemaker.utils.FilterUtils;

public class SplashBlurSquareFragment extends DialogFragment implements SplashSquareAdapter.SplashChangeListener {
    private static final String TAG = "SplashBlurSquareFragment";
    public Bitmap bitmap;
    private Bitmap blackAndWhiteBitmap;
    private Bitmap blurBitmap;
    private SplashSticker blurSticker;
    public boolean isSplashView;
    public SplashDialogListener splashDialogListener;
    private SplashSticker splashSticker;
    private ViewGroup viewGroup;

    public interface SplashDialogListener {
        void onSaveSplash(Bitmap bitmap);
    }

    FragmentSplashSquareBinding binding;

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup2, @Nullable Bundle bundle) {
        binding = FragmentSplashSquareBinding.inflate(layoutInflater, viewGroup, false);

        viewGroup = viewGroup2;
        binding.relativeLayoutLoading.setVisibility(View.GONE);
        binding.imageViewBackground.setImageBitmap(bitmap);
        if (isSplashView) {
            binding.splashView.setImageBitmap(blackAndWhiteBitmap);
            binding.blurNumberButton.setVisibility(View.GONE);
            binding.textViewTitle.setText("SPLASH SQUARE");
        } else {
            binding.splashView.setImageBitmap(blurBitmap);
            binding.textViewTitle.setText("BLUR SQUARE");
            binding.blurNumberButton.setRange(0, 15);
        }
        binding.blurNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            public void onValueChange(ElegantNumberButton elegantNumberButton, int i, int i2) {
                new LoadBlurBitmap((float) i2).execute(new Void[0]);
            }
        });
        binding.recyclerViewSplashSquare.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.recyclerViewSplashSquare.setHasFixedSize(true);
        binding.recyclerViewSplashSquare.setAdapter(new SplashSquareAdapter(getContext(), this, isSplashView));
        if (isSplashView) {
            splashSticker = new SplashSticker(StickersAsset.loadBitmapFromAssets(getContext(), "square/mask/m1.png"), StickersAsset.loadBitmapFromAssets(getContext(), "square/frame/f1.png"));
            binding.splashView.addSticker(splashSticker);
        } else {
            blurSticker = new SplashSticker(StickersAsset.loadBitmapFromAssets(getContext(), "square/mask/m1.png"), StickersAsset.loadBitmapFromAssets(getContext(), "square/frame/f1.png"));
            binding.splashView.addSticker(blurSticker);
        }
        binding.splashView.refreshDrawableState();
        binding.splashView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        binding.textViewTitle.setOnClickListener(view -> {
            binding.splashView.setcSplashMode(0);
            binding.recyclerViewSplashSquare.setVisibility(View.VISIBLE);
            binding.splashView.refreshDrawableState();
            binding.splashView.invalidate();
        });
        binding.imageViewSaveSplash.setOnClickListener(view -> {
            splashDialogListener.onSaveSplash(binding.splashView.getBitmap(bitmap));
            dismiss();
        });
        binding.imageViewCloseSplash.setOnClickListener(view -> dismiss());
        return binding.getRoot();
    }

    public void setSplashView(boolean z) {
        this.isSplashView = z;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static SplashBlurSquareFragment show(@NonNull AppCompatActivity appCompatActivity, Bitmap bitmap2, Bitmap bitmap3, Bitmap bitmap4, SplashDialogListener splashDialogListener2, boolean z) {
        SplashBlurSquareFragment splashDialog = new SplashBlurSquareFragment();
        splashDialog.setBlurBitmap(bitmap3);
        splashDialog.setBitmap(bitmap2);
        splashDialog.setBlackAndWhiteBitmap(bitmap4);
        splashDialog.setSplashDialogListener(splashDialogListener2);
        splashDialog.setSplashView(z);
        splashDialog.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return splashDialog;
    }

    public void setBlackAndWhiteBitmap(Bitmap bitmap2) {
        this.blackAndWhiteBitmap = bitmap2;
    }

    public void setBlurBitmap(Bitmap bitmap2) {
        this.blurBitmap = bitmap2;
    }

    public void setSplashDialogListener(SplashDialogListener splashDialogListener2) {
        this.splashDialogListener = splashDialogListener2;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }





    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }

    public void onDestroy() {
        super.onDestroy();
        binding.splashView.getSticker().release();
        if (blurBitmap != null) {
            blurBitmap.recycle();
        }
        blurBitmap = null;
        if (blackAndWhiteBitmap != null) {
            blackAndWhiteBitmap.recycle();
        }
        blackAndWhiteBitmap = null;
        bitmap = null;
    }

    public void onSelected(SplashSticker splashSticker2) {
        binding.splashView.addSticker(splashSticker2);
    }

    class LoadBlurBitmap extends AsyncTask<Void, Bitmap, Bitmap> {
        private float intensity;

        public LoadBlurBitmap(float f) {
            intensity = f;
        }

        public void onPreExecute() {
            showLoading(true);
        }

        public Bitmap doInBackground(Void... voidArr) {
            return FilterUtils.getBlurImageFromBitmap(bitmap, intensity);
        }

        public void onPostExecute(Bitmap bitmap) {
            showLoading(false);
            binding.splashView.setImageBitmap(bitmap);
        }
    }

    public void showLoading(boolean z) {
        if (z) {
            getActivity().getWindow().setFlags(16, 16);
            binding.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        getActivity().getWindow().clearFlags(16);
        binding.relativeLayoutLoading.setVisibility(View.GONE);
    }

}
