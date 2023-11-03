package com.photo.collagemaker.activities.material;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.photo.collagemaker.R;

public class HeartMaskView extends View {
    private Paint paint;
    private Bitmap image1, image2;
    private Path path1, path2;

    public HeartMaskView(Context context) {
        super(context);
        init();
    }

    public HeartMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeartMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

        image1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        image2 = BitmapFactory.decodeResource(getResources(), R.drawable.background_blur);

        path1 = new Path();
        path2 = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Calculate the position and size for the heart shapes
        float centerX1 = getWidth() / 4;
        float centerX2 = 3 * getWidth() / 4;
        float centerY = getHeight() / 2;
        float heartSize = Math.min(getWidth(), getHeight()) / 4;

        // Define the paths for the heart shapes
        path1.reset();
        path1.moveTo(centerX1, centerY);
        path1.cubicTo(centerX1, centerY - heartSize,
                centerX1 - 2 * heartSize, centerY - heartSize,
                centerX1, centerY + heartSize);
        path1.cubicTo(centerX1 + 2 * heartSize, centerY - heartSize,
                centerX1, centerY - heartSize,
                centerX1, centerY);

        path2.reset();
        path2.moveTo(centerX2, centerY);
        path2.cubicTo(centerX2, centerY - heartSize,
                centerX2 - 2 * heartSize, centerY - heartSize,
                centerX2, centerY + heartSize);
        path2.cubicTo(centerX2 + 2 * heartSize, centerY - heartSize,
                centerX2, centerY - heartSize,
                centerX2, centerY);

        // Use PorterDuffXfermode to apply the clipping mask
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawPath(path1, paint);
        canvas.drawPath(path2, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(image1, 0, 0, paint);
        canvas.drawBitmap(image2, getWidth() / 2, 0, paint);
    }
}