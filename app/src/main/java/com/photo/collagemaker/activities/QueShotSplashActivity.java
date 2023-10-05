 package com.photo.collagemaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.photo.collagemaker.R;

public class QueShotSplashActivity extends AppCompatActivity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom_delay);
        findViewById(R.id.imageViewLogo).startAnimation(animation);
        findViewById(R.id.textViewTitle).startAnimation(animation1);
        findViewById(R.id.textViewSubTitle).startAnimation(animation1);

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
