package com.photo.collagemaker.layer.slant;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.grid.QueShotLine;

public class TwoSlantLayout extends NumberSlantLayout {
    public int getThemeCount() {
        return 4;
    }


    public TwoSlantLayout(SlantCollageLayout slantPuzzleLayout, boolean z) {
        super(slantPuzzleLayout, z);
    }

    public TwoSlantLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.56f, 0.44f);
                return;
            case 1:
                addLine(0, QueShotLine.Direction.VERTICAL, 0.56f, 0.44f);
                return;
            case 2:
                addLine(0, QueShotLine.Direction.VERTICAL, 0.10f, 0.90f);
                return;
            case 3:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.90f, 0.10f);
                return;
            case 4:
                addLine(0, QueShotLine.Direction.VERTICAL, 0.20f, 0.80f);
                return;
            case 5:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.80f, 0.20f);
                return;
            case 6:
                addLine(0, QueShotLine.Direction.VERTICAL, 0.30f, 0.70f);
                return;
            case 7:
                addLine(0, QueShotLine.Direction.HORIZONTAL, 0.70f, 0.30f);
                return;
            default:
                return;
        }
    }

    public QueShotLayout clone(QueShotLayout quShotLayout) {
        return new TwoSlantLayout((SlantCollageLayout) quShotLayout, true);
    }
}
