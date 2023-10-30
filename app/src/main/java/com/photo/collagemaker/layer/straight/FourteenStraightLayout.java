package com.photo.collagemaker.layer.straight;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.grid.QueShotLine;
import com.photo.collagemaker.layer.straight.layout.NumberStraightLayout;
import com.photo.collagemaker.layer.straight.layout.StraightCollageLayout;

public class FourteenStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 6;
    }

    public FourteenStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public FourteenStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.33f);

                addLine(2, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(2, QueShotLine.Direction.VERTICAL, 0.5f);
                addLine(2, QueShotLine.Direction.VERTICAL, 0.5f);

                addLine(1, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.33f);

                addLine(3, QueShotLine.Direction.HORIZONTAL, 0.5f);
                addLine(2, QueShotLine.Direction.HORIZONTAL, 0.5f);
                addLine(4, QueShotLine.Direction.VERTICAL, 0.7f);
                addLine(2, QueShotLine.Direction.VERTICAL, 0.3f);


                cutAreaEqualPart(0, 3, QueShotLine.Direction.VERTICAL);

                break;
            case 1:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.33f);
                cutAreaEqualPart(2, 6, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 2, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 6, QueShotLine.Direction.VERTICAL);
                break;
            case 2:
                addLine(0, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.33f);
                cutAreaEqualPart(2, 6, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(1, 2, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 6, QueShotLine.Direction.HORIZONTAL);
                break;
            case 3:
                addLine(0, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.33f);
                cutAreaEqualPart(2, 5, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(1, 4, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 5, QueShotLine.Direction.HORIZONTAL);

                break;
            case 4:
                cutAreaEqualPart(0, 3, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(2, 6, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 1, 2);
                cutAreaEqualPart(0, 2, QueShotLine.Direction.VERTICAL);

                break;
            case 5:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.66f);
                cutAreaEqualPart(2, 6, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 5, QueShotLine.Direction.VERTICAL);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.33333334f);
                break;

        }
    }

    public QueShotLayout clone(QueShotLayout quShotLayout) {
        return new FourteenStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
