package com.photo.collagemaker.activities.editor.collage_editor;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.photo.collagemaker.databinding.ActivityGridBinding;

public class CollageEditorViewModelFactory implements ViewModelProvider.Factory{

    Activity activity;
    ActivityGridBinding binding;

    public CollageEditorViewModelFactory(Activity activity, ActivityGridBinding binding){
        this.activity = activity;
        this.binding = binding;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CollageEditorViewModel.class)) {
            return (T) new CollageEditorViewModel(activity, binding);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}