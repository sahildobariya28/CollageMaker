package com.photo.collagemaker.layer.straight;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.grid.QueShotLine;
import com.photo.collagemaker.layer.straight.layout.NumberStraightLayout;
import com.photo.collagemaker.layer.straight.layout.StraightCollageLayout;

public class ElevanStraightLayout extends NumberStraightLayout {

    public ElevanStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public ElevanStraightLayout(int i) {
        super(i);
    }


    @Override
    public int getThemeCount() {
        return 5;
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.33333334f);
                cutAreaEqualPart(2, 4, QueShotLine.Direction.VERTICAL);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(1, QueShotLine.Direction.VERTICAL, 0.33333334f);
                cutAreaEqualPart(0, 4, QueShotLine.Direction.VERTICAL);
                break;
            case 1:
                addLine(0, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.33333334f);
                cutAreaEqualPart(2, 4, QueShotLine.Direction.HORIZONTAL);
                addLine(1, QueShotLine.Direction.HORIZONTAL, 0.75f);
                addLine(1, QueShotLine.Direction.HORIZONTAL, 0.33333334f);
                cutAreaEqualPart(0, 4, QueShotLine.Direction.HORIZONTAL);
                break;
            case 2:
                cutAreaEqualPart(0, 3, QueShotLine.Direction.VERTICAL);
                addLine(2, QueShotLine.Direction.HORIZONTAL, 0.75f);
                addLine(2, QueShotLine.Direction.HORIZONTAL, 0.33333334f);
                cutAreaEqualPart(1, 5, QueShotLine.Direction.HORIZONTAL);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.33333334f);
                break;
            case 3:
                cutAreaEqualPart(0, 3, QueShotLine.Direction.HORIZONTAL);
                addLine(2, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(2, QueShotLine.Direction.VERTICAL, 0.33333334f);
                cutAreaEqualPart(1, 5, QueShotLine.Direction.VERTICAL);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.75f);
                addLine(0, QueShotLine.Direction.VERTICAL, 0.33333334f);
                break;
            case 4:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.5f);
                cutAreaEqualPart(1, 1, 3);
                cutAreaEqualPart(0, 3, QueShotLine.Direction.VERTICAL);
                break;

        }
    }

    public QueShotLayout clone(QueShotLayout quShotLayout) {
        return new ElevanStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
