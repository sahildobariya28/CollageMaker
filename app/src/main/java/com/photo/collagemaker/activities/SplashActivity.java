 package com.photo.collagemaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.photo.collagemaker.R;
import com.photo.collagemaker.databinding.ActivitySplashBinding;

 public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom_delay);
        binding.imageViewLogo.startAnimation(animation);
        binding.textViewTitle.startAnimation(animation1);
        binding.textViewSubTitle.startAnimation(animation1);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }, 2000);
    }

    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }
}
