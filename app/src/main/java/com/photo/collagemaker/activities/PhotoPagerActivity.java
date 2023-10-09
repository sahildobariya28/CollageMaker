package com.photo.collagemaker.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.photo.collagemaker.R;
import com.photo.collagemaker.databinding.ActivityPhotoPagerBinding;
import com.photo.collagemaker.fragment.ImagePagerFragment;
import com.photo.collagemaker.custom_view.CollagePreview;

import java.util.ArrayList;

public class PhotoPagerActivity extends AppCompatActivity {
    public ActionBar actionBar;
    private ImagePagerFragment fragment_photo_pager;
    public boolean setDelete;
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    ActivityPhotoPagerBinding binding;
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityPhotoPagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        int intExtra = getIntent().getIntExtra(CollagePreview.EXTRA_CURRENT_ITEM, 0);
        ArrayList<String> stringArrayListExtra = getIntent().getStringArrayListExtra(CollagePreview.EXTRA_PHOTOS);
        setDelete = getIntent().getBooleanExtra(CollagePreview.EXTRA_SHOW_DELETE, true);
        if (fragment_photo_pager == null) {
            fragment_photo_pager = (ImagePagerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_photo_pager);
        }
        fragment_photo_pager.setPhotos(stringArrayListExtra, intExtra);

        binding.btnBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("SELECTED_PHOTOS", fragment_photo_pager.getPaths());
        setResult(-1, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
