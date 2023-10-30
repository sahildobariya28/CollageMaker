package com.photo.collagemaker.activities.material.util;

import android.app.Activity;
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
import android.util.Log;

import androidx.core.graphics.PathParser;

import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.material.material_assert.MaterialModel;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class MaskImage {

    public static List<String> getAllPathDataFromVectorDrawable(Activity activity, int drawableResId) {
        List<String> pathDataList = new ArrayList<>();

        try {
            XmlPullParser parser = activity.getResources().getXml(drawableResId);

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("path")) {

                    String pathData = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "pathData");
                    if (pathData != null) {
                        pathDataList.add(pathData);
                    }
                }
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("vector")) {
                    String width = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "viewportWidth");
                    String height = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "viewportHeight");
                    Log.d("sjdfkljwueqw3", "getAllPathDataFromVectorDrawable: " + width + "   " + height );



                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pathDataList;
    }

//    public static Bitmap maskImages(Activity activity, List<Bitmap> imageList, int drawableId, int canvasWidth, int canvasHeight) {
//        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.collage_material_1);
//        List<String> pathDataList = getAllPathDataFromVectorDrawable(activity, drawableId);
//        Bitmap resultBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
//
//        Canvas canvas = new Canvas(resultBitmap);
//
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//
//        Matrix matrix = new Matrix();
//
//        for (int i = 0; i < pathDataList.size(); i++) {
//            Path path = PathParser.createPathFromPathData(pathDataList.get(i));
//            Bitmap jpgBitmap = imageList.get(i % imageList.size()); // Use a bitmap from the list
//
//            BitmapShader shader = new BitmapShader(jpgBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//
//            RectF pathBounds = new RectF();
//            path.computeBounds(pathBounds, true);
//
//            // Calculate the scaling factor
////            Log.d("sjdfkljwueqw3", "maskImages: " + canvasWidth + "   " + canvasHeight + "    " + bitmap.getWidth() + "    " + bitmap.getHeight());
//            float scaleX = canvasWidth / 400;
//            float scaleY = canvasHeight / 369;
//
//            // Apply the scale factor to the path
//            matrix.reset();
//            matrix.setScale(scaleX, scaleY);
//            path.transform(matrix);
//
//            // Update the path bounds with the scaled path
//            path.computeBounds(pathBounds, true);
//
//            // Apply shader with the scaled path
//            shader.setLocalMatrix(matrix);
//            paint.setShader(shader);
//
//            // Translate the path to ensure it fits within the canvas
//            matrix.reset();
//            matrix.postTranslate(-pathBounds.left, -pathBounds.top);
//            path.transform(matrix);
//
//            paint.setShader(shader);
//
//            // Draw the path
//            canvas.drawPath(path, paint);
//        }
//
//        // The resultBitmap now contains the combined image
//        return resultBitmap;
//    }


    public static Bitmap maskImages(Activity activity, List<Bitmap> imageList, int drawableId, int canvasWidth, int canvasHeight) {
        List<String> pathDataList = getAllPathDataFromVectorDrawable(activity, drawableId);
        Bitmap resultBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(resultBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Matrix matrix = new Matrix();

        for (int i = 0; i < pathDataList.size(); i++) {
            Path path = PathParser.createPathFromPathData(pathDataList.get(i));
            Bitmap jpgBitmap = imageList.get(i % imageList.size()); // Use a bitmap from the list

            BitmapShader shader = new BitmapShader(jpgBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            RectF pathBounds = new RectF();
//            float scaleX = canvasWidth / 400;
//            float scaleY = canvasHeight / 369;
//
//            // Apply the scale factor to the path
//            matrix.reset();
//            matrix.setScale(scaleX, scaleX);
//            path.transform(matrix);
//
//            // Update the path bounds with the scaled path
            path.computeBounds(pathBounds, true);

            matrix.reset();
            matrix.setRectToRect(new RectF(0, 0, jpgBitmap.getWidth(), jpgBitmap.getHeight()), pathBounds, Matrix.ScaleToFit.CENTER);
            shader.setLocalMatrix(matrix);

            // Calculate the scaling factor
            Log.d("sjdfkljwueqw3", "maskImages: " + canvasWidth + "   " + canvasHeight);


            paint.setShader(shader);
            canvas.drawPath(path, paint);
        }

        // The resultBitmap now contains the combined image
        return resultBitmap;
    }

    public static ArrayList<MaterialModel> fetchAllMaterialTheme() {

        ArrayList<MaterialModel> materialModelList = new ArrayList<>();
        materialModelList.add(new MaterialModel(R.drawable.collage_material_1, 2));
        materialModelList.add(new MaterialModel(R.drawable.collage_material_2, 2));
//        materialModelList.add(new MaterialModel(R.drawable.collage_material_3, 2));
        materialModelList.add(new MaterialModel(R.drawable.collage_material_4, 2));
        materialModelList.add(new MaterialModel(R.drawable.collage_material_5, 4));

        return materialModelList;
    }

}
