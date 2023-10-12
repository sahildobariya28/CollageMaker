package com.photo.collagemaker.custom;

import com.steelkiwi.cropiwa.AspectRatio;

public class RatioCustom extends AspectRatio {
    private int imgSelected,imgUnselected, i, i2;
    private String name;

    public RatioCustom(int i, int i2, int imgSelected,int imgUnselected, String name) {
        super(i, i2);
        this.imgSelected = imgSelected;
        this.imgUnselected = imgUnselected;
        this.i = i;
        this.i2 = i2;
        this.name = name;
    }

    public int getImgSelected() {
        return imgSelected;
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

    public void setImgSelected(int imgSelected) {
        this.imgSelected = imgSelected;
    }

    public int getImgUnselected() {
        return imgUnselected;
    }

    public void setImgUnselected(int imgUnselected) {
        this.imgUnselected = imgUnselected;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getI2() {
        return i2;
    }

    public void setI2(int i2) {
        this.i2 = i2;
    }

    public void setName(String name) {
        this.name = name;
    }
}
