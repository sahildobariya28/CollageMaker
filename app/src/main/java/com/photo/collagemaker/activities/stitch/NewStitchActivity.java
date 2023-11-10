package com.photo.collagemaker.activities.stitch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.PhotoShareActivity;
import com.photo.collagemaker.activities.picker.MultipleImagePickerActivity;
import com.photo.collagemaker.databinding.ActivityNewStitchBinding;
import com.photo.collagemaker.utils.SaveFileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewStitchActivity extends AppCompatActivity {

    ActivityNewStitchBinding binding;
    public List<String> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewStitchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageList = getIntent().getStringArrayListExtra(MultipleImagePickerActivity.KEY_DATA_RESULT);

        binding.btnBack.setOnClickListener(view -> onBackPressed());

        binding.verticalStitchScrollView.post(() -> {
            binding.imgVertical.setColorFilter(ContextCompat.getColor(this, R.color.text_color_theme), PorterDuff.Mode.SRC_IN);
            binding.imgHorizontal.setColorFilter(ContextCompat.getColor(this, R.color.text_color_dark), PorterDuff.Mode.SRC_IN);
            binding.textVertical.setTextColor(ContextCompat.getColor(this, R.color.text_color_theme));
            binding.textHorizontal.setTextColor(ContextCompat.getColor(this, R.color.text_color_dark));

            binding.verticalStitchScrollView.setVisibility(View.VISIBLE);
            binding.horizontalStitchScrollView.setVisibility(View.GONE);

            initVerticalStitch();
            setLoading(false);
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

            new SaveCollageAsFile().execute();

        });

    }

    public void setLoading(boolean isShowing) {
        if (isShowing) {
            binding.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        binding.relativeLayoutLoading.setVisibility(View.GONE);
    }

    public void initVerticalStitch() {
        for (int i = 0; i < imageList.size(); i++) {
            ImageView imageView = new ImageView(this);
            Glide.with(this)
                    .asBitmap()
                    .load("file:///" + imageList.get(i))
                    .centerInside()
                    .into(imageView);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setLayoutParams(layoutParams);

// Add the ImageView to the LinearLayout
            binding.verticalStitch.setOrientation(LinearLayout.VERTICAL);
            binding.verticalStitch.addView(imageView);
        }
    }

    public void initHorizontalStitch() {
        for (int i = 0; i < imageList.size(); i++) {
            ImageView imageView = new ImageView(this);
            Glide.with(this)
                    .asBitmap()
                    .load("file:///" + imageList.get(i))
                    .centerInside()
                    .into(imageView);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setLayoutParams(layoutParams);

// Add the ImageView to the LinearLayout
            binding.verticalStitch.setOrientation(LinearLayout.HORIZONTAL);
            binding.verticalStitch.addView(imageView);
        }
    }

    class SaveCollageAsFile extends AsyncTask<String, String, String> {
        @Override
        public void onPreExecute() {
            setLoading(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            Bitmap bitmap;
            if (binding.verticalStitchScrollView.getVisibility() == View.VISIBLE) {
                bitmap = getBitmapFromView(binding.verticalStitch);
            } else {
                bitmap = getBitmapFromView(binding.horizontalStitch);
            }
            try {
                File image = SaveFileUtils.saveBitmapFileCollage(NewStitchActivity.this, bitmap, new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()));

                return image.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        public void onPostExecute(String str) {
            setLoading(false);
            Intent intent = new Intent(NewStitchActivity.this, PhotoShareActivity.class);
            intent.putExtra("path", str);
            startActivity(intent);
        }
    }

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
}