package com.photo.collagemaker.activities.multifit;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.photo.collagemaker.activities.editor.collage_editor.adapter.BackgroundGridAdapter;
import com.photo.collagemaker.databinding.ActivityGridBinding;
import com.photo.collagemaker.databinding.ActivityMultiFitBinding;

import java.util.ArrayList;

public class MultiFitViewModel extends ViewModel {

    Activity activity;
    ActivityMultiFitBinding binding;

    ArrayList<String> imageList = new ArrayList<>();
    ArrayList<BitmapDrawable> imageDrawableList = new ArrayList<>();
    ArrayList<BackgroundGridAdapter.SquareView> imageModelsList = new ArrayList<>();

    ArrayList<ImageView> multiFitImageList = new ArrayList<>();


    public MultiFitViewModel(Activity activity, ActivityMultiFitBinding binding) {
        this.activity = activity;
        this.binding = binding;
    }

    public void hideAllView() {
        //save
        binding.constraintSaveControl.setVisibility(View.GONE);
//        binding.backgroundContainer.getRoot().setVisibility(View.GONE);

        //filter
        binding.constraintLayoutFilterLayout.setVisibility(View.GONE);
        binding.recyclerViewFilterAll.setVisibility(View.GONE);
        binding.recyclerViewFilterBW.setVisibility(View.GONE);
        binding.recyclerViewFilterVintage.setVisibility(View.GONE);
        binding.recyclerViewFilterSmooth.setVisibility(View.GONE);
        binding.recyclerViewFilterCold.setVisibility(View.GONE);
        binding.recyclerViewFilterWarm.setVisibility(View.GONE);
        binding.recyclerViewFilterLegacy.setVisibility(View.GONE);

//      background
        binding.backgroundContainer.getRoot().setVisibility(View.GONE);


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
        binding.backgroundContainer.getRoot().setVisibility(View.VISIBLE);
        binding.constraintSaveControl.setVisibility(View.VISIBLE);
    }

    public void filterAllShow() {
        hideAllView();
        binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterAll.setVisibility(View.VISIBLE);
        binding.viewAll.setVisibility(View.VISIBLE);
    }

    public void filterBWShow() {
        hideAllView();
        binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterBW.setVisibility(View.VISIBLE);
        binding.viewBW.setVisibility(View.VISIBLE);
    }

    public void filterVintageShow() {
        hideAllView();
        binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterVintage.setVisibility(View.VISIBLE);
        binding.viewVintage.setVisibility(View.VISIBLE);
    }

    public void filterSmoothShow() {
        hideAllView();
        binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterSmooth.setVisibility(View.VISIBLE);
        binding.viewSmooth.setVisibility(View.VISIBLE);
    }

    public void filterColdShow() {
        hideAllView();
        binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterCold.setVisibility(View.VISIBLE);
        binding.viewCold.setVisibility(View.VISIBLE);
    }

    public void filterWarmShow() {
        hideAllView();
        binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterWarm.setVisibility(View.VISIBLE);
        binding.viewWarm.setVisibility(View.VISIBLE);
    }

    public void filterLegacyShow() {
        hideAllView();
        binding.constraintLayoutFilterLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewFilterLegacy.setVisibility(View.VISIBLE);
        binding.viewLegacy.setVisibility(View.VISIBLE);
    }


}
