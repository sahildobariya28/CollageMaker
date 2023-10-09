package com.photo.collagemaker.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.photo.collagemaker.R;
import com.photo.collagemaker.constants.Constants;
import com.photo.collagemaker.databinding.ActivitySharePhotoBinding;

import java.io.File;

public class PhotoShareActivity extends BaseActivity implements View.OnClickListener {
    private File file;

    ActivitySharePhotoBinding binding;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivitySharePhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String string = getIntent().getExtras().getString("path");
        this.file = new File(string);
        Glide.with(getApplicationContext()).load(this.file).into((ImageView) binding.imageViewPreview);
        binding.imageViewPreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClick(view);
            }
        });

        binding.btnBack.setOnClickListener(view -> onBackPressed());

        binding.btnHome.setOnClickListener(view -> {
            Intent intent = new Intent(PhotoShareActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }


    public void onDestroy() {
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
    }

    @SuppressLint("WrongConstant")
    public void onClick(View view) {
        if (view != null) {
            int id = view.getId();

            if (id != binding.imageViewPreview.getId()) {
                if (id == binding.linearLayoutShareOne.getId()) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("image/*");
                    intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getApplicationContext(), getResources().getString(R.string.file_provider), file));
                    intent.addFlags(3);
                    startActivity(Intent.createChooser(intent, "Share"));
                } else if (id == binding.linearLayoutFacebook.getId()) {
                    sharePhoto(Constants.FACEBOOK);
                } else if (id == binding.linearLayoutInstagram.getId()) {
                    sharePhoto(Constants.INSTAGRAM);
                } else if (id == binding.linearLayoutMessenger.getId()) {
                    sharePhoto(Constants.MESSEGER);
                } else if (id == binding.linearLayoutShareMore.getId()) {
                    Uri createCacheFile = createCacheFile();
                    if (createCacheFile != null) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.SEND");
                        intent.addFlags(1);
                        intent.setDataAndType(createCacheFile, getContentResolver().getType(createCacheFile));
                        intent.putExtra("android.intent.extra.STREAM", createCacheFile);
                        startActivity(Intent.createChooser(intent, "Choose an app"));

                    }
                    Toast.makeText(this, "Fail to sharing", 0).show();

                }
                if (id == binding.linearLayoutTwitter.getId()) {
                    sharePhoto(Constants.TWITTER);
                } else if (id == binding.linearLayoutWhatsapp.getId()) {
                    sharePhoto(Constants.WHATSAPP);
                }
            } else {
                Intent intent4 = new Intent();
                intent4.setAction("android.intent.action.VIEW");
                intent4.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), getResources().getString(R.string.file_provider), this.file), "image/*");
                intent4.addFlags(3);
                startActivity(intent4);
            }
        }
    }



    @SuppressLint("WrongConstant")
    public void sharePhoto(String str) {
        if (isPackageInstalled(this, str)) {
            Uri createCacheFile = createCacheFile();
            if (createCacheFile != null) {
                startActivity(new Intent().setAction("android.intent.action.SEND").addFlags(1).setDataAndType(createCacheFile, getContentResolver().getType(createCacheFile)).putExtra("android.intent.extra.STREAM", createCacheFile).setPackage(str));
            }
            Toast.makeText(this, "Fail to sharing", 0).show();
        }
        Toast.makeText(this, "Can't find this App, please download and try it again", 0).show();
        startActivity(new Intent("android.intent.action.VIEW").setData(Uri.parse("market://details?id=" + str)));
    }

    @SuppressLint("WrongConstant")
    public static boolean isPackageInstalled(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 128);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private Uri createCacheFile() {
        return FileProvider.getUriForFile(getApplicationContext(), getResources().getString(R.string.file_provider), this.file);
    }
}
