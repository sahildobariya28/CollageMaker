package com.photo.collagemaker.layer.stitch;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.grid.QueShotLine;
import com.photo.collagemaker.layer.straight.layout.NumberStraightLayout;
import com.photo.collagemaker.layer.straight.layout.StraightCollageLayout;

public class DynamicStraightLayout extends NumberStraightLayout {
    boolean isVertical;
    int imageListSize;
    public int getThemeCount() {
        return 1;
    }

    public DynamicStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);

    }

    public DynamicStraightLayout(int i, boolean isVertical, int imageListSize) {
        super(i);
        this.isVertical = isVertical;
        this.imageListSize = imageListSize;
    }

    public void layout() {
        if (isVertical) {
            cutAreaEqualPart(0, imageListSize, QueShotLine.Direction.HORIZONTAL);
        }else {
            cutAreaEqualPart(0, imageListSize, QueShotLine.Direction.VERTICAL);
        }
    }

    public QueShotLayout clone(QueShotLayout quShotLayout) {
        return new DynamicStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
