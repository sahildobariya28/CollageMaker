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

        //tools
        binding.constraintLayoutFilter.setVisibility(View.GONE);
        binding.constraintLayoutAdjust.setVisibility(View.GONE);
        binding.constraintLayoutEffect.setVisibility(View.GONE);
        binding.constraintLayoutSticker.setVisibility(View.GONE);
        binding.constraintLayoutBlur.setVisibility(View.GONE);
        binding.constraintLayoutSplash.setVisibility(View.GONE);
        binding.constraintLayoutDraw.setVisibility(View.GONE);
        binding.constraintLayoutConfirmText.setVisibility(View.GONE);

        //draw
        binding.constraintLayoutPaint.setVisibility(View.GONE);
        binding.constraintLayoutNeon.setVisibility(View.GONE);
        binding.constraintLayoutEraser.setVisibility(View.GONE);

        //filter
        binding.recyclerViewFilterAll.setVisibility(View.GONE);
        binding.recyclerViewFilterBW.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        binding.recyclerViewFilterSmooth.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        binding.recyclerViewFilterLegacy.setVisibility(View.GONE);

        binding.viewAll.setVisibility(View.INVISIBLE);
        binding.viewSmooth.setVisibility(View.INVISIBLE);
        binding.viewBW.setVisibility(View.INVISIBLE);
        binding.viewVintage.setVisibility(View.INVISIBLE);
        binding.viewCold.setVisibility(View.INVISIBLE);
        binding.viewWarm.setVisibility(View.INVISIBLE);
        binding.viewLegacy.setVisibility(View.INVISIBLE);

        //effect
        binding.recyclerViewHardmix.setVisibility(View.GONE);
        binding.recyclerViewDodge.setVisibility(View.GONE);
        binding.recyclerViewOverlay.setVisibility(View.GONE);
        binding.recyclerViewDivide.setVisibility(View.GONE);
        binding.recyclerViewBurn.setVisibility(View.GONE);

        binding.viewEffect.setVisibility(View.INVISIBLE);
        binding.viewHardmix.setVisibility(View.INVISIBLE);
        binding.viewDodge.setVisibility(View.INVISIBLE);
        binding.viewburn.setVisibility(View.INVISIBLE);
        binding.viewDivide.setVisibility(View.INVISIBLE);

    }


    public void rvPrimaryToolShow() {
        hideAllView();
        binding.recyclerViewTools.setVisibility(View.VISIBLE);
        binding.constraintLayoutSave.setVisibility(View.VISIBLE);
    }

    public void filterShow() {
        hideAllView();
        binding.constraintLayoutFilter.setVisibility(View.VISIBLE);
        allFilterShow();
    }

    public void allFilterShow() {
        hideAllView();
        binding.constraintLayoutFilter.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterAll.setVisibility(View.VISIBLE);
        binding.viewAll.setVisibility(View.VISIBLE);
    }

    public void filterSmoothShow() {
        hideAllView();
        binding.constraintLayoutFilter.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterSmooth.setVisibility(View.VISIBLE);
        binding.viewSmooth.setVisibility(View.VISIBLE);
    }

    public void filterBWShow() {
        hideAllView();
        binding.constraintLayoutFilter.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterBW.setVisibility(View.VISIBLE);
        binding.viewBW.setVisibility(View.VISIBLE);
    }

    public void filterVintageShow() {
        hideAllView();
        binding.constraintLayoutFilter.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterVintage.setVisibility(View.VISIBLE);
        binding.viewVintage.setVisibility(View.VISIBLE);
    }

    public void filterColdShow() {
        hideAllView();
        binding.constraintLayoutFilter.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterCold.setVisibility(View.VISIBLE);
        binding.viewCold.setVisibility(View.VISIBLE);
    }

    public void filterWarmShow() {
        hideAllView();
        binding.constraintLayoutFilter.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterWarm.setVisibility(View.VISIBLE);
        binding.viewWarm.setVisibility(View.VISIBLE);
    }

    public void filterLegacyShow() {
        hideAllView();
        binding.constraintLayoutFilter.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterLegacy.setVisibility(View.VISIBLE);
        binding.viewLegacy.setVisibility(View.VISIBLE);
    }

    public void adjustShow() {
        hideAllView();
        binding.constraintLayoutAdjust.setVisibility(View.VISIBLE);
    }

    public void overLayShow() {
        hideAllView();
        binding.constraintLayoutEffect.setVisibility(View.VISIBLE);
        overlayEffectShow();
    }

    public void overlayEffectShow() {
        hideAllView();
        binding.constraintLayoutEffect.setVisibility(View.VISIBLE);
        binding.recyclerViewOverlay.setVisibility(View.VISIBLE);
        binding.viewEffect.setVisibility(View.VISIBLE);
    }

    public void overLayHardMixShow() {
        hideAllView();
        binding.constraintLayoutEffect.setVisibility(View.VISIBLE);
        binding.recyclerViewHardmix.setVisibility(View.VISIBLE);
        binding.viewHardmix.setVisibility(View.VISIBLE);
    }

    public void overLayDodgeShow() {
        hideAllView();
        binding.constraintLayoutEffect.setVisibility(View.VISIBLE);
        binding.recyclerViewDodge.setVisibility(View.VISIBLE);
        binding.viewDodge.setVisibility(View.VISIBLE);
    }

    public void overLayBurnShow() {
        hideAllView();
        binding.constraintLayoutEffect.setVisibility(View.VISIBLE);
        binding.recyclerViewBurn.setVisibility(View.VISIBLE);
        binding.viewburn.setVisibility(View.VISIBLE);
    }

    public void overLayDivideShow() {
        hideAllView();
        binding.constraintLayoutEffect.setVisibility(View.VISIBLE);
        binding.recyclerViewDivide.setVisibility(View.VISIBLE);
        binding.viewDivide.setVisibility(View.VISIBLE);
    }

    public void stickerShow() {
        hideAllView();
        binding.constraintLayoutSticker.setVisibility(View.VISIBLE);
    }

    public void blurShow() {
        hideAllView();
        binding.constraintLayoutBlur.setVisibility(View.VISIBLE);
    }

    public void splashShow() {
        hideAllView();
        binding.constraintLayoutSplash.setVisibility(View.VISIBLE);
    }

    public void drawShow() {
        hideAllView();
        binding.constraintLayoutDraw.setVisibility(View.VISIBLE);
        paintShow();
    }

    public void textShow() {
        hideAllView();
        binding.constraintLayoutConfirmText.setVisibility(View.VISIBLE);
    }

    public void paintShow() {
        hideAllView();
        binding.constraintLayoutDraw.setVisibility(View.VISIBLE);
        binding.constraintLayoutPaint.setVisibility(View.VISIBLE);
    }

    public void neonShow() {
        hideAllView();
        binding.constraintLayoutDraw.setVisibility(View.VISIBLE);
        binding.constraintLayoutNeon.setVisibility(View.VISIBLE);
    }

    public void eraserShow() {
        hideAllView();
        binding.constraintLayoutDraw.setVisibility(View.VISIBLE);
        binding.constraintLayoutEraser.setVisibility(View.VISIBLE);
    }
}
