package com.photo.collagemaker.activities.material;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.PathParser;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.material.material_assert.MaterialModel;
import com.photo.collagemaker.activities.material.util.MaskImage;
import com.photo.collagemaker.activities.picker.MultipleImagePickerActivity;
import com.photo.collagemaker.databinding.ActivityCollageMaterialBinding;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollageMaterialActivity extends AppCompatActivity {

    ActivityCollageMaterialBinding binding;
    ArrayList<MaterialModel> listMaterialModel = new ArrayList<>();
    public ArrayList<String> imageList;
    int selectedPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollageMaterialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        selectedPosition = getIntent().getIntExtra("position", 0);
        imageList = getIntent().getStringArrayListExtra(MultipleImagePickerActivity.KEY_DATA_RESULT);

        listMaterialModel = MaskImage.fetchAllMaterialTheme();

        binding.image.post(() -> {
            List<Bitmap> dynamicImages = new ArrayList<>();
            for (int i = 0; i < imageList.size(); i++) {
                dynamicImages.add(new BitmapDrawable(getResources(), imageList.get(i)).getBitmap());
            }

            Bitmap maskedImage = MaskImage.maskImages(this, dynamicImages, listMaterialModel.get(selectedPosition).getDrawableId(), binding.image.getWidth(), binding.image.getHeight());
            binding.image.setImageBitmap(maskedImage);
        });
    }


}