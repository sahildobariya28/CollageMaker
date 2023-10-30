package com.photo.collagemaker.activities.stitch;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.picker.MultipleImagePickerActivity;
import com.photo.collagemaker.databinding.ActivityStitchBinding;
import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.utils.CollageUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class StitchActivity extends AppCompatActivity implements ImageStitchingView.OnGenerateBitmapListener {

    public QueShotLayout queShotLayout;
    public List<String> imageList;
    public List<Target> targets = new ArrayList();
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
                binding.verticalStitchScrollView.setVisibility(View.VISIBLE);
                binding.horizontalStitchScrollView.setVisibility(View.GONE);
                initStitchView();
                setStitchSize();
                setLoading(false);
            });
        });

        binding.btnVertical.setOnClickListener(view -> {
            binding.verticalStitchScrollView.setVisibility(View.VISIBLE);
            binding.horizontalStitchScrollView.setVisibility(View.GONE);
        });

        binding.btnHorizontal.setOnClickListener(view -> {
            binding.verticalStitchScrollView.setVisibility(View.GONE);
            binding.horizontalStitchScrollView.setVisibility(View.VISIBLE);
        });


    }


    public void initStitchView() {
        //vertical
        queShotLayout = CollageUtils.getStitchLayouts(imageList.size(), true).get(0);
        binding.verticalCollageView.setQueShotLayout(queShotLayout);
        binding.verticalCollageView.setTouchEnable(true);
        binding.verticalCollageView.setNeedDrawLine(false);
        binding.verticalCollageView.setNeedDrawOuterLine(false);
        binding.verticalCollageView.setLineSize(4);
        binding.verticalCollageView.setCollagePadding(6.0f);
        binding.verticalCollageView.setCollageRadian(15.0f);
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
        binding.horizontalCollageView.setCollagePadding(6.0f);
        binding.horizontalCollageView.setCollageRadian(15.0f);
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
        final ArrayList arrayList = new ArrayList();
        if (imageList.size() > queShotLayout.getAreaCount()) {
            i = queShotLayout.getAreaCount();
        } else {
            i = imageList.size();
        }
        for (int i2 = 0; i2 < i; i2++) {
            Target r4 = new Target() {
                public void onBitmapFailed(Exception exc, Drawable drawable) {
                }

                public void onPrepareLoad(Drawable drawable) {
                }

                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {

                    arrayList.add(bitmap);
                    if (arrayList.size() == i) {
                        if (imageList.size() < queShotLayout.getAreaCount()) {
                            for (int i = 0; i < queShotLayout.getAreaCount(); i++) {
                                binding.verticalCollageView.addQuShotCollage((Bitmap) arrayList.get(i));
                            }
                        } else {
                            binding.verticalCollageView.addPieces(arrayList);
                        }
                    }
                    targets.remove(this);
                }
            };
            int deviceWidth = getResources().getDisplayMetrics().widthPixels;
            Picasso.get().load("file:///" + imageList.get(i2)).resize(deviceWidth, deviceWidth).centerInside().config(Bitmap.Config.RGB_565).into(r4);
            targets.add(r4);
        }
    }

    public void loadHorizontalPhoto() {
        final int i;
        final ArrayList arrayList = new ArrayList();
        if (imageList.size() > queShotLayout.getAreaCount()) {
            i = queShotLayout.getAreaCount();
        } else {
            i = imageList.size();
        }
        for (int i2 = 0; i2 < i; i2++) {
            Target r4 = new Target() {
                public void onBitmapFailed(Exception exc, Drawable drawable) {
                }

                public void onPrepareLoad(Drawable drawable) {
                }

                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {

                    arrayList.add(bitmap);
                    if (arrayList.size() == i) {
                        if (imageList.size() < queShotLayout.getAreaCount()) {
                            for (int i = 0; i < queShotLayout.getAreaCount(); i++) {
                                binding.horizontalCollageView.addQuShotCollage((Bitmap) arrayList.get(i));
                            }
                        } else {
                            binding.horizontalCollageView.addPieces(arrayList);
                        }
                    }
                    targets.remove(this);
                }
            };
            int deviceWidth = getResources().getDisplayMetrics().widthPixels;
            Picasso.get().load("file:///" + imageList.get(i2)).resize(deviceWidth, deviceWidth).centerInside().config(Bitmap.Config.RGB_565).into(r4);
            targets.add(r4);
        }
    }

    public void setLoading(boolean z) {
        if (z) {
            getWindow().setFlags(16, 16);
            binding.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        getWindow().clearFlags(16);
        binding.relativeLayoutLoading.setVisibility(View.GONE);
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onResourceReady(Bitmap bitmap) {

    }
}