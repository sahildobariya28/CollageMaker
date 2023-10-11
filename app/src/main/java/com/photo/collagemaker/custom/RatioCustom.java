package com.photo.collagemaker.custom;

import com.steelkiwi.cropiwa.AspectRatio;

public class RatioCustom extends AspectRatio {
    private int selectedItem, i, i2;
    private String name;

    public RatioCustom(int i, int i2, int selectedItem, String name) {
        super(i, i2);
        this.selectedItem = selectedItem;
        this.i = i;
        this.i2 = i2;
        this.name = name;
    }

    public int getSelectedIem() {
        return selectedItem;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return i;
    }

    public int getY() {
        return i2;
    }

}
