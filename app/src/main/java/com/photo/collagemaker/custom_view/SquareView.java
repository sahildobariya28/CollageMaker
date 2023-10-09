package com.photo.collagemaker.custom_view;

import android.content.Context;
import android.util.AttributeSet;

public class SquareView extends CustomGridView {
    public SquareView(Context context) {
        super(context);
    }

    public SquareView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SquareView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }


    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredWidth > measuredHeight) {
            measuredWidth = measuredHeight;
        }
        setMeasuredDimension(measuredWidth, measuredWidth);
    }
}
