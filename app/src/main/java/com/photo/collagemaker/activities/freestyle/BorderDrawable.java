package com.photo.collagemaker.activities.freestyle;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class BorderDrawable extends Drawable {
    private Drawable drawable;
    private Paint borderPaint;
    private int borderWidth;

    public BorderDrawable(Drawable drawable, int borderWidth, int borderColor) {
        this.drawable = drawable;
        this.borderWidth = borderWidth;

        borderPaint = new Paint();
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
    }

    @Override
    public void draw(Canvas canvas) {
        // Calculate the border's bounds
        Rect bounds = getBounds();
        int left = bounds.left + borderWidth / 2;
        int top = bounds.top + borderWidth / 2;
        int right = bounds.right - borderWidth / 2;
        int bottom = bounds.bottom - borderWidth / 2;

        // Draw the border
        canvas.drawRect(left, top, right, bottom, borderPaint);

        // Draw the wrapped drawable
        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);
    }

    @Override
    public void setAlpha(int alpha) {
        drawable.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        drawable.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return drawable.getOpacity();
    }
}