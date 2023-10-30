package com.photo.collagemaker.layer.straight;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.grid.QueShotLine;
import com.photo.collagemaker.layer.straight.layout.NumberStraightLayout;
import com.photo.collagemaker.layer.straight.layout.StraightCollageLayout;

public class NineteenStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 6;
    }

    public NineteenStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public NineteenStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.80f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.25f);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.80f);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.25f);
                cutAreaEqualPart(4, 7, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(3, 2, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(1, 2, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 7, QueShotLine.Direction.VERTICAL);

                break;
            case 1:
                addLine(0, QueShotLine.Direction.VERTICAL, 0.80f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.25f);
                addLine(1, QueShotLine.Direction.HORIZONTAL, 0.70f);
                addLine(1, QueShotLine.Direction.HORIZONTAL, 0.25f);

                cutAreaEqualPart(4, 3, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(2, 7, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 7, QueShotLine.Direction.HORIZONTAL);
                break;
            case 2:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.33333334f);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.80f);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.25f);

                cutAreaEqualPart(4, 5, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(3, 3, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(2, 2, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(2, 2, QueShotLine.Direction.VERTICAL);

                cutAreaEqualPart(1, 3, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 5, QueShotLine.Direction.VERTICAL);
                break;
            case 3:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.5f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.8f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.75f);

                cutAreaEqualPart(3, 1, 5);
                cutAreaEqualPart(2, 3, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(1, 3, QueShotLine.Direction.HORIZONTAL);


                break;
            case 4:
                cutAreaEqualPart(0, 3, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(2, 6, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 1, 5);

                break;
            case 5:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.8f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.75f);

                cutAreaEqualPart(2, 6, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 6, QueShotLine.Direction.VERTICAL);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.80f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.25f);
                cutAreaEqualPart(2, 3, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 3, QueShotLine.Direction.HORIZONTAL);

                break;

        }
    }

    public QueShotLayout clone(QueShotLayout quShotLayout) {
        return new NineteenStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}

