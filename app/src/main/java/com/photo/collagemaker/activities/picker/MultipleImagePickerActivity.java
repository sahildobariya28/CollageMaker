package com.photo.collagemaker.activities.picker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.editor.collage_editor.CollageEditorActivity;
import com.photo.collagemaker.activities.freestyle.FreeStyleActivity;
import com.photo.collagemaker.activities.material.CollageMaterialActivity;
import com.photo.collagemaker.activities.multifit.MultiFitActivity;
import com.photo.collagemaker.activities.stitch.NewStitchActivity;
import com.photo.collagemaker.activities.stitch.StitchActivity;
import com.photo.collagemaker.adapters.SelectedPhotoAdapter;
import com.photo.collagemaker.constants.Constants;
import com.photo.collagemaker.adapters.AlbumAdapter;
import com.photo.collagemaker.adapters.PhotoAdapter;
import com.photo.collagemaker.databinding.ActivityMultiImagePickerBinding;
import com.photo.collagemaker.interfac.OnSelectedPhoto;
import com.photo.collagemaker.model.ImageModel;
import com.photo.collagemaker.interfac.OnAlbum;
import com.photo.collagemaker.interfac.OnPhoto;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MultipleImagePickerActivity extends AppCompatActivity
        implements OnAlbum, OnPhoto, OnSelectedPhoto {
    public static final String KEY_DATA_RESULT = "KEY_DATA_RESULT";
    public static final String KEY_LIMIT_MAX_IMAGE = "KEY_LIMIT_MAX_IMAGE";
    public static final String KEY_LIMIT_MIN_IMAGE = "KEY_LIMIT_MIN_IMAGE";

    AlbumAdapter albumAdapter;
    ArrayList<ImageModel> listAlbums = new ArrayList<>();
    ArrayList<ImageModel> listPhotos = new ArrayList<>();
    int limitImageMax = 9;
    int limitImageMin = 2;
    PhotoAdapter photoAdapter;
    SelectedPhotoAdapter selectedPhotoAdapter;
    ArrayList<ImageModel> listItemSelect = new ArrayList<>();
    ArrayList<String> stringArrayListPath = new ArrayList<>();
    int selectedPosition = 0;

    ActivityMultiImagePickerBinding binding;
    String tracker;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityMultiImagePickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            limitImageMax = extras.getInt(KEY_LIMIT_MAX_IMAGE, 9);
            limitImageMin = extras.getInt(KEY_LIMIT_MIN_IMAGE, 2);
            tracker = extras.getString("tracker");

            if (tracker.equals("CollageMaterial")){
                selectedPosition = extras.getInt("position", 0);
            }
            if (limitImageMin > limitImageMax || limitImageMin < 1) {
                finish();
            }
        }
        selectedPhotoAdapter = new SelectedPhotoAdapter(this, listItemSelect, this);
        binding.recyclerSelectedItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerSelectedItem.setAdapter(selectedPhotoAdapter);


        binding.textViewDone.setOnClickListener(view -> {
            ArrayList<String> imageList = getListString(listItemSelect);
            if (imageList.size() >= limitImageMin) {
                if (tracker.equals("Stitch")){
                    Intent intent = new Intent(this, NewStitchActivity.class);
                    intent.putStringArrayListExtra(KEY_DATA_RESULT, imageList);
                    startActivity(intent);
                }else if (tracker.equals("CollageMaterial")){
                    Intent intent = new Intent(this, CollageMaterialActivity.class);
                    intent.putStringArrayListExtra(KEY_DATA_RESULT, imageList);
                    intent.putExtra("position", selectedPosition);
                    startActivity(intent);
                }else if (tracker.equals("Multifit")) {
                    Intent intent = new Intent(this, MultiFitActivity.class);
                    intent.putStringArrayListExtra(KEY_DATA_RESULT, imageList);
                    startActivity(intent);
                } else if (tracker.equals("Collage")) {
                    Intent intent = new Intent(this, CollageEditorActivity.class);
                    intent.putStringArrayListExtra(KEY_DATA_RESULT, imageList);
                    startActivity(intent);
                } else if (tracker.equals("ScrapBook")) {
                    Intent intent = new Intent(this, FreeStyleActivity.class);
                    intent.putStringArrayListExtra(KEY_DATA_RESULT, imageList);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Please select at least " + limitImageMin + " images", Toast.LENGTH_SHORT).show();
            }
        });

        binding.dropdownMenu.setOnClickListener(view -> {
            if (binding.gridViewAlbum.getVisibility() == View.VISIBLE) {
                binding.gridViewAlbum.setVisibility(View.GONE);
            } else {
                binding.gridViewAlbum.setVisibility(View.VISIBLE);
            }
        });

        binding.btnBack.setOnClickListener(view -> onBackPressed());



        binding.btnDelete.setOnClickListener(view -> {
            listItemSelect.clear();
            photoAdapter = new PhotoAdapter(this, R.layout.item_list_album, listPhotos, this);
            photoAdapter.updateSelectionList(listItemSelect);
            binding.gridViewPhotos.setAdapter(photoAdapter);


            binding.textSelectCount.setText("Select 1 - " + limitImageMax + " photos  (" + listItemSelect.size() + ")");
            if (listItemSelect.isEmpty()) {
                binding.selectCountContainer.setVisibility(View.GONE);
                binding.recyclerSelectedItem.setVisibility(View.GONE);
            }
            photoAdapter.notifyDataSetChanged();
            selectedPhotoAdapter.notifyDataSetChanged();
        });
        try {
            listAlbums.sort((imageModel, imageModel2) -> imageModel.getName().compareToIgnoreCase(imageModel2.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        albumAdapter = new AlbumAdapter(this, this, R.layout.item_album, listAlbums);

        if (isPermissionGranted("android.permission.READ_EXTERNAL_STORAGE")) {
            new getItemAlbum().execute();
        } else {
            requestPermission("android.permission.READ_EXTERNAL_STORAGE", 1001);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission, int requestCode) {
        ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
    }

    private class getItemAlbum extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... voidArr) {
            Cursor cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{"_data", "bucket_display_name"},
                    null, null, null);

            if (cursor == null) {
                return "";
            }

            int columnIndexOrThrow = cursor.getColumnIndexOrThrow("_data");
            while (cursor.moveToNext()) {
                String string = cursor.getString(columnIndexOrThrow);
                File file = new File(string);
                if (file.exists()) {
                    boolean isValidFile = checkFile(file);
                    if (!Check(file.getParent(), stringArrayListPath) && isValidFile) {
                        stringArrayListPath.add(file.getParent());
                        listAlbums.add(new ImageModel(file.getParentFile().getName(), string, file.getParent()));
                    }
                }
            }
            Collections.sort(listAlbums);
            cursor.close();
            return "";
        }

        @Override
        protected void onPostExecute(String str) {
            setListAlbum(listAlbums.get(0).getPathFolder());
            binding.gridViewAlbum.setAdapter(albumAdapter);
        }
    }

    private class GetItemListAlbum extends AsyncTask<Void, Void, String> {
        private String pathAlbum;

        GetItemListAlbum(String str) {
            pathAlbum = str;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... voidArr) {
            File file = new File(pathAlbum);
            if (!file.isDirectory()) {
                return "";
            }
            listPhotos.clear();
            for (File file2 : file.listFiles()) {
                if (file2.exists()) {
                    boolean isValidFile = checkFile(file2);
                    if (!file2.isDirectory() && isValidFile) {
                        listPhotos.add(new ImageModel(file2.getName(), file2.getAbsolutePath(), file2.getAbsolutePath()));
                        publishProgress();
                    }
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String str) {
            try {
                Collections.sort(listPhotos, (imageModel, imageModel2) -> {
                    File file = new File(imageModel.getPathFolder());
                    File file2 = new File(imageModel2.getPathFolder());
                    if (file.lastModified() > file2.lastModified()) {
                        return -1;
                    }
                    return file.lastModified() < file2.lastModified() ? 1 : 0;
                });
            } catch (Exception e) {
            }
            photoAdapter.notifyDataSetChanged();
        }
    }

    public boolean Check(String string, ArrayList<String> arrayList) {
        return !arrayList.isEmpty() && arrayList.contains(string);
    }

    public void itemSelect(final ImageModel imageModel) {
        imageModel.setId(listItemSelect.size());
        listItemSelect.add(imageModel);
        photoAdapter = new PhotoAdapter(this, R.layout.item_list_album, listPhotos, this);
        photoAdapter.updateSelectionList(listItemSelect);
        binding.gridViewPhotos.setAdapter(photoAdapter);


        if (!listItemSelect.isEmpty()) {
            binding.selectCountContainer.setVisibility(View.VISIBLE);
            binding.recyclerSelectedItem.setVisibility(View.VISIBLE);

            binding.textSelectCount.setText("Select 1 - "+ limitImageMax +" photos  (" + listItemSelect.size() + ")");
        } else {
            binding.selectCountContainer.setVisibility(View.GONE);
            binding.recyclerSelectedItem.setVisibility(View.GONE);
        }

        selectedPhotoAdapter.notifyDataSetChanged();
    }

    public void setListAlbum(String str) {
        binding.textTitle.setText(new File(str).getName());
        photoAdapter = new PhotoAdapter(this, R.layout.item_list_album, listPhotos, this);
        photoAdapter.updateSelectionList(listItemSelect);
        binding.gridViewPhotos.setAdapter(photoAdapter);
        binding.gridViewPhotos.setVisibility(View.GONE);
        binding.gridViewPhotos.setVisibility(View.VISIBLE);
        new GetItemListAlbum(str).execute();
    }


    public ArrayList<String> getListString(ArrayList<ImageModel> arrayList) {
        ArrayList<String> arrayList2 = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(arrayList.get(i).getPathFile());
        }
        return arrayList2;
    }

    public boolean checkFile(File file) {
        if (file == null) {
            return false;
        }
        if (!file.isFile()) {
            return true;
        }
        String name = file.getName();
        if (name.startsWith(".") || file.length() == 0) {
            return false;
        }
        for (int i = 0; i < Constants.FORMAT_IMAGE.size(); i++) {
            if (name.endsWith(Constants.FORMAT_IMAGE.get(i))) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void OnItemAlbumClick(int i) {
        binding.gridViewAlbum.setVisibility(View.GONE);
        setListAlbum(listAlbums.get(i).getPathFolder());
    }

    @Override
    public void OnSelectedItemDelete(ImageModel imageModel) {
        listItemSelect.remove(imageModel);
        photoAdapter = new PhotoAdapter(this, R.layout.item_list_album, listPhotos,this);
        photoAdapter.updateSelectionList(listItemSelect);
        binding.gridViewPhotos.setAdapter(photoAdapter);


        binding.textSelectCount.setText("Select 1 - " + limitImageMax + " photos  (" + listItemSelect.size() + ")");
        if (listItemSelect.isEmpty()) {
            binding.selectCountContainer.setVisibility(View.GONE);
            binding.recyclerSelectedItem.setVisibility(View.GONE);
        }
        photoAdapter.notifyDataSetChanged();
        selectedPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnItemListPhotoClick(ImageModel imageModel) {
        if (listItemSelect.size() < limitImageMax) {
            itemSelect(imageModel);

        } else {
            Toast.makeText(this, "Limit " + limitImageMax + " images", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish();
            } else {
                new getItemAlbum().execute();
            }
        } else if (requestCode == 1002 && grantResults.length > 0) {
            int result = grantResults[0];
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.gridViewAlbum.getVisibility() == View.VISIBLE) {
            binding.gridViewAlbum.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}

