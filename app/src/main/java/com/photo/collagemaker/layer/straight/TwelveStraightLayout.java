package com.photo.collagemaker.layer.straight;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.grid.QueShotLine;
import com.photo.collagemaker.layer.straight.layout.NumberStraightLayout;
import com.photo.collagemaker.layer.straight.layout.StraightCollageLayout;

public class TwelveStraightLayout extends NumberStraightLayout {
    public TwelveStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public TwelveStraightLayout(int i) {
        super(i);
    }


    @Override
    public int getThemeCount() {
        return 9;
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                cutAreaEqualPart(0, 5, 1);
                break;
            case 1:
                cutAreaEqualPart(0, 1, 5);
                break;
            case 2:
                cutAreaEqualPart(0, 3, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(2, 8, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 2, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 2, QueShotLine.Direction.VERTICAL);

                break;
            case 3:
                cutAreaEqualPart(0, 3, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(2, 2, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 8, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 2, QueShotLine.Direction.VERTICAL);

                break;
            case 4:
                cutAreaEqualPart(0, 3, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(2, 8, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(1, 2, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 2, QueShotLine.Direction.HORIZONTAL);
                break;
            case 5:
                cutAreaEqualPart(0, 3, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(2, 2, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(1, 8, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 2, QueShotLine.Direction.HORIZONTAL);
                break;
            case 6:
                cutAreaEqualPart(0, 3, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(2, 6, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 4, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 2, QueShotLine.Direction.VERTICAL);
                break;
            case 7:
                cutAreaEqualPart(0, 3, QueShotLine.Direction.VERTICAL);
                cutAreaEqualPart(2, 6, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(1, 4, QueShotLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 2, QueShotLine.Direction.HORIZONTAL);
                break;
            case 8:
                cutAreaEqualPart(0, 3, 2);
                break;

        }
    }

    public QueShotLayout clone(QueShotLayout quShotLayout) {
        return new TwelveStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
