package com.photo.collagemaker.listener;

import com.photo.collagemaker.draw.BrushDrawingView;

public interface BrushColorChangeListener {
    void onStartDrawing();

    void onStopDrawing();

    void onViewAdd(BrushDrawingView brushDrawingView);

    void onViewRemoved(BrushDrawingView brushDrawingView);
}
