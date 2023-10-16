package com.photo.collagemaker.activities.editor.single_editor;

import android.app.Activity;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.photo.collagemaker.databinding.ActivityEditorBinding;

public class SingleEditorViewModel extends ViewModel {
    Activity activity;
    ActivityEditorBinding binding;

    public SingleEditorViewModel(Activity activity, ActivityEditorBinding binding) {
        this.activity = activity;
        this.binding = binding;
    }

    public void hideAllView() {
        //default tool
        binding.recyclerViewTools.setVisibility(View.GONE);
        binding.constraintLayoutSave.setVisibility(View.GONE);

        //draw
        binding.constraintLayoutDraw.setVisibility(View.GONE);
        binding.constraintLayoutPaint.setVisibility(View.GONE);
        binding.constraintLayoutNeon.setVisibility(View.GONE);
        binding.constraintLayoutEraser.setVisibility(View.GONE);
//        binding.constraintLayoutConfirmSavePaint.setVisibility(View.GONE);
//        binding.constraintLayoutConfirmSaveNeon.setVisibility(View.GONE);
    }

    public void rvPrimaryToolShow() {
        hideAllView();
        binding.recyclerViewTools.setVisibility(View.VISIBLE);
        binding.constraintLayoutSave.setVisibility(View.VISIBLE);
    }

    public void drawShow() {
        hideAllView();
        binding.constraintLayoutDraw.setVisibility(View.VISIBLE);
        paintShow();
    }

    public void paintShow() {
        hideAllView();
        binding.constraintLayoutDraw.setVisibility(View.VISIBLE);
        binding.constraintLayoutPaint.setVisibility(View.VISIBLE);
//        binding.constraintLayoutConfirmSavePaint.setVisibility(View.VISIBLE);
    }

    public void neonShow() {
        hideAllView();
        binding.constraintLayoutDraw.setVisibility(View.VISIBLE);
        binding.constraintLayoutNeon.setVisibility(View.VISIBLE);
//        binding.constraintLayoutConfirmSavePaint.setVisibility(View.VISIBLE);
    }

    public void eraserShow() {
        hideAllView();
        binding.constraintLayoutDraw.setVisibility(View.VISIBLE);
        binding.constraintLayoutEraser.setVisibility(View.VISIBLE);
//        binding.constraintLayoutConfirmSavePaint.setVisibility(View.VISIBLE);
    }
}
