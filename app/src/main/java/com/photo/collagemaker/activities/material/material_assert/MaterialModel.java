package com.photo.collagemaker.activities.material.material_assert;

import java.util.ArrayList;
import java.util.List;

public class MaterialModel {
    int drawableId;
    int drawableSize;

    public MaterialModel(int drawableId, int drawableSize) {
        this.drawableId = drawableId;
        this.drawableSize = drawableSize;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public int getDrawableSize() {
        return drawableSize;
    }

    public void setDrawableSize(int drawableSize) {
        this.drawableSize = drawableSize;
    }

}
