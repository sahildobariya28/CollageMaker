package com.photo.collagemaker.event;

import com.photo.collagemaker.entity.Photo;

public interface OnItemCheckListener {
    boolean onItemCheck(int i, Photo photo, int i2);
}
