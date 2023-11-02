package com.photo.collagemaker.activities.multifit;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MultiFitViewModelFactory implements ViewModelProvider.Factory{

    Activity activity;

    public MultiFitViewModelFactory(Activity activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MultiFitViewModel.class)) {
            return (T) new MultiFitViewModel(activity);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}