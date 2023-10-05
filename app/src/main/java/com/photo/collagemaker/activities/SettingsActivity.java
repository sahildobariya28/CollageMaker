package com.photo.collagemaker.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.photo.collagemaker.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.linearLayoutShare.setOnClickListener(view -> {
//            Intent intent = new Intent("android.intent.action.SEND");
//            intent.setType("img_text/plain");
//            intent.putExtra("android.intent.extra.SUBJECT", getString(R.string.app_name));
//            intent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + "com.collage.maker.photo.editing");
//            startActivity(Intent.createChooser(intent, "Choose"));
        });

        binding.linearLayoutRate.setOnClickListener(view -> {
//            new RateDialog(SettingsActivity.this, false).show();
        });

        binding.linearLayoutFeedback.setOnClickListener(view -> {
//            Intent intent = new Intent("android.intent.action.SEND");
//            intent.putExtra("android.intent.extra.EMAIL", new String[]{getResources().getString(R.string.email_feedback)});
//            intent.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name) + " feedback : ");
//            intent.putExtra("android.intent.extra.TEXT", "");
//            intent.setType("message/rfc822");
//            startActivity(Intent.createChooser(intent, getString(R.string.choose_email) + " :"));
        });

        binding.linearLayoutPrivacy.setOnClickListener(view -> {
//            try {
//                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.policy_url))));
//            } catch (Exception ignored) {
//            }
        });

        binding.linearLayoutTermCondition.setOnClickListener(v -> {
//            Log.d("qq","moreApp");
//            try {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + getString(R.string.developer_name))));
//            } catch (android.content.ActivityNotFoundException anfe) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=" + getString(R.string.developer_name))));
//            }
        });
        binding.btnBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

}
