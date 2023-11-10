package com.photo.collagemaker.activities.stitch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.PhotoShareActivity;
import com.photo.collagemaker.activities.picker.MultipleImagePickerActivity;
import com.photo.collagemaker.databinding.ActivityStitchBinding;
import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.picker.PermissionsUtils;
import com.photo.collagemaker.utils.CollageUtils;
import com.photo.collagemaker.utils.SaveFileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StitchActivity extends AppCompatActivity implements ImageStitchingView.OnGenerateBitmapListener {

    public QueShotLayout queShotLayout;
    public List<String> imageList;
    public List<CustomTarget<Bitmap>> targets = new ArrayList<>();
    ActivityStitchBinding binding;
    int stitchWidth, stitchHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStitchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageList = getIntent().getStringArrayListExtra(MultipleImagePickerActivity.KEY_DATA_RESULT);

        binding.verticalStitchScrollView.post(() -> {
            binding.horizontalStitchScrollView.post(() -> {
                binding.imgVertical.setColorFilter(ContextCompat.getColor(this, R.color.text_color_theme), PorterDuff.Mode.SRC_IN);
                binding.imgHorizontal.setColorFilter(ContextCompat.getColor(this, R.color.text_color_dark), PorterDuff.Mode.SRC_IN);
                binding.textVertical.setTextColor(ContextCompat.getColor(this, R.color.text_color_theme));
                binding.textHorizontal.setTextColor(ContextCompat.getColor(this, R.color.text_color_dark));

                binding.verticalStitchScrollView.setVisibility(View.VISIBLE);
                binding.horizontalStitchScrollView.setVisibility(View.GONE);
                initStitchView();
                setStitchSize();
                setLoading(false);
            });
        });

        binding.btnVertical.setOnClickListener(view -> {

            binding.imgVertical.setColorFilter(ContextCompat.getColor(this, R.color.text_color_theme), PorterDuff.Mode.SRC_IN);
            binding.imgHorizontal.setColorFilter(ContextCompat.getColor(this, R.color.text_color_dark), PorterDuff.Mode.SRC_IN);
            binding.textVertical.setTextColor(ContextCompat.getColor(this, R.color.text_color_theme));
            binding.textHorizontal.setTextColor(ContextCompat.getColor(this, R.color.text_color_dark));

            binding.verticalStitchScrollView.setVisibility(View.VISIBLE);
            binding.horizontalStitchScrollView.setVisibility(View.GONE);
        });

        binding.btnHorizontal.setOnClickListener(view -> {
            binding.imgVertical.setColorFilter(ContextCompat.getColor(this, R.color.text_color_dark), PorterDuff.Mode.SRC_IN);
            binding.imgHorizontal.setColorFilter(ContextCompat.getColor(this, R.color.text_color_theme), PorterDuff.Mode.SRC_IN);
            binding.textVertical.setTextColor(ContextCompat.getColor(this, R.color.text_color_dark));
            binding.textHorizontal.setTextColor(ContextCompat.getColor(this, R.color.text_color_theme));

            binding.verticalStitchScrollView.setVisibility(View.GONE);
            binding.horizontalStitchScrollView.setVisibility(View.VISIBLE);
        });

        binding.btnSave.setOnClickListener(view -> {
            if (PermissionsUtils.checkWriteStoragePermission(StitchActivity.this)) {

                if (binding.verticalStitchScrollView.getVisibility() == View.VISIBLE){
                    Bitmap createBitmap = SaveFileUtils.createBitmap(binding.verticalCollageView, 1920);
                    Bitmap createBitmap2 = binding.verticalCollageView.createBitmap();
                    new SaveCollageAsFile().execute(createBitmap, createBitmap2);
                }else {
                    Bitmap createBitmap = SaveFileUtils.createBitmap(binding.horizontalCollageView, 1920);
                    Bitmap createBitmap2 = binding.horizontalCollageView.createBitmap();
                    new SaveCollageAsFile().execute(createBitmap, createBitmap2);
                }

            }

        });
        binding.btnBack.setOnClickListener(view -> onBackPressed());
    }


    public void initStitchView() {
        //vertical
        queShotLayout = CollageUtils.getStitchLayouts(imageList.size(), true).get(0);
        binding.verticalCollageView.setQueShotLayout(queShotLayout);
        binding.verticalCollageView.setTouchEnable(true);
        binding.verticalCollageView.setNeedDrawLine(false);
        binding.verticalCollageView.setNeedDrawOuterLine(false);
        binding.verticalCollageView.setLineSize(4);
        binding.verticalCollageView.setLineColor(ContextCompat.getColor(this, R.color.theme_color_dark));
        binding.verticalCollageView.setSelectedLineColor(ContextCompat.getColor(this, R.color.theme_color));
        binding.verticalCollageView.setHandleBarColor(ContextCompat.getColor(this, R.color.theme_color));
        binding.verticalCollageView.setAnimateDuration(300);
        binding.verticalCollageView.setOnQueShotSelectedListener((collage, i) -> {
            binding.verticalStitchScrollView.setActivated(false);
        });
        binding.verticalCollageView.setOnQueShotUnSelectedListener(() -> {
            binding.verticalStitchScrollView.setActivated(true);
        });
        binding.verticalCollageView.post(this::loadVerticalPhoto);

        //horizontal
        queShotLayout = CollageUtils.getStitchLayouts(imageList.size(), false).get(0);
        binding.horizontalCollageView.setQueShotLayout(queShotLayout);
        binding.horizontalCollageView.setTouchEnable(true);
        binding.horizontalCollageView.setNeedDrawLine(false);
        binding.horizontalCollageView.setNeedDrawOuterLine(false);
        binding.horizontalCollageView.setLineSize(4);
        binding.horizontalCollageView.setLineColor(ContextCompat.getColor(this, R.color.theme_color_dark));
        binding.horizontalCollageView.setSelectedLineColor(ContextCompat.getColor(this, R.color.theme_color));
        binding.horizontalCollageView.setHandleBarColor(ContextCompat.getColor(this, R.color.theme_color));
        binding.horizontalCollageView.setAnimateDuration(300);

        binding.horizontalCollageView.setOnQueShotSelectedListener((collage, i) -> {
            binding.horizontalStitchScrollView.setEnabled(false);
        });
        binding.horizontalCollageView.setOnQueShotUnSelectedListener(() -> {
            binding.horizontalStitchScrollView.setEnabled(true);
        });
        binding.horizontalCollageView.post(this::loadHorizontalPhoto);

  binding.verticalCollageView.setLocked(false);
        binding.verticalCollageView.setTouchEnable(false);
        binding.horizontalCollageView.setLocked(false);
        binding.horizontalCollageView.setTouchEnable(false);

    }


    public void setStitchSize() {
        stitchWidth = binding.verticalStitchScrollView.getMeasuredWidth() / 2;
        stitchHeight = binding.horizontalStitchScrollView.getMeasuredHeight() / 2;


//        //imageStitch
//        for (int i = 0; i < imageList.size(); i++) {
//
//            Bitmap bitmap = new BitmapDrawable(getResources(), imageList.get(i)).getBitmap();
//            binding.isvTest.addImage(Bitmap.createScaledBitmap(bitmap, stitchWidth, stitchWidth + 100, false));
//        }
//        binding.isvTest.setOnGenerateBitmapListener(this);


        //vertical
        ViewGroup.LayoutParams params = binding.verticalCollageView.getLayoutParams();
        params.width = stitchWidth;
        params.height = (stitchWidth * imageList.size());
        binding.verticalCollageView.setLayoutParams(params);

        //horizontal
        ViewGroup.LayoutParams params1 = binding.horizontalCollageView.getLayoutParams();
        params1.width = (stitchHeight * imageList.size());
        params1.height = stitchHeight;
        binding.horizontalCollageView.setLayoutParams(params1);

    }

    public void loadVerticalPhoto() {
        final int i;
        final ArrayList<Bitmap> arrayList = new ArrayList<>();
        if (imageList.size() > queShotLayout.getAreaCount()) {
            i = queShotLayout.getAreaCount();
        } else {
            i = imageList.size();
        }
        for (int i2 = 0; i2 < i; i2++) {
            CustomTarget<Bitmap> target = new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    arrayList.add(bitmap);
                    if (arrayList.size() == i) {
                        if (imageList.size() < queShotLayout.getAreaCount()) {
                            for (int i = 0; i < queShotLayout.getAreaCount(); i++) {
                                binding.verticalCollageView.addQuShotCollage(arrayList.get(i));
                            }
                        } else {
                            binding.verticalCollageView.addPieces(arrayList);
                        }
                        targets.remove(this);
                    }
                }

                @Override
                public void onLoadCleared(Drawable placeholder) {
                    // Handle clearing if needed
                }
            };
            int deviceWidth = getResources().getDisplayMetrics().widthPixels;
            Glide.with(this)
                    .asBitmap()
                    .load("file:///" + imageList.get(i2))
                    .override(deviceWidth, deviceWidth)
                    .centerInside()
                    .into(target);
            targets.add(target);
        }
    }

    public void loadHorizontalPhoto() {
        final int i;
        final ArrayList<Bitmap> arrayList = new ArrayList<>();
        if (imageList.size() > queShotLayout.getAreaCount()) {
            i = queShotLayout.getAreaCount();
        } else {
            i = imageList.size();
        }
        for (int i2 = 0; i2 < i; i2++) {
            CustomTarget<Bitmap> target = new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    arrayList.add(bitmap);
                    if (arrayList.size() == i) {
                        if (imageList.size() < queShotLayout.getAreaCount()) {
                            for (int i = 0; i < queShotLayout.getAreaCount(); i++) {
                                binding.horizontalCollageView.addQuShotCollage(arrayList.get(i));
                            }
                        } else {
                            binding.horizontalCollageView.addPieces(arrayList);
                        }
                        targets.remove(this);
                    }
                }

                @Override
                public void onLoadCleared(Drawable placeholder) {
                    // Handle clearing if needed
                }
            };
            int deviceWidth = getResources().getDisplayMetrics().widthPixels;
            Glide.with(this)
                    .asBitmap()
                    .load("file:///" + imageList.get(i2))
                    .override(deviceWidth, deviceWidth)
                    .centerInside()
                    .into(target);
            targets.add(target);
        }
    }
    public void setLoading(boolean isShowing) {
        if (isShowing) {
            binding.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        binding.relativeLayoutLoading.setVisibility(View.GONE);
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onResourceReady(Bitmap bitmap) {

    }

    class SaveCollageAsFile extends AsyncTask<Bitmap, String, String> {
        SaveCollageAsFile() {
        }

        @Override
        public void onPreExecute() {
            setLoading(true);
        }

        @Override
        public String doInBackground(Bitmap... bitmapArr) {
            Bitmap bitmap = bitmapArr[0];
            Bitmap bitmap2 = bitmapArr[1];
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = null;
            canvas.drawBitmap(bitmap, null, new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), paint);
            canvas.drawBitmap(bitmap2, null, new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), paint);
            bitmap.recycle();
            bitmap2.recycle();
            try {
                File image = SaveFileUtils.saveBitmapFileCollage(StitchActivity.this, createBitmap, new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()));
                createBitmap.recycle();
                return image.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(String str) {
            setLoading(false);
            Intent intent = new Intent(StitchActivity.this, PhotoShareActivity.class);
            intent.putExtra("path", str);
            startActivity(intent);
        }
    }
}