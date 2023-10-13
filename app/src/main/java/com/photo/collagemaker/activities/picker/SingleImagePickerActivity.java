package com.photo.collagemaker.activities.picker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

import com.photo.collagemaker.activities.editor.single_editor.SingleEditorActivity;
import com.photo.collagemaker.activities.editor.collage_editor.CollageEditorActivity;
import com.photo.collagemaker.databinding.ActivitySingleImagePickerBinding;
import com.photo.collagemaker.fragment.ImagePagerFragment;
import com.photo.collagemaker.fragment.PhotoPickerFragment;

import java.util.ArrayList;

public class SingleImagePickerActivity extends AppCompatActivity {
    private boolean forwardMain;
    private int maxCount = 9;
    private ArrayList<String> originalPhotos = null;
    private PhotoPickerFragment pickerFragment;

    ActivitySingleImagePickerBinding binding;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivitySingleImagePickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        boolean booleanExtra = getIntent().getBooleanExtra("SHOW_CAMERA", true);
        boolean booleanExtra2 = getIntent().getBooleanExtra("SHOW_GIF", false);
        boolean booleanExtra3 = getIntent().getBooleanExtra("PREVIEW_ENABLED", true);
        boolean isAddImage = getIntent().getBooleanExtra("ADD_IMAGE", true);
        forwardMain = getIntent().getBooleanExtra("MAIN_ACTIVITY", false);

        maxCount = getIntent().getIntExtra("MAX_COUNT", 9);
        int intExtra = getIntent().getIntExtra("column", 3);
        originalPhotos = getIntent().getStringArrayListExtra("ORIGINAL_PHOTOS");
        pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag("tag");
        if (pickerFragment == null) {
            pickerFragment = PhotoPickerFragment.newInstance(booleanExtra, booleanExtra2, booleanExtra3, intExtra, maxCount, originalPhotos);
            getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), pickerFragment, "tag").commit();
            getSupportFragmentManager().executePendingTransactions();
        }
        pickerFragment.getPhotoGridAdapter().setOnItemCheckListener((i, photo, i2) -> {
            if (!forwardMain) {
                startActivity(new Intent(SingleImagePickerActivity.this, SingleEditorActivity.class).putExtra("SELECTED_PHOTOS", photo.getPath()));
                finish();
                return true;
            }
            if (isAddImage) {
                CollageEditorActivity.getQueShotGridActivityInstance().resultAddImage(photo.getPath());
            } else {
                CollageEditorActivity.getQueShotGridActivityInstance().replaceCurrentPiece(photo.getPath());
            }
            finish();
            return true;
        });
    }
}
