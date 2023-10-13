package com.photo.collagemaker.activities.editor.single_editor;

import android.app.Activity;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.photo.collagemaker.databinding.ActivityEditorBinding;
import com.photo.collagemaker.databinding.ActivityGridBinding;

public class SingleEditorViewModel extends ViewModel {
    Activity activity;
    ActivityEditorBinding binding;

    public SingleEditorViewModel(Activity activity, ActivityEditorBinding binding) {
        this.activity = activity;
        this.binding = binding;
    }

    public void hideAllView() {

    }
}
