package com.photo.collagemaker.activities.editor.scrapbook.multitouch.custom;


import com.photo.collagemaker.activities.editor.scrapbook.multitouch.controller.MultiTouchEntity;

public interface OnDoubleClickListener {
	public void onPhotoViewDoubleClick(PhotoView view, MultiTouchEntity entity);
	public void onBackgroundDoubleClick();
}
