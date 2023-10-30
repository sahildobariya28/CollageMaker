package com.photo.collagemaker.layer.straight;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.grid.QueShotLine;
import com.photo.collagemaker.layer.straight.layout.NumberStraightLayout;
import com.photo.collagemaker.layer.straight.layout.StraightCollageLayout;

public class TenStraightLayout extends NumberStraightLayout {
    public TenStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public TenStraightLayout(int i) {
        super(i);
    }

    public int getThemeCount() {
        return 6; // You can change this value to match the number of themes you want.
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                cutAreaEqualPart(0, 1, 4);
                break;
            case 1:
                cutAreaEqualPart(0, 4, 1);
                break;
            case 2:
                addLine(0, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.33f);
                cutAreaEqualPart(2, 4, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(1, 2, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 4, QueShotLine.Direction.HORIZONTAL);
                break;
            case 3:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.33f);
                cutAreaEqualPart(2, 4, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 2, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 4, QueShotLine.Direction.VERTICAL);
                break;
            case 4:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.5f);
                cutAreaEqualPart(0, 2, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(2, 1, 3);
                break;
            case 5:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.5f);
                cutAreaEqualPart(1, 2, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 1, 3);
                break;

        }
    }

    public QueShotLayout clone(QueShotLayout quShotLayout) {
        return new TenStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}

