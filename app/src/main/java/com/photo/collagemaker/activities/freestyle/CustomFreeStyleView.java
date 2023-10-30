package com.photo.collagemaker.activities.freestyle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.photo.collagemaker.custom_view.StickerView;
import com.photo.collagemaker.draw.BrushDrawingView;
import com.steelkiwi.cropiwa.AspectRatio;

public class CustomFreeStyleView extends StickerView {

    private AspectRatio aspectRatio;
    private int backgroundResource;


    public CustomFreeStyleView(Context context) {
        super(context);
    }

    public CustomFreeStyleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public CustomFreeStyleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    public CustomFreeStyleView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet);
    }



    public AspectRatio getAspectRatio() {
        return this.aspectRatio;
    }

    public void setAspectRatio(AspectRatio aspectRatio2) {
        this.aspectRatio = aspectRatio2;
    }

    public int getBackgroundResourceMode() {
        return this.backgroundResource;
    }

    public void setBackgroundResourceMode(int i) {
        this.backgroundResource = i;
    }

    private BrushDrawingView brushDrawingView;
    @SuppressLint("ResourceType")
    private void init(Context context, AttributeSet attributeSet) {
        this.brushDrawingView = new BrushDrawingView(getContext());
        this.brushDrawingView.setVisibility(View.GONE);
        this.brushDrawingView.setId(2);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams2.addRule(13, -1);
        layoutParams2.addRule(6, 1);
        layoutParams2.addRule(8, 1);
        addView(this.brushDrawingView, layoutParams2);
    }

    public BrushDrawingView getBrushDrawingView() {
        return this.brushDrawingView;
    }
}
