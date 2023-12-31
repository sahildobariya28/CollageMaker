package com.photo.collagemaker.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.photo.collagemaker.activities.editor.single_editor.SingleEditorActivity;
import com.photo.collagemaker.activities.material.CollageMaterialActivity;
import com.photo.collagemaker.activities.material.MaterialThemeList;
import com.photo.collagemaker.activities.picker.MultipleImagePickerActivity;
import com.photo.collagemaker.activities.picker.NewSingleImagePickerActivity;
import com.photo.collagemaker.databinding.ActivityHomeBinding;
import com.photo.collagemaker.dialog.DetailsDialog;
import com.photo.collagemaker.picker.ImageCaptureManager;
import com.photo.collagemaker.preference.Preference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ImageCaptureManager imageCaptureManager;
    ActivityHomeBinding binding;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageCaptureManager = new ImageCaptureManager(this);

        binding.btnCollage.setOnClickListener(view -> {
            Dexter.withContext(HomeActivity.this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Intent intent = new Intent(HomeActivity.this, MultipleImagePickerActivity.class);
                        intent.putExtra(MultipleImagePickerActivity.KEY_LIMIT_MAX_IMAGE, 20);
                        intent.putExtra(MultipleImagePickerActivity.KEY_LIMIT_MIN_IMAGE, 2);
                        intent.putExtra("tracker", "Collage");
                        startActivityForResult(intent, 1001);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        DetailsDialog.showDetailsDialog(HomeActivity.this);
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(dexterError -> Toast.makeText(HomeActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
        });

        binding.btnEditor.setOnClickListener(view -> {
            Dexter.withContext(HomeActivity.this)
                    .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                startActivityForResult(new Intent(HomeActivity.this, NewSingleImagePickerActivity.class), 233);
                            }
                            if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                                DetailsDialog.showDetailsDialog(HomeActivity.this);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    })
                    .withErrorListener(dexterError -> Toast.makeText(HomeActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show()).onSameThread().check();


        });
        binding.cardFreeStyle.setOnClickListener(view -> {
            Dexter.withContext(HomeActivity.this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Intent intent = new Intent(HomeActivity.this, MultipleImagePickerActivity.class);
                        intent.putExtra(MultipleImagePickerActivity.KEY_LIMIT_MAX_IMAGE, 20);
                        intent.putExtra(MultipleImagePickerActivity.KEY_LIMIT_MIN_IMAGE, 2);
                        intent.putExtra("tracker", "ScrapBook");
                        startActivityForResult(intent, 1001);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        DetailsDialog.showDetailsDialog(HomeActivity.this);
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(dexterError -> Toast.makeText(HomeActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
        });
        binding.cardMultiFit.setOnClickListener(view -> {
            Dexter.withContext(HomeActivity.this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Intent intent = new Intent(HomeActivity.this, MultipleImagePickerActivity.class);
                        intent.putExtra(MultipleImagePickerActivity.KEY_LIMIT_MAX_IMAGE, 20);
                        intent.putExtra(MultipleImagePickerActivity.KEY_LIMIT_MIN_IMAGE, 2);
                        intent.putExtra("tracker", "Multifit");
                        startActivityForResult(intent, 1001);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        DetailsDialog.showDetailsDialog(HomeActivity.this);
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(dexterError -> Toast.makeText(HomeActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
        });

        binding.cardMaterial.setOnClickListener(view -> {
//            startActivity(new Intent(this, MaterialThemeList.class));
        });

        binding.cardStitch.setOnClickListener(view -> {
            Dexter.withContext(HomeActivity.this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Intent intent = new Intent(HomeActivity.this, MultipleImagePickerActivity.class);
                        intent.putExtra(MultipleImagePickerActivity.KEY_LIMIT_MAX_IMAGE, 20);
                        intent.putExtra(MultipleImagePickerActivity.KEY_LIMIT_MIN_IMAGE, 2);
                        intent.putExtra("tracker", "Stitch");
                        startActivityForResult(intent, 1001);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        DetailsDialog.showDetailsDialog(HomeActivity.this);
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(dexterError -> Toast.makeText(HomeActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
        });

        binding.imageViewSettings.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
        });
    }

    public void onPostCreate(@Nullable Bundle bundle) {
        super.onPostCreate(bundle);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            super.onActivityResult(i, i2, intent);
        } else if (i == 1) {
            if (imageCaptureManager == null) {
                imageCaptureManager = new ImageCaptureManager(this);
            }
            new Handler().post(() -> imageCaptureManager.galleryAddPic());
            startActivity(new Intent(getApplicationContext(), SingleEditorActivity.class).putExtra("SELECTED_PHOTOS", imageCaptureManager.getCurrentPhotoPath()));
        }
    }

    public void onResume() {
        super.onResume();
        Preference.increateCounter(getApplicationContext());
    }

    public void takePhotoFromCamera() {
        try {
            startActivityForResult(imageCaptureManager.dispatchTakePictureIntent(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException unused) {
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        finish();
    }

}
