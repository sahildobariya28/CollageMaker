package com.photo.collagemaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

import com.photo.collagemaker.R;
import com.photo.collagemaker.fragment.ImagePagerFragment;
import com.photo.collagemaker.fragment.PhotoPickerFragment;

import java.util.ArrayList;

public class SingleImagePickerActivity extends AppCompatActivity {
    private boolean forwardMain;
    private ImagePagerFragment imagePagerFragment;
    private int maxCount = 9;
    private ArrayList<String> originalPhotos = null;
    private PhotoPickerFragment pickerFragment;
    private boolean showGif = false;

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        boolean booleanExtra = getIntent().getBooleanExtra("SHOW_CAMERA", true);
        boolean booleanExtra2 = getIntent().getBooleanExtra("SHOW_GIF", false);
        boolean booleanExtra3 = getIntent().getBooleanExtra("PREVIEW_ENABLED", true);
        forwardMain = getIntent().getBooleanExtra("MAIN_ACTIVITY", false);
        setShowGif(booleanExtra2);
        setContentView(R.layout.activity_single_image_picker);

        maxCount = getIntent().getIntExtra("MAX_COUNT", 9);
        int intExtra = getIntent().getIntExtra("column", 3);
        originalPhotos = getIntent().getStringArrayListExtra("ORIGINAL_PHOTOS");
        pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag("tag");
        if (pickerFragment == null) {
            pickerFragment = PhotoPickerFragment.newInstance(booleanExtra, booleanExtra2, booleanExtra3, intExtra, maxCount, originalPhotos);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, pickerFragment, "tag").commit();
            getSupportFragmentManager().executePendingTransactions();
        }
        pickerFragment.getPhotoGridAdapter().setOnItemCheckListener((i, photo, i2) -> {
            if (!forwardMain) {
                Intent intent = new Intent(SingleImagePickerActivity.this, EditorActivity.class);
                intent.putExtra("SELECTED_PHOTOS", photo.getPath());
                startActivity(intent);
                finish();
                return true;
            }
            GridActivity.getQueShotGridActivityInstance().replaceCurrentPiece(photo.getPath());
            finish();
            return true;
        });
    }


    public void onBackPressed() {
        if (imagePagerFragment == null || !imagePagerFragment.isVisible()) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setShowGif(boolean z) {
        showGif = z;
    }
}
