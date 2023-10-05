package com.photo.collagemaker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.photo.collagemaker.R;
import com.photo.collagemaker.fragment.ImagePagerFragment;
import com.photo.collagemaker.queshot.QueShotPreview;

import java.util.ArrayList;

public class PhotoPagerActivity extends AppCompatActivity {
    public ActionBar actionBar;
    private ImagePagerFragment fragment_photo_pager;
    public boolean setDelete;
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_photo_pager);


        int intExtra = getIntent().getIntExtra(QueShotPreview.EXTRA_CURRENT_ITEM, 0);
        ArrayList<String> stringArrayListExtra = getIntent().getStringArrayListExtra(QueShotPreview.EXTRA_PHOTOS);
        setDelete = getIntent().getBooleanExtra(QueShotPreview.EXTRA_SHOW_DELETE, true);
        if (fragment_photo_pager == null) {
            fragment_photo_pager = (ImagePagerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_photo_pager);
        }
        fragment_photo_pager.setPhotos(stringArrayListExtra, intExtra);
        setSupportActionBar(findViewById(R.id.toolbar));
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                actionBar.setElevation(25.0f);
            }
        }

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
