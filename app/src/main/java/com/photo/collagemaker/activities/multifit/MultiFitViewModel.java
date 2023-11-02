package com.photo.collagemaker.activities.multifit;

import android.app.Activity;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.photo.collagemaker.activities.editor.collage_editor.adapter.BackgroundGridAdapter;

import java.util.ArrayList;

public class MultiFitViewModel extends ViewModel {

    Activity activity;
    ArrayList<String> imageList = new ArrayList<>();
    ArrayList<BackgroundGridAdapter.SquareView> imageModelsList = new ArrayList<>();

    ArrayList<ImageView> multiFitImageList = new ArrayList<>();


    public MultiFitViewModel(Activity activity) {
        this.activity = activity;
    }


}
