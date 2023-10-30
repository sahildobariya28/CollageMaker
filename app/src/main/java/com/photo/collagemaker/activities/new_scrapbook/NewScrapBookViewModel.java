package com.photo.collagemaker.activities.new_scrapbook;

import android.app.Activity;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.photo.collagemaker.databinding.ActivityGridBinding;
import com.photo.collagemaker.databinding.ActivityNewScrapBookBinding;

public class NewScrapBookViewModel extends ViewModel {

    Activity activity;
    ActivityNewScrapBookBinding binding;

    public NewScrapBookViewModel(Activity activity, ActivityNewScrapBookBinding binding) {
        this.activity = activity;
        this.binding = binding;
    }

    public void hideAllView() {
        //save
        binding.constraintSaveControl.setVisibility(View.GONE);
        binding.constraintLayoutConfirmSaveAddImage.setVisibility(View.GONE);
        binding.constraintLayoutConfirmSaveFilter.setVisibility(View.GONE);

        //Tools list
        binding.rvPrimaryTool.setVisibility(View.GONE);
        binding.rvSecondaryToolContainer.setVisibility(View.GONE);

        //tools
        binding.constraintLayoutDraw.setVisibility(View.GONE);
        binding.constraintLayoutPaint.setVisibility(View.GONE);
        binding.constraintLayoutNeon.setVisibility(View.GONE);
        binding.constraintLayoutEraser.setVisibility(View.GONE);
        binding.constraintLayoutConfirmText.setVisibility(View.GONE);
        binding.relativeLayoutAddText.setVisibility(View.GONE);
        binding.constrantLayoutChangeLayout.setVisibility(View.GONE);
        binding.constraintLayoutSticker.setVisibility(View.GONE);
        binding.constraintLayoutFilterLayout.setVisibility(View.GONE);

        //collage
        binding.recyclerViewRatio.setVisibility(View.GONE);
        binding.linearLayoutPadding.setVisibility(View.GONE);
        binding.backgroundContainer.getRoot().setVisibility(View.GONE);

        //filter
        binding.recyclerViewFilterAll.setVisibility(View.GONE);
        binding.recyclerViewFilterBW.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        binding.recyclerViewFilterSmooth.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        binding.recyclerViewFilterLegacy.setVisibility(View.GONE);

        //background
        binding.backgroundContainer.backgroundTools.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewBlur.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewColor.setVisibility(View.GONE);
        binding.backgroundContainer.recyclerViewGradient.setVisibility(View.GONE);
        binding.colorPickerView.setVisibility(View.GONE);

        //indicator
        binding.indicatorRatio.setVisibility(View.INVISIBLE);
        binding.indicatorBorder.setVisibility(View.INVISIBLE);
        binding.indicatorBackground.setVisibility(View.INVISIBLE);

        binding.viewAll.setVisibility(View.INVISIBLE);
        binding.viewBW.setVisibility(View.INVISIBLE);
        binding.viewVintage.setVisibility(View.INVISIBLE);
        binding.viewSmooth.setVisibility(View.INVISIBLE);
        binding.viewCold.setVisibility(View.INVISIBLE);
        binding.viewWarm.setVisibility(View.INVISIBLE);
        binding.viewLegacy.setVisibility(View.INVISIBLE);



    }

    public void rvPrimaryToolShow() {
        hideAllView();
        binding.rvPrimaryTool.setVisibility(View.VISIBLE);
        binding.constraintSaveControl.setVisibility(View.VISIBLE);
    }

    public void rvSecondaryToolShow() {
        hideAllView();
        binding.rvSecondaryToolContainer.setVisibility(View.VISIBLE);
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

    public void textShow() {
        hideAllView();
        binding.constraintLayoutConfirmText.setVisibility(View.VISIBLE);
        binding.relativeLayoutAddText.setVisibility(View.VISIBLE);
    }

    public void ratioShow() {
        hideAllView();
        binding.constrantLayoutChangeLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewRatio.setVisibility(View.VISIBLE);
        binding.indicatorRatio.setVisibility(View.VISIBLE);
    }

    public void borderShow() {
        hideAllView();
        binding.constrantLayoutChangeLayout.setVisibility(View.VISIBLE);
        binding.linearLayoutPadding.setVisibility(View.VISIBLE);
        binding.indicatorBorder.setVisibility(View.VISIBLE);
    }

    public void bgShow() {
        hideAllView();
        binding.constrantLayoutChangeLayout.setVisibility(View.VISIBLE);
        binding.backgroundContainer.getRoot().setVisibility(View.VISIBLE);
        binding.indicatorBackground.setVisibility(View.VISIBLE);
        binding.backgroundContainer.backgroundTools.setVisibility(View.VISIBLE);
    }

    public void filterAllShow() {
        hideAllView();
        binding.constraintLayoutConfirmSaveFilter.setVisibility(View.VISIBLE);
        binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterAll.setVisibility(View.VISIBLE);
        binding.viewAll.setVisibility(View.VISIBLE);
    }

    public void filterBWShow() {
        hideAllView();
        binding.constraintLayoutConfirmSaveFilter.setVisibility(View.VISIBLE);
        binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterBW.setVisibility(View.VISIBLE);
        binding.viewBW.setVisibility(View.VISIBLE);
    }

    public void filterVintageShow() {
        hideAllView();
        binding.constraintLayoutConfirmSaveFilter.setVisibility(View.VISIBLE);
        binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterVintage.setVisibility(View.VISIBLE);
        binding.viewVintage.setVisibility(View.VISIBLE);
    }

    public void filterSmoothShow() {
        hideAllView();
        binding.constraintLayoutConfirmSaveFilter.setVisibility(View.VISIBLE);
        binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterSmooth.setVisibility(View.VISIBLE);
        binding.viewSmooth.setVisibility(View.VISIBLE);
    }

    public void filterColdShow() {
        hideAllView();
        binding.constraintLayoutConfirmSaveFilter.setVisibility(View.VISIBLE);
        binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterCold.setVisibility(View.VISIBLE);
        binding.viewCold.setVisibility(View.VISIBLE);
    }

    public void filterWarmShow() {
        hideAllView();
        binding.constraintLayoutConfirmSaveFilter.setVisibility(View.VISIBLE);
        binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterWarm.setVisibility(View.VISIBLE);
        binding.viewWarm.setVisibility(View.VISIBLE);
    }

    public void filterLegacyShow() {
        hideAllView();
        binding.constraintLayoutConfirmSaveFilter.setVisibility(View.VISIBLE);
        binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterLegacy.setVisibility(View.VISIBLE);
        binding.viewLegacy.setVisibility(View.VISIBLE);
    }

    public void stickerShow() {
        hideAllView();
        binding.constraintLayoutSticker.setVisibility(View.VISIBLE);
        binding.linearLayoutWrapperStickerList.setVisibility(View.VISIBLE);
    }

    public void backgroundToolShow() {
        hideAllView();
        binding.constrantLayoutChangeLayout.setVisibility(View.VISIBLE);
        binding.backgroundContainer.getRoot().setVisibility(View.VISIBLE);
        binding.indicatorBackground.setVisibility(View.VISIBLE);
        binding.backgroundContainer.backgroundTools.setVisibility(View.VISIBLE);
    }

    public void bgBlurShow() {
        hideAllView();
        binding.backgroundContainer.getRoot().setVisibility(View.VISIBLE);
        binding.backgroundContainer.recyclerViewBlur.setVisibility(View.VISIBLE);
    }

    public void bgColorSelectShow() {
        hideAllView();
        binding.backgroundContainer.getRoot().setVisibility(View.VISIBLE);
        binding.backgroundContainer.recyclerViewColor.setVisibility(View.VISIBLE);
    }

    public void bgGradientSelectShow() {
        hideAllView();
        binding.backgroundContainer.getRoot().setVisibility(View.VISIBLE);
        binding.backgroundContainer.recyclerViewGradient.setVisibility(View.VISIBLE);
    }

    public void colorPickerShow() {
        hideAllView();
        binding.backgroundContainer.getRoot().setVisibility(View.VISIBLE);
        binding.colorPickerView.setVisibility(View.VISIBLE);
    }

}
