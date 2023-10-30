package com.photo.collagemaker.layer.straight;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.grid.QueShotLine;
import com.photo.collagemaker.layer.straight.layout.NumberStraightLayout;
import com.photo.collagemaker.layer.straight.layout.StraightCollageLayout;

public class EighteenStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 7;
    }

    public EighteenStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public EighteenStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, QueShotLine.Direction.VERTICAL, 0.80f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.25f);
                addLine(1, QueShotLine.Direction.HORIZONTAL, 0.75f);

                cutAreaEqualPart(3, 3, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(2, 7, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 7, QueShotLine.Direction.HORIZONTAL);

                break;
            case 1:
                cutAreaEqualPart(0, 4, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(3, 5, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(2, 5, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 5, QueShotLine.Direction.VERTICAL);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.80f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.25f);

                break;
            case 2:
                cutAreaEqualPart(0, 4, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(3, 2, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(3, 2, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(2, 5, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 5, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 5, QueShotLine.Direction.VERTICAL);
                break;
            case 3:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.60f);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.50f);
                addLine(2, QueShotLine.Direction.VERTICAL, 0.50f);
                addCross(1, 0.5f, 0.5f);
                cutAreaEqualPart(0, 2, 3);

                break;
            case 4:
                cutAreaEqualPart(0, 2, 5);

                break;
            case 5:
                cutAreaEqualPart(0, 5, 2);

                break;
            case 6:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.80f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.25f);

                cutAreaEqualPart(2, 7, QueShotLine.Direction.VERTICAL);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.80f);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.25f);
                cutAreaEqualPart(2, 2, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 7, QueShotLine.Direction.VERTICAL);
                break;

        }
    }

    public QueShotLayout clone(QueShotLayout quShotLayout) {
        return new EighteenStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}

