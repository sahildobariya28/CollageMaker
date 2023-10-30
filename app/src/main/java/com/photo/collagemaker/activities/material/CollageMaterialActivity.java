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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.photo.collagemaker.R;
import com.photo.collagemaker.databinding.ActivityCollageMaterialBinding;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollageMaterialActivity extends AppCompatActivity {

    VectorDrawableCompat.VFullPath rectangle, star;


    ActivityCollageMaterialBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollageMaterialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        VectorChildFinder vector = new VectorChildFinder(this,
                R.drawable.collage_material, binding.image);

        rectangle = vector.findPathByName("rectangle");
        star = vector.findPathByName("star");

        rectangle.setFillColor(getResources().getColor(android.R.color.holo_red_light));
        star.setFillColor(getResources().getColor(android.R.color.holo_green_dark));


        binding.image.invalidate();


        binding.image.post(() -> {
            List<Bitmap> dynamicImages = new ArrayList<>();
            Bitmap jpgBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.colored_1);
            Bitmap jpgBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.colored_12);
            dynamicImages.add(jpgBitmap1);
            dynamicImages.add(jpgBitmap2);

            Bitmap maskedImage = maskImages(dynamicImages, getAllPathDataFromVectorDrawable(R.drawable.collage_material), binding.image.getWidth(), binding.image.getHeight());
            binding.image.setImageBitmap(maskedImage);
        });


    }

    public List<String> getAllPathDataFromVectorDrawable(int drawableResId) {
        List<String> pathDataList = new ArrayList<>();

        try {
            XmlPullParser parser = getResources().getXml(drawableResId);

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("path")) {
                    // Fetch the path data from the "android:pathData" attribute
                    String pathData = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "pathData");
                    if (pathData != null) {
                        pathDataList.add(pathData);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pathDataList;
    }


    public Bitmap maskImages(List<Bitmap> bitmaps, List<String> pathDataList, int canvasWidth, int canvasHeight) {
        // Create a mutable Bitmap to draw the masked image
        Bitmap resultBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);

        // Create a canvas for the resultBitmap
        Canvas canvas = new Canvas(resultBitmap);

        // Create Paint with anti-alias
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // Loop through the path data and mask each image separately
        Matrix matrix = new Matrix();

        for (int i = 0; i < pathDataList.size(); i++) {
            Path path = PathParser.createPathFromPathData(pathDataList.get(i));
            Bitmap jpgBitmap = bitmaps.get(i % bitmaps.size()); // Use a bitmap from the list

            BitmapShader shader = new BitmapShader(jpgBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            // Scale the bitmap to fit the path
            RectF pathBounds = new RectF();
            path.computeBounds(pathBounds, true);
            matrix.reset();
            matrix.setRectToRect(new RectF(0, 0, jpgBitmap.getWidth(), jpgBitmap.getHeight()), pathBounds, Matrix.ScaleToFit.CENTER);
            shader.setLocalMatrix(matrix);

            paint.setShader(shader);
            canvas.drawPath(path, paint);
        }

        // The resultBitmap now contains the combined image
        return resultBitmap;
    }
}