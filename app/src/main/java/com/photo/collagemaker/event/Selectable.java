package com.photo.collagemaker.event;

import com.photo.collagemaker.entity.Photo;

public interface Selectable {

    int getSelectedItemCount();

    boolean isSelected(Photo photo);

}
