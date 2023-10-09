package com.photo.collagemaker.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.photo.collagemaker.R;
import com.photo.collagemaker.databinding.FragmentSplashBinding;
import com.photo.collagemaker.utils.FilterUtils;

public class SplashFragment extends DialogFragment {
    private static final String TAG = "SplashFragment";


    public Bitmap bitmap;
    private Bitmap blackAndWhiteBitmap;
    private Bitmap blurBitmap;

    public boolean isSplashView;
    public SplashListener splashListener;


    public interface SplashListener {
        void onSaveSplash(Bitmap bitmap);
    }

    FragmentSplashBinding binding;

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup2, @Nullable Bundle bundle) {

        binding = FragmentSplashBinding.inflate(layoutInflater, viewGroup2, false);



        binding.relativeLayoutLoading.setVisibility(View.GONE);



        binding.linearLayoutSplash.setVisibility(View.VISIBLE);


        binding.imageViewUndo.setOnClickListener(view -> binding.splashView.undo());

        binding.imageViewRedo.setOnClickListener(view -> binding.splashView.redo());

        binding.seekbarBrush.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                binding.splashView.setBrushSize(i + 25);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                binding.splashView.updateBrush();
            }
        });

        binding.imageViewBackground.setImageBitmap(bitmap);


        if (isSplashView) {
            binding.splashView.setImageBitmap(blackAndWhiteBitmap);
            binding.blurNumberButton.setVisibility(View.GONE);
            binding.textViewTitle.setText("SPLASH");
        } else {
            binding.splashView.setImageBitmap(blurBitmap);
            binding.textViewTitle.setText("BLUR");
            binding.blurNumberButton.setRange(0, 15);
        }
        binding.blurNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            public void onValueChange(ElegantNumberButton elegantNumberButton, int i, int i2) {
                new LoadBlurBitmap((float) i2).execute(new Void[0]);
            }
        });
        binding.splashView.refreshDrawableState();
        binding.splashView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        binding.splashView.setcSplashMode(5);
        binding.imageViewSaveSplash.setOnClickListener(view -> {
            splashListener.onSaveSplash(binding.splashView.getBitmap(bitmap));
            dismiss();
        });
        binding.imageViewCloseSplash.setOnClickListener(view -> dismiss());
        return binding.getRoot();
    }

    public void setQuShotSplashView(boolean z) {
        this.isSplashView = z;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static SplashFragment show(@NonNull AppCompatActivity appCompatActivity, Bitmap bitmap2, Bitmap bitmap3, Bitmap bitmap4, SplashListener splashDialogListener2, boolean z) {
        SplashFragment splashDialog = new SplashFragment();
        splashDialog.setBlurBitmap(bitmap3);
        splashDialog.setBitmap(bitmap2);
        splashDialog.setBlackAndWhiteBitmap(bitmap4);
        splashDialog.setSplashListener(splashDialogListener2);
        splashDialog.setQuShotSplashView(z);
        splashDialog.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return splashDialog;
    }

    public void setBlackAndWhiteBitmap(Bitmap bitmap2) {
        this.blackAndWhiteBitmap = bitmap2;
    }

    public void setBlurBitmap(Bitmap bitmap2) {
        this.blurBitmap = bitmap2;
    }

    public void setSplashListener(SplashListener splashDialogListener2) {
        this.splashListener = splashDialogListener2;
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
