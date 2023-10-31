package com.photo.collagemaker.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;

import com.photo.collagemaker.R;
import com.photo.collagemaker.event.DeleteIconEvent;
import com.photo.collagemaker.event.FlipHorizontallyEvent;
import com.photo.collagemaker.event.ZoomIconEvent;
import com.photo.collagemaker.sticker.DrawableSticker;
import com.photo.collagemaker.sticker.Sticker;
import com.photo.collagemaker.utils.StickerUtils;
import com.photo.collagemaker.utils.SystemUtil;
import com.steelkiwi.cropiwa.AspectRatio;

import org.wysaid.view.ImageGLSurfaceView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StickerView extends RelativeLayout {
    private static final String TAG = "QuShotStickerView";
    private final float[] bitmapPoints;
    private final Paint borderPaint;
    private final Paint borderPaintRed;
    private final float[] bounds;
    private boolean bringToFrontCurrentSticker;
    private int circleRadius;
    private boolean constrained;
    private final PointF currentCenterPoint;
    private StickerIcons currentIcon;
    private int currentMode;
    private float currentMoveingX;
    private float currentMoveingY;
    private final Matrix downMatrix;
    private float downX;
    private float downY;
    private boolean drawCirclePoint;
    private Sticker handlingSticker;
    private final List<StickerIcons> icons;
    private long lastClickTime;
    private Sticker lastHandlingSticker;
    private final Paint linePaint;
    private boolean locked;
    private PointF midPoint;
    private int minClickDelayTime;
    private final Matrix moveMatrix;
    private float oldDistance;
    private float oldRotation;
    private boolean onMoving;
    private OnStickerOperationListener onStickerOperationListener;
    private Paint paintCircle;
    private final float[] point;
    private boolean showBorder;
    private boolean showIcons;
    private final Matrix sizeMatrix;
    private final RectF stickerRect;
    private final List<Sticker> stickers;
    private final float[] tmp;
    private int touchSlop;
    private boolean isframe;
    private int frameSize;
    private int frameColor;
    private int selectedStickerPosition = 0;

    public interface OnStickerOperationListener {
        void onAddSticker(Sticker sticker);

        void onStickerSelected(Sticker sticker, int selectedStickerPosition);

        void onStickerDeleted(Sticker sticker);

        void onStickerDoubleTap(Sticker sticker);

        void onStickerDrag(Sticker sticker);

        void onStickerFlip(Sticker sticker);

        void onStickerTouchOutside();

        void onStickerTouchedDown(Sticker sticker);

        void onStickerZoom(Sticker sticker);

        void onTouchDownBeauty(float f, float f2);

        void onTouchDragBeauty(float f, float f2);

        void onTouchUpBeauty(float f, float f2);
    }

    public StickerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public StickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @SuppressLint("ResourceType")
    public StickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray typedArray;
        stickers = new ArrayList();
        icons = new ArrayList(4);
        borderPaint = new Paint();
        borderPaintRed = new Paint();
        linePaint = new Paint();
        stickerRect = new RectF();
        sizeMatrix = new Matrix();
        downMatrix = new Matrix();
        moveMatrix = new Matrix();
        bitmapPoints = new float[8];
        bounds = new float[8];
        point = new float[2];
        currentCenterPoint = new PointF();
        tmp = new float[2];
        midPoint = new PointF();
        drawCirclePoint = false;
        onMoving = false;
        oldDistance = 0.0f;
        oldRotation = 0.0f;
        currentMode = 0;
        lastClickTime = 0;
        minClickDelayTime = 200;
        paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setDither(true);
        paintCircle.setColor(getContext().getResources().getColor(R.color.theme_color));
        paintCircle.setStrokeWidth((float) SystemUtil.dpToPx(getContext(), 2));
        paintCircle.setStyle(Paint.Style.STROKE);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        try {
            int[] StickerView = {R.attr.borderAlpha, R.attr.borderColor, R.attr.bringToFrontCurrentSticker, R.attr.showBorder, R.attr.showIcons};
            typedArray = context.obtainStyledAttributes(attributeSet, StickerView);
            try {
                showIcons = typedArray.getBoolean(4, false);
                showBorder = typedArray.getBoolean(3, false);
                bringToFrontCurrentSticker = typedArray.getBoolean(2, false);
                borderPaint.setAntiAlias(true);
                borderPaint.setColor(typedArray.getColor(1, context.getColor(R.color.black)));
                borderPaint.setAlpha(typedArray.getInteger(0, 255));
                borderPaintRed.setAntiAlias(true);
                borderPaintRed.setColor(typedArray.getColor(1, context.getColor(R.color.black)));
                borderPaintRed.setAlpha(typedArray.getInteger(0, 255));
                getStickerIcons();
                if (typedArray != null) {
                    typedArray.recycle();
                }
            } catch (Throwable th) {
                th = th;
                if (typedArray != null) {
                    typedArray.recycle();
                }
                throw th;
            }
        } catch (Throwable th2) {

            typedArray = null;
            if (typedArray != null) {
            }

        }
    }

    @RequiresApi(api = 21)
    public StickerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        stickers = new ArrayList();
        icons = new ArrayList(4);
        borderPaint = new Paint();
        borderPaintRed = new Paint();
        linePaint = new Paint();
        stickerRect = new RectF();
        sizeMatrix = new Matrix();
        downMatrix = new Matrix();
        moveMatrix = new Matrix();
        bitmapPoints = new float[8];
        bounds = new float[8];
        point = new float[2];
        currentCenterPoint = new PointF();
        tmp = new float[2];
        midPoint = new PointF();
        drawCirclePoint = false;
        onMoving = false;
        oldDistance = 0.0f;
        oldRotation = 0.0f;
        currentMode = 0;
        lastClickTime = 0;
        minClickDelayTime = 200;
    }

    public Matrix getSizeMatrix() {
        return sizeMatrix;
    }

    public Matrix getDownMatrix() {
        return downMatrix;
    }

    public Matrix getMoveMatrix() {
        return moveMatrix;
    }

    public List<Sticker> getStickers() {
        return stickers;
    }

    public void getStickerIcons() {
        StickerIcons quShotStickerIconClose = new StickerIcons(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_close), 0, StickerIcons.DELETE);
        quShotStickerIconClose.setIconEvent(new DeleteIconEvent());
        StickerIcons quShotStickerIconScale = new StickerIcons(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_scale), 3, StickerIcons.SCALE);
        quShotStickerIconScale.setIconEvent(new ZoomIconEvent());
        StickerIcons quShotStickerIconFlip = new StickerIcons(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_flip), 1, StickerIcons.FLIP);
        quShotStickerIconFlip.setIconEvent(new FlipHorizontallyEvent());
        StickerIcons quShotStickerIconEdit = new StickerIcons(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_edit), 2, StickerIcons.EDIT);
        quShotStickerIconEdit.setIconEvent(new FlipHorizontallyEvent());
        icons.clear();
        icons.add(quShotStickerIconClose);
        icons.add(quShotStickerIconScale);
        icons.add(quShotStickerIconFlip);
        icons.add(quShotStickerIconEdit);
    }

    public void setHandlingSticker(Sticker sticker) {
        lastHandlingSticker = handlingSticker;
        handlingSticker = sticker;
        invalidate();
    }

    public void showLastHandlingSticker() {
        if (lastHandlingSticker != null && !lastHandlingSticker.isShow()) {
            lastHandlingSticker.setShow(true);
            invalidate();
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            stickerRect.left = (float) i;
            stickerRect.top = (float) i2;
            stickerRect.right = (float) i3;
            stickerRect.bottom = (float) i4;
        }
    }


    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (drawCirclePoint && onMoving) {
            canvas.drawCircle(downX, downY, (float) circleRadius, paintCircle);
            canvas.drawLine(downX, downY, currentMoveingX, currentMoveingY, paintCircle);
        }
        drawStickers(canvas);
    }

    public void drawStickers(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float f4;
        Canvas canvas2 = canvas;

        for (int i = 0; i < stickers.size(); i++) {
            Sticker sticker = stickers.get(i);
            if (sticker != null && sticker.isShow()) {
                sticker.draw(canvas2);
            }
        }

        if (handlingSticker != null && !locked && (showBorder || showIcons)) {
            getStickerPoints(handlingSticker, bitmapPoints);

            float f5 = bitmapPoints[0];
            float f6 = bitmapPoints[1];
            float f7 = bitmapPoints[2];
            float f8 = bitmapPoints[3];
            float f9 = bitmapPoints[4];
            float f10 = bitmapPoints[5];
            float f11 = bitmapPoints[6];
            float f12 = bitmapPoints[7];

            if (showBorder) {
                canvas.drawLine(f5, f6, f7, f8, borderPaint);
                canvas.drawLine(f5, f6, f9, f10, borderPaint);
                canvas.drawLine(f7, f8, f11, f12, borderPaint);
                canvas.drawLine(f11, f12, f9, f10, borderPaint);
            }

            if (showIcons) {
                float f15 = f12;
                float f16 = f11;
                float f17 = f10;
                float f18 = f9;
                float calculateRotation = calculateRotation(f16, f15, f18, f17);

                for (StickerIcons bitmapStickerIcon : icons) {
                    switch (bitmapStickerIcon.getPosition()) {
                        case 0:
                            configIconMatrix(bitmapStickerIcon, f5, f6, calculateRotation);
                            bitmapStickerIcon.draw(canvas2, borderPaintRed);
                            break;
                        case 1:
                            if ((handlingSticker instanceof CustomTextView && bitmapStickerIcon.getTag().equals(StickerIcons.EDIT)) ||
                                    (handlingSticker instanceof DrawableSticker && bitmapStickerIcon.getTag().equals(StickerIcons.FLIP))) {
                                configIconMatrix(bitmapStickerIcon, f7, f8, calculateRotation);
                                bitmapStickerIcon.draw(canvas2, borderPaint);
                            }
                            break;
                        case 2:
                            if (handlingSticker instanceof CustomSticker) {
                                if (((CustomSticker) handlingSticker).getType() == 0) {
                                    configIconMatrix(bitmapStickerIcon, f18, f17, calculateRotation);
                                    bitmapStickerIcon.draw(canvas2, borderPaint);
                                }
                            } else {
                                configIconMatrix(bitmapStickerIcon, f18, f17, calculateRotation);
                                bitmapStickerIcon.draw(canvas2, borderPaint);
                            }
                            break;
                        case 3:
                            if (!(handlingSticker instanceof CustomTextView) || !bitmapStickerIcon.getTag().equals(StickerIcons.ROTATE)) {
                                if (!(handlingSticker instanceof DrawableSticker) || !bitmapStickerIcon.getTag().equals(StickerIcons.SCALE)) {
                                    if (handlingSticker instanceof CustomSticker) {
                                        CustomSticker beautySticker = (CustomSticker) handlingSticker;
                                        if (beautySticker.getType() != 0) {
                                            configIconMatrix(bitmapStickerIcon, f16, f15, calculateRotation);
                                            bitmapStickerIcon.draw(canvas2, borderPaint);
                                        }
                                    } else {
                                        configIconMatrix(bitmapStickerIcon, f16, f15, calculateRotation);
                                        bitmapStickerIcon.draw(canvas2, borderPaint);
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        }
        invalidate();
    }


    public void configIconMatrix(@NonNull StickerIcons bitmapStickerIcon, float f, float f2, float f3) {
        bitmapStickerIcon.setX(f);
        bitmapStickerIcon.setY(f2);
        bitmapStickerIcon.getMatrix().reset();
        bitmapStickerIcon.getMatrix().postRotate(f3, (float) (bitmapStickerIcon.getWidth() / 2), (float) (bitmapStickerIcon.getHeight() / 2));
        bitmapStickerIcon.getMatrix().postTranslate(f - ((float) (bitmapStickerIcon.getWidth() / 2)), f2 - ((float) (bitmapStickerIcon.getHeight() / 2)));
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (locked) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        if (motionEvent.getAction() != 0) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        downX = motionEvent.getX();
        downY = motionEvent.getY();
        return (findCurrentIconTouched() == null && findHandlingSticker() == null) ? false : true;
    }

    public void setDrawCirclePoint(boolean z) {
        drawCirclePoint = z;
        onMoving = false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (locked) {
            return super.onTouchEvent(motionEvent);
        }
        switch (MotionEventCompat.getActionMasked(motionEvent)) {
            case 0:
                if (!onTouchDown(motionEvent)) {
                    if (onStickerOperationListener == null) {
                        return false;
                    }
                    onStickerOperationListener.onStickerTouchOutside();
                    invalidate();
                    if (!drawCirclePoint) {
                        return false;
                    }
                }
                break;
            case 1:
                onTouchUp(motionEvent);
                break;
            case 2:
                handleCurrentMode(motionEvent);
                invalidate();
                break;
            case 5:
                oldDistance = calculateDistance(motionEvent);
                oldRotation = calculateRotation(motionEvent);
                midPoint = calculateMidPoint(motionEvent);
                if (handlingSticker != null && isInStickerArea(handlingSticker, motionEvent.getX(1), motionEvent.getY(1)) && findCurrentIconTouched() == null) {
                    currentMode = 2;
                    break;
                }
            case 6:
                if (!(currentMode != 2 || handlingSticker == null || onStickerOperationListener == null)) {
                    onStickerOperationListener.onStickerZoom(handlingSticker);
                }
                currentMode = 0;
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);

        // Measure the children (stickers, icons, etc.) here if needed.
    }

    public boolean onTouchDown(@NonNull MotionEvent motionEvent) {
        currentMode = 1;
        downX = motionEvent.getX();
        downY = motionEvent.getY();
        onMoving = true;
        currentMoveingX = motionEvent.getX();
        currentMoveingY = motionEvent.getY();
        midPoint = calculateMidPoint();
        oldDistance = calculateDistance(midPoint.x, midPoint.y, downX, downY);
        oldRotation = calculateRotation(midPoint.x, midPoint.y, downX, downY);
        currentIcon = findCurrentIconTouched();
        if (currentIcon != null) {
            currentMode = 3;
            currentIcon.onActionDown(this, motionEvent);
        } else {
            handlingSticker = findHandlingSticker();
        }
        if (handlingSticker != null) {
            downMatrix.set(handlingSticker.getMatrix());
            if (bringToFrontCurrentSticker) {
                stickers.remove(handlingSticker);
                stickers.add(handlingSticker);
            }
            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerTouchedDown(handlingSticker);
            }
        }
        if (drawCirclePoint) {
            onStickerOperationListener.onTouchDownBeauty(currentMoveingX, currentMoveingY);
            invalidate();
            return true;
        } else if (currentIcon == null && handlingSticker == null) {
            return false;
        } else {
            invalidate();
            return true;
        }
    }


    public void onTouchUp(@NonNull MotionEvent motionEvent) {
        long uptimeMillis = SystemClock.uptimeMillis();
        onMoving = false;
        if (drawCirclePoint) {
            onStickerOperationListener.onTouchUpBeauty(motionEvent.getX(), motionEvent.getY());
        }
        if (!(currentMode != 3 || currentIcon == null || handlingSticker == null)) {
            currentIcon.onActionUp(this, motionEvent);
        }
        if (currentMode == 1 && Math.abs(motionEvent.getX() - downX) < ((float) touchSlop) && Math.abs(motionEvent.getY() - downY) < ((float) touchSlop) && handlingSticker != null) {
            currentMode = 4;
            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerSelected(handlingSticker, selectedStickerPosition);
            }
            if (uptimeMillis - lastClickTime < ((long) minClickDelayTime) && onStickerOperationListener != null) {
                onStickerOperationListener.onStickerDoubleTap(handlingSticker);
            }
        }
        if (!(currentMode != 1 || handlingSticker == null || onStickerOperationListener == null)) {
            onStickerOperationListener.onStickerDrag(handlingSticker);
        }
        currentMode = 0;
        lastClickTime = uptimeMillis;
    }


    public void handleCurrentMode(@NonNull MotionEvent motionEvent) {
        switch (this.currentMode) {
            case 1:
                currentMoveingX = motionEvent.getX();
                currentMoveingY = motionEvent.getY();
                if (drawCirclePoint) {
                    onStickerOperationListener.onTouchDragBeauty(currentMoveingX, currentMoveingY);
                }
                if (handlingSticker != null) {
                    moveMatrix.set(this.downMatrix);
                    if (handlingSticker instanceof CustomSticker) {
                        CustomSticker beautySticker = (CustomSticker) handlingSticker;
                        if (beautySticker.getType() == 10 || beautySticker.getType() == 11) {
                            moveMatrix.postTranslate(0.0f, motionEvent.getY() - downY);
                        } else {
                            moveMatrix.postTranslate(motionEvent.getX() - downX, motionEvent.getY() - downY);
                        }
                    } else {
                        moveMatrix.postTranslate(motionEvent.getX() - downX, motionEvent.getY() - downY);
                    }
                    this.handlingSticker.setMatrix(moveMatrix);
                    if (constrained) {
                        constrainSticker(handlingSticker);
                        return;
                    }
                    return;
                }
                return;
            case 2:
                if (handlingSticker != null) {
                    float calculateDistance = calculateDistance(motionEvent);
                    float calculateRotation = calculateRotation(motionEvent);
                    moveMatrix.set(downMatrix);
                    moveMatrix.postScale(calculateDistance / oldDistance, calculateDistance / oldDistance, midPoint.x, midPoint.y);
                    moveMatrix.postRotate(calculateRotation - oldRotation, midPoint.x, midPoint.y);
                    handlingSticker.setMatrix(moveMatrix);
                    return;
                }
                return;
            case 3:
                if (handlingSticker != null && this.currentIcon != null) {
                    currentIcon.onActionMove(this, motionEvent);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void zoomAndRotateCurrentSticker(@NonNull MotionEvent motionEvent) {
        zoomAndRotateSticker(this.handlingSticker, motionEvent);
    }

    public void alignHorizontally() {
        moveMatrix.set(downMatrix);
        moveMatrix.postRotate(-getCurrentSticker().getCurrentAngle(), midPoint.x, midPoint.y);
        handlingSticker.setMatrix(moveMatrix);
    }

    public void zoomAndRotateSticker(@Nullable Sticker sticker, @NonNull MotionEvent motionEvent) {
        float f;
        if (sticker != null) {
            boolean z = sticker instanceof CustomSticker;
            if (z) {
                CustomSticker beautySticker = (CustomSticker) sticker;
                if (beautySticker.getType() == 10 || beautySticker.getType() == 11) {
                    return;
                }
            }
            if (sticker instanceof CustomTextView) {
                f = oldDistance;
            } else {
                f = calculateDistance(midPoint.x, midPoint.y, motionEvent.getX(), motionEvent.getY());
            }
            float calculateRotation = calculateRotation(midPoint.x, midPoint.y, motionEvent.getX(), motionEvent.getY());
            moveMatrix.set(downMatrix);
            moveMatrix.postScale(f / oldDistance, f / oldDistance, midPoint.x, midPoint.y);
            if (!z) {
                moveMatrix.postRotate(calculateRotation - oldRotation, midPoint.x, midPoint.y);
            }
            handlingSticker.setMatrix(moveMatrix);
        }
    }


    public void constrainSticker(@NonNull Sticker sticker) {
        int width = getWidth();
        int height = getHeight();
        sticker.getMappedCenterPoint(currentCenterPoint, point, tmp);
        float f = 0.0f;
        float f2 = this.currentCenterPoint.x < 0.0f ? -currentCenterPoint.x : 0.0f;
        float f3 = (float) width;
        if (currentCenterPoint.x > f3) {
            f2 = f3 - currentCenterPoint.x;
        }
        if (currentCenterPoint.y < 0.0f) {
            f = -currentCenterPoint.y;
        }
        float f4 = (float) height;
        if (currentCenterPoint.y > f4) {
            f = f4 - currentCenterPoint.y;
        }
        sticker.getMatrix().postTranslate(f2, f);
    }


    @Nullable
    public StickerIcons findCurrentIconTouched() {
        for (StickerIcons next : icons) {
            float x = next.getX() - downX;
            float y = next.getY() - downY;
            if (((double) ((x * x) + (y * y))) <= Math.pow((double) (next.getIconRadius() + next.getIconRadius()), 2.0d)) {
                return next;
            }
        }
        return null;
    }


    @Nullable
    public Sticker findHandlingSticker() {
        for (int size = stickers.size() - 1; size >= 0; size--) {
            if (isInStickerArea(stickers.get(size), downX, downY)) {
                selectedStickerPosition = size;
                return stickers.get(size);
            }
        }
        return null;
    }


    public boolean isInStickerArea(@NonNull Sticker sticker, float f, float f2) {
        tmp[0] = f;
        tmp[1] = f2;
        return sticker.contains(tmp);
    }


    @NonNull
    public PointF calculateMidPoint(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            midPoint.set(0.0f, 0.0f);
            return midPoint;
        }
        midPoint.set((motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f, (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f);
        return midPoint;
    }

    @NonNull
    public PointF calculateMidPoint() {
        if (handlingSticker == null) {
            midPoint.set(0.0f, 0.0f);
            return midPoint;
        }
        handlingSticker.getMappedCenterPoint(midPoint, point, tmp);
        return midPoint;
    }


    public float calculateRotation(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            return 0.0f;
        }
        return calculateRotation(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1));
    }


    public float calculateRotation(float f, float f2, float f3, float f4) {
        return (float) Math.toDegrees(Math.atan2((double) (f2 - f4), (double) (f - f3)));
    }


    public float calculateDistance(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            return 0.0f;
        }
        return calculateDistance(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1));
    }


    public float calculateDistance(float f, float f2, float f3, float f4) {
        double d = (double) (f - f3);
        double d2 = (double) (f2 - f4);
        return (float) Math.sqrt((d * d) + (d2 * d2));
    }


    public void transformSticker(@Nullable Sticker sticker) {
        if (sticker == null) {
            Log.e(TAG, "transformSticker: the bitmapSticker is null or the bitmapSticker bitmap is null");
            return;
        }
        sizeMatrix.reset();
        float width = (float) getWidth();
        float height = (float) getHeight();
        float width2 = (float) sticker.getWidth();
        float height2 = (float) sticker.getHeight();
        sizeMatrix.postTranslate((width - width2) / 2.0f, (height - height2) / 2.0f);
        float f = (width < height ? width / width2 : height / height2) / 2.0f;
        sizeMatrix.postScale(f, f, width / 2.0f, height / 2.0f);
        sticker.getMatrix().reset();
        sticker.setMatrix(this.sizeMatrix);
        invalidate();
    }


    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        for (int i5 = 0; i5 < stickers.size(); i5++) {
            Sticker sticker = stickers.get(i5);
            if (sticker != null) {
                transformSticker(sticker);
            }
        }
    }

    public void flipCurrentSticker(int i) {
        flip(handlingSticker, i);
    }

    public void flip(@Nullable Sticker sticker, int i) {
        if (sticker != null) {
            sticker.getCenterPoint(midPoint);
            if ((i & 1) > 0) {
                sticker.getMatrix().preScale(-1.0f, 1.0f, midPoint.x, midPoint.y);
                sticker.setFlippedHorizontally(!sticker.isFlippedHorizontally());
            }
            if ((i & 2) > 0) {
                sticker.getMatrix().preScale(1.0f, -1.0f, midPoint.x, midPoint.y);
                sticker.setFlippedVertically(!sticker.isFlippedVertically());
            }
            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerFlip(sticker);
            }
            invalidate();
        }
    }

    public boolean replace(@Nullable Sticker sticker) {
        return replace(sticker, true);
    }

    public Sticker getLastHandlingSticker() {
        return lastHandlingSticker;
    }

    public boolean replace(@Nullable Sticker sticker, boolean z) {
        float f;
        if (handlingSticker == null) {
            handlingSticker = lastHandlingSticker;
        }
        if (this.handlingSticker == null || sticker == null) {
            return false;
        }
        float width = (float) getWidth();
        float height = (float) getHeight();
        if (z) {
            sticker.setMatrix(handlingSticker.getMatrix());
            sticker.setFlippedVertically(handlingSticker.isFlippedVertically());
            sticker.setFlippedHorizontally(handlingSticker.isFlippedHorizontally());
        } else {
            handlingSticker.getMatrix().reset();
            sticker.getMatrix().postTranslate((width - ((float) handlingSticker.getWidth())) / 2.0f, (height - ((float) this.handlingSticker.getHeight())) / 2.0f);
            if (width < height) {
                if (handlingSticker instanceof CustomTextView) {
                    f = width / ((float) handlingSticker.getWidth());
                } else {
                    f = width / ((float) handlingSticker.getDrawable().getIntrinsicWidth());
                }
            } else if (handlingSticker instanceof CustomTextView) {
                f = height / ((float) handlingSticker.getHeight());
            } else {
                f = height / ((float) handlingSticker.getDrawable().getIntrinsicHeight());
            }
            float f2 = f / 2.0f;
            sticker.getMatrix().postScale(f2, f2, width / 2.0f, height / 2.0f);
        }
        stickers.set(stickers.indexOf(handlingSticker), sticker);
        handlingSticker = sticker;
        invalidate();
        return true;
    }

    public boolean remove(@Nullable Sticker sticker) {
        if (stickers.contains(sticker)) {
            stickers.remove(sticker);
            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerDeleted(sticker);
            }
            if (handlingSticker == sticker) {
                handlingSticker = null;
            }
            invalidate();
            return true;
        }
        Log.d(TAG, "remove: the sticker is not in this StickerView");
        return false;
    }

    public boolean removeCurrentSticker() {
        return remove(this.handlingSticker);
    }

    public void removeAllStickers() {
        this.stickers.clear();
        if (handlingSticker != null) {
            handlingSticker.release();
            handlingSticker = null;
        }
        invalidate();
    }

    @NonNull
    public StickerView addSticker(@NonNull Sticker sticker) {
        return addSticker(sticker, 1);
    }

    public StickerView addSticker(@NonNull final Sticker sticker, final int i) {
        if (ViewCompat.isLaidOut(this)) {
            addStickerImmediately(sticker, i);
        } else {
            post(new Runnable() {
                public void run() {
                    StickerView.this.addStickerImmediately(sticker, i);
                }
            });
        }
        return this;
    }

    public void setSticker(@NonNull final Sticker sticker, int position) {
        setStickerPosition(sticker, position);

        sticker.getMatrix().postScale(1.0f, 1.0f, (float) getWidth(), (float) getHeight());
        handlingSticker = sticker;
        stickers.set(position, sticker);

        invalidate();
    }


    public void addStickerImmediately(@NonNull Sticker sticker, int i) {
        setStickerPosition(sticker, i);
        sticker.getMatrix().postScale(1.0f, 1.0f, (float) getWidth(), (float) getHeight());
        handlingSticker = sticker;
        stickers.add(sticker);
        if (onStickerOperationListener != null) {
            onStickerOperationListener.onAddSticker(sticker);
        }
        invalidate();
    }


    public void setStickerPosition(@NonNull Sticker sticker, int i) {
        float f;
        float width = ((float) getWidth()) - ((float) sticker.getWidth());
        float height = ((float) getHeight()) - ((float) sticker.getHeight());
        if (sticker instanceof CustomSticker) {
            CustomSticker beautySticker = (CustomSticker) sticker;
            f = height / 2.0f;
            if (beautySticker.getType() == 0) {
                width /= 3.0f;
            } else if (beautySticker.getType() == 1) {
                width = (width * 2.0f) / 3.0f;
            } else if (beautySticker.getType() == 2) {
                width /= 2.0f;
            } else if (beautySticker.getType() == 4) {
                width /= 2.0f;
            } else if (beautySticker.getType() == 10) {
                width /= 2.0f;
                f = (f * 2.0f) / 3.0f;
            } else if (beautySticker.getType() == 11) {
                width /= 2.0f;
                f = (f * 3.0f) / 2.0f;
            }
        } else {
            f = (i & 2) > 0 ? height / 4.0f : (i & 16) > 0 ? height * 0.75f : height / 2.0f;
            width = (i & 4) > 0 ? width / 4.0f : (i & 8) > 0 ? width * 0.75f : width / 2.0f;
        }
            Log.d("fdsfjskljfwe23", "setSticker: " + width + "   " + f);
        sticker.getMatrix().postTranslate(width, f);
    }

    public void editTextSticker() {
        this.onStickerOperationListener.onStickerDoubleTap(handlingSticker);
    }

    @NonNull
    public float[] getStickerPoints(@Nullable Sticker sticker) {
        float[] fArr = new float[8];
        getStickerPoints(sticker, fArr);
        return fArr;
    }

    public void getStickerPoints(@Nullable Sticker sticker, @NonNull float[] fArr) {
        if (sticker == null) {
            Arrays.fill(fArr, 0.0f);
            return;
        }
        sticker.getBoundPoints(bounds);
        sticker.getMappedPoints(fArr, bounds);
    }

    public void save(@NonNull File file) {
        try {
            StickerUtils.saveImageToGallery(file, createBitmap());
            StickerUtils.notifySystemGallery(getContext(), file);
        } catch (IllegalArgumentException | IllegalStateException unused) {
        }
    }

    @NonNull
    public Bitmap createBitmap() throws OutOfMemoryError {
        handlingSticker = null;
        Bitmap createBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public int getStickerCount() {
        return stickers.size();
    }

    public boolean isNoneSticker() {
        return getStickerCount() == 0;
    }

    @NonNull
    public StickerView setLocked(boolean z) {
        locked = z;
        invalidate();
        return this;
    }

    @NonNull
    public StickerView setMinClickDelayTime(int i) {
        minClickDelayTime = i;
        return this;
    }

    public int getMinClickDelayTime() {
        return minClickDelayTime;
    }

    public boolean isConstrained() {
        return constrained;
    }

    @NonNull
    public StickerView setConstrained(boolean z) {
        constrained = z;
        postInvalidate();
        return this;
    }

    @NonNull
    public StickerView setOnStickerOperationListener(@Nullable OnStickerOperationListener onStickerOperationListener2) {
        onStickerOperationListener = onStickerOperationListener2;
        return this;
    }

    @Nullable
    public OnStickerOperationListener getOnStickerOperationListener() {
        return onStickerOperationListener;
    }

    @Nullable
    public Sticker getCurrentSticker() {
        return handlingSticker;
    }

    @NonNull
    public List<StickerIcons> getIcons() {
        return icons;
    }

    public void setIcons(@NonNull List<StickerIcons> list) {
        icons.clear();
        icons.addAll(list);
        invalidate();
    }

    public boolean isIsframe() {
        return isframe;
    }

    public void setIsframe(boolean isframe) {
        this.isframe = isframe;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(int frameSize) {
        this.frameSize = frameSize;
    }

    public int getFrameColor() {
        return frameColor;
    }

    public void setFrameColor(int frameColor) {
        this.frameColor = frameColor;
    }
}
