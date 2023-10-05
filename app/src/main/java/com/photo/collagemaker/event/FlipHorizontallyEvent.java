package com.photo.collagemaker.event;

public class FlipHorizontallyEvent extends AbstractFlipEvent {
    protected int getFlipDirection() {
        return 1;
    }
}
