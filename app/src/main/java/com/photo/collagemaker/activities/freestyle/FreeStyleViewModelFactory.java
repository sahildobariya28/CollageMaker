package com.photo.collagemaker.activities.freestyle;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.photo.collagemaker.databinding.ActivityFreeStyleBinding;


public class FreeStyleViewModelFactory implements ViewModelProvider.Factory{

    Activity activity;
    ActivityFreeStyleBinding binding;

    public FreeStyleViewModelFactory(Activity activity, ActivityFreeStyleBinding binding){
        this.activity = activity;
        this.binding = binding;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FreeStyleViewModel.class)) {
            return (T) new FreeStyleViewModel(activity, binding);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}