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
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.AspectAdapter;
import com.photo.collagemaker.adapters.RatioAdapter;
import com.photo.collagemaker.databinding.FragmentRatioBinding;
import com.photo.collagemaker.listener.BrushColorListener;
import com.photo.collagemaker.utils.FilterUtils;
import com.photo.collagemaker.utils.SystemUtil;
import com.steelkiwi.cropiwa.AspectRatio;

public class RatioFragment extends DialogFragment implements AspectAdapter.OnNewSelectedListener,
        RatioAdapter.RatioListener, BrushColorListener {
    private static final String TAG = "RatioFragment";
    private Bitmap bitmap;
    private Bitmap blurBitmap;
    private AspectRatio aspectRatio;
    public RatioSaveListener ratioSaveListener;


    FragmentRatioBinding binding;


    public interface RatioSaveListener {
        void ratioSavedBitmap(Bitmap bitmap);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static RatioFragment show(@NonNull AppCompatActivity appCompatActivity, RatioSaveListener ratioSaveListener, Bitmap mBitmap, Bitmap iBitmap) {
        RatioFragment ratioFragment = new RatioFragment();
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



    @SuppressLint("WrongConstant")
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        binding = FragmentRatioBinding.inflate(layoutInflater, viewGroup, false);

        AspectAdapter aspectRatioPreviewAdapter = new AspectAdapter(getActivity());
        aspectRatioPreviewAdapter.setListener(this);
        binding.relativeLayoutLoading.setVisibility(View.GONE);
        binding.recyclerViewRatio.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        binding.recyclerViewRatio.setAdapter(aspectRatioPreviewAdapter);
        aspectRatio = new AspectRatio(1, 1);

        binding.seekbarPadding.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int dpToPx = SystemUtil.dpToPx(getContext(), i);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) binding.imageViewRatio.getLayoutParams();
                layoutParams.setMargins(dpToPx, dpToPx, dpToPx, dpToPx);
                binding.imageViewRatio.setLayoutParams(layoutParams);
            }
        });
        binding.imageViewRatio.setImageBitmap(bitmap);
        binding.imageViewRatio.setAdjustViewBounds(true);
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        binding.imageViewBlur.setImageBitmap(blurBitmap);
        binding.frameLayoutWrapper.setLayoutParams(new ConstraintLayout.LayoutParams(point.x, point.x));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.constraintLayoutRatio);
        ConstraintSet constraintSet2 = constraintSet;
        constraintSet2.connect(binding.frameLayoutWrapper.getId(), 3, binding.constraintLayoutRatio.getId(), 3, 0);
        constraintSet2.connect(binding.frameLayoutWrapper.getId(), 1, binding.constraintLayoutRatio.getId(), 1, 0);
        constraintSet2.connect(binding.frameLayoutWrapper.getId(), 4, binding.constraintLayoutRatio.getId(), 4, 0);
        constraintSet2.connect(binding.frameLayoutWrapper.getId(), 2, binding.constraintLayoutRatio.getId(), 2, 0);
        constraintSet.applyTo(binding.constraintLayoutRatio);
        binding.imageViewCloseRatio.setOnClickListener(view -> dismiss());
        binding.imageViewSaveRatio.setOnClickListener(view -> new SaveRatioView().execute(getBitmapFromView(binding.frameLayoutWrapper)));
        return binding.getRoot();
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

    private int[] calculateWidthAndHeight(AspectRatio aspectRatio, Point point) {
        int height = binding.constraintLayoutRatio.getHeight();
        if (aspectRatio.getHeight() > aspectRatio.getWidth()) {
            int mRatio = (int) (aspectRatio.getRatio() * ((float) height));
            if (mRatio < point.x) {
                return new int[]{mRatio, height};
            }
            return new int[]{point.x, (int) (((float) point.x) / aspectRatio.getRatio())};
        }
        int iRatio = (int) (((float) point.x) / aspectRatio.getRatio());
        if (iRatio > height) {
            return new int[]{(int) (((float) height) * aspectRatio.getRatio()), height};
        }
        return new int[]{point.x, iRatio};
    }

    private int[] calculateWidthAndHeightReal(AspectRatio aspectRatio, Point point) {
        int height = bitmap.getHeight();
        if (aspectRatio.getHeight() > aspectRatio.getWidth()) {
            int ratio2 = (int) (aspectRatio.getRatio() * ((float) height));
            if (ratio2 < point.x) {
                return new int[]{ratio2, height};
            }
            return new int[]{point.x, (int) (((float) point.x) / aspectRatio.getRatio())};
        }
        int ratio3 = (int) (((float) point.x) / aspectRatio.getRatio());
        if (ratio3 > height) {
            return new int[]{(int) (((float) height) * aspectRatio.getRatio()), height};
        }
        return new int[]{point.x, ratio3};
    }

    public void onNewAspectRatioSelected(AspectRatio aspectRatio) {
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        aspectRatio = aspectRatio;
        int[] calculateWidthAndHeight = calculateWidthAndHeight(aspectRatio, point);
        binding.frameLayoutWrapper.setLayoutParams(new ConstraintLayout.LayoutParams(calculateWidthAndHeight[0], calculateWidthAndHeight[1]));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.constraintLayoutRatio);
        ConstraintSet constraintSet2 = constraintSet;
        constraintSet2.connect(binding.frameLayoutWrapper.getId(), 3, binding.constraintLayoutRatio.getId(), 3, 0);
        constraintSet2.connect(binding.frameLayoutWrapper.getId(), 1, binding.constraintLayoutRatio.getId(), 1, 0);
        constraintSet2.connect(binding.frameLayoutWrapper.getId(), 4, binding.constraintLayoutRatio.getId(), 4, 0);
        constraintSet2.connect(binding.frameLayoutWrapper.getId(), 2, binding.constraintLayoutRatio.getId(), 2, 0);
        constraintSet.applyTo(binding.constraintLayoutRatio);
    }

    class SaveRatioView extends AsyncTask<Bitmap, Bitmap, Bitmap> {
        SaveRatioView() {
        }

        public void onPreExecute() {
            mLoading(true);
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

    public void onBackgroundSelected(RatioAdapter.SquareView squareView) {
        if (squareView.isColor) {
            binding.frameLayoutWrapper.setBackgroundColor(squareView.drawableId);
        } else if (squareView.text.equals("Blur")) {
            binding.imageViewBlur.setVisibility(View.VISIBLE);
        } else {
            binding.frameLayoutWrapper.setBackgroundResource(squareView.drawableId);
            binding.imageViewBlur.setVisibility(View.GONE);
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
        binding.imageViewRatio.setBackgroundColor(Color.parseColor(str));
        if (!str.equals("#00000000")) {
            int dpToPx = SystemUtil.dpToPx(getContext(), 3);
            binding.imageViewRatio.setPadding(dpToPx, dpToPx, dpToPx, dpToPx);
            return;
        }
        binding.imageViewRatio.setPadding(0, 0, 0, 0);
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
