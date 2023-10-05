package com.photo.collagemaker.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.adapters.FrameAdapter;
import com.photo.collagemaker.databinding.FragmentFrameBinding;
import com.photo.collagemaker.listener.BrushColorListener;
import com.photo.collagemaker.utils.FilterUtils;
import com.photo.collagemaker.utils.SystemUtil;

public class FrameFragment extends DialogFragment implements
        FrameAdapter.FrameListener, BrushColorListener {
    private static final String TAG = "FrameFragment";

    private Bitmap bitmap;
    private Bitmap blurBitmap;
    public RatioSaveListener ratioSaveListener;

    public interface RatioSaveListener {
        void ratioSavedBitmap(Bitmap bitmap);
    }

    FragmentFrameBinding binding;

    @SuppressLint("WrongConstant")
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        binding = FragmentFrameBinding.inflate(layoutInflater, viewGroup, false);



        binding.relativeLayoutLoading.setVisibility(View.GONE);

        binding.recyclerViewFrame.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        binding.recyclerViewFrame.setAdapter(new FrameAdapter(getContext(), this));
        binding.recyclerViewFrame.setVisibility(View.VISIBLE);

        binding.seekbarFrame.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int dpToPx = SystemUtil.dpToPx(getContext(), i);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) binding.imageViewFrame.getLayoutParams();
                layoutParams.setMargins(dpToPx, dpToPx, dpToPx, dpToPx);
                binding.imageViewFrame.setLayoutParams(layoutParams);
            }
        });

        binding.imageViewFrame.setImageBitmap(bitmap);
        binding.imageViewFrame.setAdjustViewBounds(true);
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.constraintLayoutFrame);
        ConstraintSet constraintSet2 = constraintSet;
        constraintSet2.connect(binding.frameLayoutWrapper.getId(), 3, binding.constraintLayoutFrame.getId(), 3, 0);
        constraintSet2.connect(binding.frameLayoutWrapper.getId(), 1, binding.constraintLayoutFrame.getId(), 1, 0);
        constraintSet2.connect(binding.frameLayoutWrapper.getId(), 4, binding.constraintLayoutFrame.getId(), 4, 0);
        constraintSet2.connect(binding.frameLayoutWrapper.getId(), 2, binding.constraintLayoutFrame.getId(), 2, 0);
        constraintSet.applyTo(binding.constraintLayoutFrame);
        binding.imageViewCloseFrame.setOnClickListener((View.OnClickListener) view -> dismiss());
        binding.imageViewSaveFrame.setOnClickListener((View.OnClickListener) view -> new SaveRatioView().execute(getBitmapFromView(binding.frameLayoutWrapper)));
        return binding.getRoot();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static FrameFragment show(@NonNull AppCompatActivity appCompatActivity, RatioSaveListener ratioSaveListener, Bitmap mBitmap, Bitmap iBitmap) {
        FrameFragment ratioFragment = new FrameFragment();
        ratioFragment.setBitmap(mBitmap);
        ratioFragment.setBlurBitmap(iBitmap);
        ratioFragment.setRatioSaveListener(ratioSaveListener);
        ratioFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return ratioFragment;
    }

    public void setBlurBitmap(Bitmap bitmap2) {
        this.blurBitmap = bitmap2;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public void setRatioSaveListener(RatioSaveListener iRatioSaveListener) {
        this.ratioSaveListener = iRatioSaveListener;
    }



    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }

    public void onStop() {
        super.onStop();
    }

    class SaveRatioView extends AsyncTask<Bitmap, Bitmap, Bitmap> {
        SaveRatioView() {
        }

        public void onPreExecute() {mLoading(true);
        }

        public Bitmap doInBackground(Bitmap... bitmapArr) {
            Bitmap cloneBitmap = FilterUtils.cloneBitmap(bitmapArr[0]);
            bitmapArr[0].recycle();
            bitmapArr[0] = null;
            return cloneBitmap;
        }

        public void onPostExecute(Bitmap bitmap) {
            mLoading(false);
            ratioSaveListener.ratioSavedBitmap(bitmap);
            dismiss();
        }
    }

    public void onBackgroundSelected(FrameAdapter.SquareView squareView) {
        if (squareView.isColor) {
            binding.frameLayoutWrapper.setBackgroundColor(squareView.drawableId);
        }  else {
            binding.frameLayoutWrapper.setBackgroundResource(squareView.drawableId);
        }
        binding.frameLayoutWrapper.invalidate();
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (blurBitmap != null) {
            blurBitmap.recycle();
            blurBitmap = null;
        }
        bitmap = null;
    }

    public void onColorChanged(String str) {
        binding.imageViewFrame.setBackgroundColor(Color.parseColor(str));
        if (!str.equals("#00000000")) {
            int dpToPx = SystemUtil.dpToPx(getContext(), 35);
            binding.imageViewFrame.setPadding(dpToPx, dpToPx, dpToPx, dpToPx);
            return;
        }
        binding.imageViewFrame.setPadding(0, 0, 0, 0);
    }

    public Bitmap getBitmapFromView(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
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
