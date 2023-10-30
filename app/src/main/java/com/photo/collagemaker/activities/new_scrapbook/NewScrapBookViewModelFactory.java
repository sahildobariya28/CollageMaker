package com.photo.collagemaker.activities.new_scrapbook;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.photo.collagemaker.activities.editor.collage_editor.CollageEditorViewModel;
import com.photo.collagemaker.databinding.ActivityGridBinding;
import com.photo.collagemaker.databinding.ActivityNewScrapBookBinding;

public class NewScrapBookViewModelFactory implements ViewModelProvider.Factory{

    Activity activity;
    ActivityNewScrapBookBinding binding;

    public NewScrapBookViewModelFactory(Activity activity, ActivityNewScrapBookBinding binding){
        this.activity = activity;
        this.binding = binding;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewScrapBookViewModel.class)) {
            return (T) new NewScrapBookViewModel(activity, binding);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}