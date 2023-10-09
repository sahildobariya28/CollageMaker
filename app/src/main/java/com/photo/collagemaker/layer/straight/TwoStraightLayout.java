package com.photo.collagemaker.layer.straight;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.grid.QueShotLine;

public class TwoStraightLayout extends NumberStraightLayout {
    private float mRadio = 0.5f;

    public int getThemeCount() {
        return 8;
    }

    public TwoStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public TwoStraightLayout(int i) {
        super(i);
    }


    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, QueShotLine.Direction.HORIZONTAL, this.mRadio);
                return;
            case 1:
                addLine(0, QueShotLine.Direction.VERTICAL, this.mRadio);
                return;
            case 2:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.33333334f);
                return;
            case 3:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.6666667f);
                return;
            case 4:
                addLine(0, QueShotLine.Direction.VERTICAL, 0.33333334f);
                return;
            case 5:
                addLine(0, QueShotLine.Direction.VERTICAL, 0.6666667f);
                return;
            case 6:
                // Theme for two images in a horizontal line (1/2 ratio)
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 7:
                // Theme for two images in a vertical line (1/2 ratio)
                addLine(0, QueShotLine.Direction.VERTICAL, 0.5f);
                return;
            default:
                addLine(0, QueShotLine.Direction.HORIZONTAL, this.mRadio);
                return;
        }
    }

    public QueShotLayout clone(QueShotLayout quShotLayout) {
        return new TwoStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
