package com.photo.collagemaker.activities.multifit;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.photo.collagemaker.databinding.ActivityGridBinding;
import com.photo.collagemaker.databinding.ActivityMultiFitBinding;

public class MultiFitViewModelFactory implements ViewModelProvider.Factory{

    Activity activity;
    ActivityMultiFitBinding binding;

    public MultiFitViewModelFactory(Activity activity, ActivityMultiFitBinding binding){
        this.activity = activity;
        this.binding = binding;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MultiFitViewModel.class)) {
            return (T) new MultiFitViewModel(activity, binding);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}