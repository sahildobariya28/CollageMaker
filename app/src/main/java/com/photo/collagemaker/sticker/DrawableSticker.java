package com.photo.collagemaker.sticker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public class DrawableSticker extends Sticker {
    private Drawable drawable;

    private Rect realBounds;

    public DrawableSticker(Drawable paramDrawable) {

        this.drawable = getRoundedDrawableWithStroke(paramDrawable);
        this.realBounds = new Rect(0, 0, getWidth(), getHeight());
    }

    public Drawable getRoundedDrawableWithStroke(Drawable originalDrawable) {
        if (originalDrawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) originalDrawable;

            Bitmap bitmap = bitmapDrawable.getBitmap();
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            // Draw the rounded corners
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(getExtraBorderColor()); // Replace with your desired background color
            canvas.drawRoundRect(rectF, getCornerRadius() * 2, getCornerRadius() * 2, paint);

            // Apply the original bitmap inside the rounded rectangle
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            // Draw the stroke
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(getExtraBorderColor());
            paint.setStrokeWidth(getExtraBorderWidth() * 2);
            canvas.drawRoundRect(rectF, getCornerRadius() * 2, getCornerRadius() * 2, paint);
            return new BitmapDrawable(Resources.getSystem(), output);
        }

        // If it's not a BitmapDrawable, return the original drawable
        return originalDrawable;
    }


    public void draw(@NonNull Canvas paramCanvas) {
        Drawable tempDrawable = getRoundedDrawableWithStroke(drawable);

        paramCanvas.save();
        paramCanvas.concat(getMatrix());
        tempDrawable.setBounds(this.realBounds);
        tempDrawable.draw(paramCanvas);
        paramCanvas.restore();

    }




    public int getAlpha() {
        return this.drawable.getAlpha();
    }

    @NonNull
    public Drawable getDrawable() {
        return this.drawable;
    }

    public int getHeight() {
        return this.drawable.getIntrinsicHeight();
    }

    public int getWidth() {
        return this.drawable.getIntrinsicWidth();
    }

    public void release() {
        super.release();
        if (this.drawable != null)
            this.drawable = null;
    }

    @NonNull
    public DrawableSticker setAlpha(@IntRange(from = 0L, to = 255L) int paramInt) {
        this.drawable.setAlpha(paramInt);
        return this;
    }

    public DrawableSticker setDrawable(@NonNull Drawable paramDrawable) {
        this.drawable = paramDrawable;
        return this;
    }
}
