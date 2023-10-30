package com.photo.collagemaker.layer.straight;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.grid.QueShotLine;
import com.photo.collagemaker.layer.straight.layout.NumberStraightLayout;
import com.photo.collagemaker.layer.straight.layout.StraightCollageLayout;

public class FifteenStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 6;
    }

    public FifteenStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public FifteenStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.33f);
                cutAreaEqualPart(2, 3, QueShotLine.Direction.VERTICAL);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.33f);
                cutAreaEqualPart(2, 2, QueShotLine.Direction.HORIZONTAL);
                addCross(2, 0.5f, 0.5f);
                cutAreaEqualPart(1, 2, QueShotLine.Direction.HORIZONTAL);

                cutAreaEqualPart(0, 4, QueShotLine.Direction.VERTICAL);

                break;
            case 1:
//                addCross(0, 0.2f, 0.8f);
//                addCross(2, 0.2f, 0.8f);
//                addCross(5, 0.2f, 0.8f);
//                addCross(8, 0.2f, 0.8f);
//                addCross(11, 0.2f, 0.8f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.80f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.25f);
                cutAreaEqualPart(2, 7, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 7, QueShotLine.Direction.VERTICAL);

                break;
            case 2:
                addLine(0, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.33f);
                cutAreaEqualPart(2, 6, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(1, 3, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 6, QueShotLine.Direction.HORIZONTAL);
                break;
            case 3:
                addLine(0, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.33f);
                cutAreaEqualPart(2, 5, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(1, 5, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 5, QueShotLine.Direction.HORIZONTAL);

                break;
            case 4:
                cutAreaEqualPart(0, 3, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(2, 6, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 1, 3);
//                cutAreaEqualPart(0, 2, QueShotLine.Direction.VERTICAL);

                break;
            case 5:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.80f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.80f);
                cutAreaEqualPart(2, 6, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 5, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 4, QueShotLine.Direction.VERTICAL);
                break;

        }
    }

    public QueShotLayout clone(QueShotLayout quShotLayout) {
        return new FifteenStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}

