package com.photo.collagemaker.activities.material;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.material.material_assert.MaterialModel;
import com.photo.collagemaker.activities.material.util.MaskImage;
import com.photo.collagemaker.activities.picker.MultipleImagePickerActivity;
import com.photo.collagemaker.databinding.ActivityMaterialThemeListBinding;
import com.photo.collagemaker.dialog.DetailsDialog;

import java.util.ArrayList;
import java.util.List;

public class MaterialThemeList extends AppCompatActivity implements MaterialAdapter.OnCollageMaterial {

    ActivityMaterialThemeListBinding binding;
    MaterialAdapter materialAdapter;
    ArrayList<MaterialModel> listMaterialModel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaterialThemeListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new getCollageMaterial().execute();
    }

    private class getCollageMaterial extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... voidArr) {

            listMaterialModel = MaskImage.fetchAllMaterialTheme();
            return "";
        }

        @Override
        protected void onPostExecute(String str) {
            materialAdapter = new MaterialAdapter(MaterialThemeList.this, R.layout.item_collage_material, listMaterialModel, MaterialThemeList.this);
            binding.gridMaterial.setAdapter(materialAdapter);
        }
    }



    @Override
    public void OnItemListCollageMaterialSelect(MaterialModel imageModel, int position) {
        Dexter.withContext(MaterialThemeList.this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    Intent intent = new Intent(MaterialThemeList.this, MultipleImagePickerActivity.class);
                    intent.putExtra(MultipleImagePickerActivity.KEY_LIMIT_MAX_IMAGE, imageModel.getDrawableSize());
                    intent.putExtra(MultipleImagePickerActivity.KEY_LIMIT_MIN_IMAGE, imageModel.getDrawableSize());
                    intent.putExtra("position", position);
                    intent.putExtra("tracker", "CollageMaterial");
                    startActivityForResult(intent, 1001);
                }
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                    DetailsDialog.showDetailsDialog(MaterialThemeList.this);
                }
            }

            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).withErrorListener(dexterError -> Toast.makeText(MaterialThemeList.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
    }
}