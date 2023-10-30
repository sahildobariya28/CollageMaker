package com.photo.collagemaker.activities.multifit;

import android.graphics.Bitmap;

public class ImageModel {

    String imagePath;
    Bitmap background;

    public ImageModel(String imagePath, Bitmap backgroun) {
        this.imagePath = imagePath;
        this.background = background;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Bitmap getBackground() {
        return background;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }
}
