package com.photo.collagemaker.custom_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.photo.collagemaker.activities.PhotoPagerActivity;

public class CollagePreview {
    public static final String EXTRA_CURRENT_ITEM = "current_item";
    public static final String EXTRA_PHOTOS = "photos";
    public static final String EXTRA_SHOW_DELETE = "show_delete";
    public static final int REQUEST_CODE = 666;

    public static PhotoPreviewBuilder builder() {
        return new PhotoPreviewBuilder();
    }

    public static class PhotoPreviewBuilder {
        private Intent mPreviewIntent = new Intent();
        private Bundle mPreviewOptionsBundle = new Bundle();

        public void start(@NonNull Activity activity, int i) {
            activity.startActivityForResult(getIntent(activity), i);
        }

        public void start(@NonNull Context context, @NonNull Fragment fragment, int i) {
            fragment.startActivityForResult(getIntent(context), i);
        }

        public void start(@NonNull Context context, @NonNull Fragment fragment) {
            fragment.startActivityForResult(getIntent(context), CollagePreview.REQUEST_CODE);
        }

        public void start(@NonNull Activity activity) {
            start(activity, CollagePreview.REQUEST_CODE);
        }

        public Intent getIntent(@NonNull Context context) {
            this.mPreviewIntent.setClass(context, PhotoPagerActivity.class);
            this.mPreviewIntent.putExtras(this.mPreviewOptionsBundle);
            return this.mPreviewIntent;
        }


    }
}
