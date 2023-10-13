package com.photo.collagemaker.activities.editor.single_editor;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.photo.collagemaker.activities.editor.collage_editor.CollageEditorViewModel;
import com.photo.collagemaker.databinding.ActivityEditorBinding;
import com.photo.collagemaker.databinding.ActivityGridBinding;

public class SingleEditorViewModelFactory implements ViewModelProvider.Factory{

    Activity activity;
    ActivityEditorBinding binding;

    public SingleEditorViewModelFactory(Activity activity, ActivityEditorBinding binding){
        this.activity = activity;
        this.binding = binding;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CollageEditorViewModel.class)) {
            return (T) new SingleEditorViewModel(activity, binding);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}