package com.photo.collagemaker.layer.straight;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.grid.QueShotLine;
import com.photo.collagemaker.layer.straight.layout.NumberStraightLayout;
import com.photo.collagemaker.layer.straight.layout.StraightCollageLayout;

public class ThirteenStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 6;
    }

    public ThirteenStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public ThirteenStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.33333334f);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.33333334f);
                cutAreaEqualPart(4, 5, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 5, QueShotLine.Direction.VERTICAL);

                break;
            case 1:
                addLine(0, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.33333334f);
                cutAreaEqualPart(2, 5, QueShotLine.Direction.HORIZONTAL);
                addLine(1, QueShotLine.Direction.HORIZONTAL, 0.75f);
                addLine(1, QueShotLine.Direction.HORIZONTAL, 0.33333334f);
                cutAreaEqualPart(0, 5, QueShotLine.Direction.HORIZONTAL);
                break;
            case 2:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.33333334f);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.33333334f);

                cutAreaEqualPart(4, 4, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(3, 2, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(1, 2, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 4, QueShotLine.Direction.VERTICAL);
                break;
            case 3:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.5f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.8f);

                cutAreaEqualPart(2, 1, 4);
                cutAreaEqualPart(1, 2, QueShotLine.Direction.HORIZONTAL);

                break;
            case 4:
                cutAreaEqualPart(0, 3, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(2, 6, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 1, 2);

                break;
            case 5:
                cutAreaEqualPart(0, 3, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(2, 5, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 5, QueShotLine.Direction.VERTICAL);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.33333334f);
                break;

        }
    }

    public QueShotLayout clone(QueShotLayout quShotLayout) {
        return new ThirteenStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}

