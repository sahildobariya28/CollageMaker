package com.photo.collagemaker;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;


public class MyApplication extends Application {
    private static MyApplication myApplication;

    public void onCreate() {
        super.onCreate();
        myApplication = this;
        if (Build.VERSION.SDK_INT >= 26) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke( null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Context getContext() {
        return myApplication.getContext();
    }

}
