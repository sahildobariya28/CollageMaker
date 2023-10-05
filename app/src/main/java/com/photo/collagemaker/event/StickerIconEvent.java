package com.photo.collagemaker.event;

import android.view.MotionEvent;

import com.photo.collagemaker.queshot.QueShotStickerView;

public interface StickerIconEvent {
    void onActionDown(QueShotStickerView paramStickerView, MotionEvent paramMotionEvent);

    void onActionMove(QueShotStickerView paramStickerView, MotionEvent paramMotionEvent);

    void onActionUp(QueShotStickerView paramStickerView, MotionEvent paramMotionEvent);
}
