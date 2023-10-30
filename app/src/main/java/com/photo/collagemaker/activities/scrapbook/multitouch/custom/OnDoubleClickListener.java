package com.photo.collagemaker.activities.scrapbook.multitouch.custom;


import com.photo.collagemaker.activities.scrapbook.multitouch.controller.MultiTouchEntity;

public interface OnDoubleClickListener {
	public void onPhotoViewDoubleClick(PhotoView view, MultiTouchEntity entity);
	public void onBackgroundDoubleClick();
}
