package com.photo.collagemaker.layer.straight;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.grid.QueShotLine;
import com.photo.collagemaker.layer.straight.layout.NumberStraightLayout;
import com.photo.collagemaker.layer.straight.layout.StraightCollageLayout;

public class SixteenStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 6;
    }

    public SixteenStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public SixteenStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                cutAreaEqualPart(0, 3, 3);
                break;
            case 1:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.90f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.111f);
                cutAreaEqualPart(2, 3, QueShotLine.Direction.VERTICAL);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.90f);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.111f);
                cutAreaEqualPart(3, 2, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(2, 2, QueShotLine.Direction.HORIZONTAL);
                addCross(2, 0.5f, 0.5f);
                cutAreaEqualPart(1, 2, QueShotLine.Direction.HORIZONTAL);

                cutAreaEqualPart(0, 4, QueShotLine.Direction.VERTICAL);

                break;
            case 2:
                addCross(0, .2f, .2f);
                addCross(3, .2f, .2f);
                addCross(6, .2f, .2f);
                addCross(9, .2f, .2f);
                addCross(12, .2f, .2f);

                break;
            case 3:
                addCross(0, 0.2f, 0.8f);
                addCross(2, 0.2f, 0.8f);
                addCross(5, 0.2f, 0.8f);
                addCross(8, 0.2f, 0.8f);
                addCross(11, 0.2f, 0.8f);

                break;
            case 4:
                addCross(0, 0.8f, 0.2f);
                addCross(1, 0.8f, 0.2f);
                addCross(2, 0.8f, 0.2f);
                addCross(3, 0.8f, 0.2f);
                addCross(4, 0.8f, 0.2f);

                break;
            case 5:
                addCross(0, .8f, .8f);
                addCross(0, .8f, .8f);
                addCross(0, .8f, .8f);
                addCross(0, .8f, .8f);
                addCross(0, .8f, .8f);
                break;

        }
    }

    public QueShotLayout clone(QueShotLayout quShotLayout) {
        return new SixteenStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}

