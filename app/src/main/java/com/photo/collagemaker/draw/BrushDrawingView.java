package com.photo.collagemaker.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.photo.collagemaker.assets.BrushColorAsset;
import com.photo.collagemaker.listener.BrushColorChangeListener;
import com.photo.collagemaker.utils.SystemUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class BrushDrawingView extends View {
    private static final float TOUCH_TOLERANCE = 4.0f;
    private Paint bitmapPaint;
    private int brushBitmapSize;
    private List<Point> currentBitmapPoint;
    private DrawModel currentMagicBrush;
    private int distance;
    private int drawMode;
    private Stack<List<Point>> lstPoints;
    private boolean mBrushDrawMode;
    private float mBrushEraserSize;
    private float mBrushSize;
    private BrushColorChangeListener mBrushViewChangeListener;
    private Canvas mDrawCanvas;
    private Paint mDrawPaint;
    private Paint mDrawPaintBlur;
    private float mLastTouchX;
    private float mLastTouchY;
    private int mOpacity;
    private Path mPath;
    private Stack<Point> mPoints;
    private Stack<List<Point>> mRedoPaths;
    private float mTouchX;
    private float mTouchY;
    private Rect tempRect;

    public BrushDrawingView(Context context) {
        this(context, (AttributeSet) null);
    }

    public BrushDrawingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mBrushSize = 25.0f;
        this.mBrushEraserSize = 50.0f;
        this.mOpacity = 255;
        this.mPoints = new Stack<>();
        this.lstPoints = new Stack<>();
        this.mRedoPaths = new Stack<>();
        this.brushBitmapSize = SystemUtil.dpToPx(getContext(), 25);
        this.distance = SystemUtil.dpToPx(getContext(), 3);
        this.currentBitmapPoint = new ArrayList();
        this.tempRect = new Rect();
        setupBrushDrawing();
    }

    public BrushDrawingView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBrushSize = 25.0f;
        this.mBrushEraserSize = 50.0f;
        this.mOpacity = 255;
        this.mPoints = new Stack<>();
        this.lstPoints = new Stack<>();
        this.mRedoPaths = new Stack<>();
        this.brushBitmapSize = SystemUtil.dpToPx(getContext(), 25);
        this.distance = SystemUtil.dpToPx(getContext(), 3);
        this.currentBitmapPoint = new ArrayList();
        this.tempRect = new Rect();
        setupBrushDrawing();
    }

    public void setCurrentMagicBrush(DrawModel drawBitmapModel) {
        currentMagicBrush = drawBitmapModel;
    }

    public void setDrawMode(int i) {
        drawMode = i;
        if (drawMode == 2) {
            mDrawPaint.setColor(-1);
            mDrawPaintBlur.setColor(Color.parseColor(BrushColorAsset.listColorBrush().get(0)));
            refreshBrushDrawing();
            return;
        }
        mDrawPaint.setColor(Color.parseColor(BrushColorAsset.listColorBrush().get(0)));
        refreshBrushDrawing();
    }

    private void setupBrushDrawing() {
        setLayerType(LAYER_TYPE_HARDWARE, null);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mDrawPaint = new Paint();
        mPath = new Path();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setDither(true);
        mDrawPaint.setColor(Color.parseColor(BrushColorAsset.listColorBrush().get(0)));
        mDrawPaint.setStyle(Paint.Style.FILL);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawPaint.setStrokeWidth(mBrushSize);
        mDrawPaint.setAlpha(mOpacity);
        mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        mDrawPaintBlur = new Paint();
        mDrawPaintBlur.setAntiAlias(true);
        mDrawPaintBlur.setDither(true);
        mDrawPaintBlur.setStyle(Paint.Style.STROKE);
        mDrawPaintBlur.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaintBlur.setMaskFilter(new BlurMaskFilter(25.0f, BlurMaskFilter.Blur.OUTER));
        mDrawPaintBlur.setStrokeCap(Paint.Cap.ROUND);
        mDrawPaintBlur.setStrokeWidth(mBrushSize * 1.1f);
        mDrawPaintBlur.setColor(Color.parseColor(BrushColorAsset.listColorBrush().get(0)));
        mDrawPaintBlur.setAlpha(mOpacity);
        mDrawPaintBlur.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        bitmapPaint = new Paint();
        bitmapPaint.setStyle(Paint.Style.FILL);
        bitmapPaint.setStrokeJoin(Paint.Join.ROUND);
        bitmapPaint.setStrokeCap(Paint.Cap.ROUND);
        bitmapPaint.setStrokeWidth(mBrushSize);
        bitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        setVisibility(View.GONE);
    }

    private void refreshBrushDrawing() {
        mBrushDrawMode = true;
        mPath = new Path();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setDither(true);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawPaint.setStrokeWidth(mBrushSize);
        mDrawPaint.setAlpha(mOpacity);
        mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        mDrawPaintBlur.setAntiAlias(true);
        mDrawPaintBlur.setDither(true);
        mDrawPaintBlur.setStyle(Paint.Style.STROKE);
        mDrawPaintBlur.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaintBlur.setMaskFilter(new BlurMaskFilter(30.0f, BlurMaskFilter.Blur.OUTER));
        mDrawPaintBlur.setStrokeCap(Paint.Cap.ROUND);
        mDrawPaintBlur.setStrokeWidth(mBrushSize * 1.1f);
        mDrawPaintBlur.setAlpha(mOpacity);
        mDrawPaintBlur.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        bitmapPaint.setStyle(Paint.Style.FILL);
        bitmapPaint.setStrokeJoin(Paint.Join.ROUND);
        bitmapPaint.setStrokeCap(Paint.Cap.ROUND);
        bitmapPaint.setStrokeWidth(mBrushSize);
        bitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
    }


    public void brushEraser() {
        mBrushDrawMode = true;
        drawMode = 4;
        mDrawPaint.setStrokeWidth(mBrushEraserSize);
        mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }


    public void setBrushDrawingMode(boolean z) {
        mBrushDrawMode = z;
        if (z) {
            setVisibility(View.VISIBLE);
            refreshBrushDrawing();
        }
    }


    public void setOpacity(@IntRange(from = 0, to = 255) int i) {
        mOpacity = i;
        setBrushDrawingMode(true);
    }


    public boolean getBrushDrawingMode() {
        return mBrushDrawMode;
    }


    public void setBrushSize(float f) {
        if (drawMode == 3) {
            brushBitmapSize = SystemUtil.dpToPx(getContext(), (int) f);
            return;
        }
        mBrushSize = f;
        setBrushDrawingMode(true);
    }


    public void setBrushColor(@ColorInt int i) {
        if (drawMode == 1) {
            mDrawPaint.setColor(i);
        } else if (drawMode == 2) {
            mDrawPaintBlur.setColor(i);
        }
        setBrushDrawingMode(true);
    }


    public void setBrushEraserSize(float f) {
        mBrushEraserSize = f;
        setBrushDrawingMode(true);
    }


    public void setBrushEraserColor(@ColorInt int i) {
        mDrawPaint.setColor(i);
        setBrushDrawingMode(true);
    }


    public float getEraserSize() {
        return mBrushEraserSize;
    }


    public float getBrushSize() {
        return mBrushSize;
    }


    public int getBrushColor() {
        return mDrawPaint.getColor();
    }


    public void clearAll() {
        mRedoPaths.clear();
        mPoints.clear();
        lstPoints.clear();

        if (mDrawCanvas != null) {
            mDrawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
        invalidate();
    }


    public void setBrushViewChangeListener(BrushColorChangeListener brushViewChangeListener) {
        mBrushViewChangeListener = brushViewChangeListener;
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i > 0 && i2 > 0) {
            mDrawCanvas = new Canvas(Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888));
        }
    }

    public void onDraw(Canvas canvas) {
        Iterator it = mPoints.iterator();
        while (it.hasNext()) {
            Point point = (Point) it.next();
            if (point.vector2 != null) {
                tempRect.set(point.vector2.x, point.vector2.y, point.vector2.x1, point.vector2.y1);
                canvas.drawBitmap(point.vector2.bitmap, null, tempRect, bitmapPaint);
            } else if (point.linePath != null) {
                canvas.drawPath(point.linePath.getDrawPath(), point.linePath.getDrawPaint());
            }
        }
        if (drawMode == 2) {
            canvas.drawPath(mPath, mDrawPaintBlur);
        }
        canvas.drawPath(mPath, mDrawPaint);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        if (!mBrushDrawMode) {
            return false;
        }
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        switch (motionEvent.getAction()) {
            case 0:
                touchStart((float) x, (float) y);
                break;
            case 1:
                touchUp();
                break;
            case 2:
                touchMove(x, y);
                break;
        }
        invalidate();
        return true;
    }

    public static class LinePath {
        private Paint mDrawPaint;
        private Path mDrawPath;

        public LinePath(Path path, Paint paint) {
            mDrawPaint = new Paint(paint);
            mDrawPath = new Path(path);
        }

        public Paint getDrawPaint() {
            return mDrawPaint;
        }

        public Path getDrawPath() {
            return mDrawPath;
        }
    }


    public boolean undo() {
        if (!lstPoints.empty()) {
            List pop = lstPoints.pop();
            mRedoPaths.push(pop);
            mPoints.removeAll(pop);
            invalidate();
        }
        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener.onViewRemoved(this);
        }
        return !lstPoints.empty();
    }


    public boolean redo() {
        if (!mRedoPaths.empty()) {
            List<Point> pop = mRedoPaths.pop();
            for (Point push : pop) {
                mPoints.push(push);
            }
            lstPoints.push(pop);
            invalidate();
        }
        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener.onViewAdd(this);
        }
        return !mRedoPaths.empty();
    }

    private void touchStart(float f, float f2) {
        mRedoPaths.clear();
        mPath.reset();
        mPath.moveTo(f, f2);
        mTouchX = f;
        mTouchY = f2;
        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener.onStartDrawing();
        }
        if (drawMode == 3) {
            currentBitmapPoint.clear();
        }
    }

    private void touchMove(int i, int i2) {
        int nextInt;
        float f = (float) i;
        float abs = Math.abs(f - mTouchX);
        float f2 = (float) i2;
        float abs2 = Math.abs(f2 - mTouchY);
        if (abs < TOUCH_TOLERANCE && abs2 < TOUCH_TOLERANCE) {
            return;
        }
        if (drawMode != 3) {
            mPath.quadTo(mTouchX, mTouchY, (mTouchX + f) / 2.0f, (mTouchY + f2) / 2.0f);
            mTouchX = f;
            mTouchY = f2;
        } else if (Math.abs(f - mLastTouchX) > ((float) (brushBitmapSize + distance)) || Math.abs(f2 - mLastTouchY) > ((float) (brushBitmapSize + distance))) {
            Random random = new Random();
            int i3 = -1;
            List<Vector2> list = currentMagicBrush.getmPositions();
            if (list.size() > 0) {
                i3 = list.get(list.size() - 1).drawableIndex;
            }
            do {
                nextInt = random.nextInt(currentMagicBrush.getLstIconWhenDrawing().size());
            } while (nextInt == i3);
            Vector2 vector2 = new Vector2(i, i2, i + brushBitmapSize, i2 + brushBitmapSize, nextInt, currentMagicBrush.getBitmapByIndex(nextInt));
            list.add(vector2);
            Point point = new Point(vector2);
            mPoints.push(point);
            currentBitmapPoint.add(point);
            mLastTouchX = f;
            mLastTouchY = f2;
        }
    }

    private void touchUp() {
        if (drawMode != 3) {
            ArrayList arrayList = new ArrayList();
            Point point = new Point(new LinePath(mPath, mDrawPaint));
            mPoints.push(point);
            arrayList.add(point);
            if (drawMode == 2) {
                Point point2 = new Point(new LinePath(mPath, mDrawPaintBlur));
                mPoints.push(point2);
                arrayList.add(point2);
            }
            lstPoints.push(arrayList);
        } else {
            lstPoints.push(new ArrayList(currentBitmapPoint));
            currentBitmapPoint.clear();
        }
        mPath = new Path();
        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener.onStopDrawing();
            mBrushViewChangeListener.onViewAdd(this);
        }
        mLastTouchX = 0.0f;
        mLastTouchY = 0.0f;
    }

    public static final class Vector2 {
        public Bitmap bitmap;
        int drawableIndex;

        public int x;

        int x1;

        public int y;

        int y1;

        Vector2(int i, int i2, int i3, int i4, int i5, Bitmap bitmap2) {
            x = i;
            y = i2;
            x1 = i3;
            y1 = i4;
            bitmap = bitmap2;
            drawableIndex = i5;
        }
    }

    class Point {
        LinePath linePath;
        Vector2 vector2;

        Point(LinePath linePath2) {
            linePath = linePath2;
        }

        Point(Vector2 vector22) {
            vector2 = vector22;
        }
    }

    public Bitmap getDrawBitmap(Bitmap bitmap) {
        int width = getWidth();
        int height = getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap, null, new RectF(0.0f, 0.0f, (float) width, (float) height), (Paint) null);
        Iterator it = mPoints.iterator();
        while (it.hasNext()) {
            Point point = (Point) it.next();
            if (point.vector2 != null) {
                tempRect.set(point.vector2.x, point.vector2.y, point.vector2.x1, point.vector2.y1);
                canvas.drawBitmap(point.vector2.bitmap, null, tempRect, bitmapPaint);
            } else if (point.linePath != null) {
                canvas.drawPath(point.linePath.getDrawPath(), point.linePath.getDrawPaint());
            }
        }
        return createBitmap;
    }
}
