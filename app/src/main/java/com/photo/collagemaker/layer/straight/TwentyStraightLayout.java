package com.photo.collagemaker.layer.straight;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.grid.QueShotLine;
import com.photo.collagemaker.layer.straight.layout.NumberStraightLayout;
import com.photo.collagemaker.layer.straight.layout.StraightCollageLayout;

public class TwentyStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 6;
    }

    public TwentyStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public TwentyStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                cutAreaEqualPart(0, 3, 4);
                break;
            case 1:
                cutAreaEqualPart(0, 4, 3);
                break;
            case 2:
                cutAreaEqualPart(0, 4, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(3, 4, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(2, 6, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 6, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 4, QueShotLine.Direction.VERTICAL);

                break;
            case 3:
                cutAreaEqualPart(0, 4, QueShotLine.Direction.HORIZONTAL);
                addLine(3, QueShotLine.Direction.VERTICAL, 0.70f);
                addLine(2, QueShotLine.Direction.VERTICAL, 0.70f);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.30f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.30f);
                cutAreaEqualPart(6, 4, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(4, 4, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(3, 4, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 4, QueShotLine.Direction.VERTICAL);
                break;
            case 4:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.5f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.8f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.75f);

                cutAreaEqualPart(3, 1, 5);
                cutAreaEqualPart(2, 3, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(1, 3, QueShotLine.Direction.HORIZONTAL);

                cutAreaEqualPart(18, 2, QueShotLine.Direction.HORIZONTAL);


                break;
            case 5:
                cutAreaEqualPart(0, 3, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(2, 6, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 1, 5);
                cutAreaEqualPart(0, 2, QueShotLine.Direction.VERTICAL);
                break;

        }
    }

    public QueShotLayout clone(QueShotLayout quShotLayout) {
        return new TwentyStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}

