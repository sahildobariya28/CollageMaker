package com.photo.collagemaker.utils;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.layer.slant.SlantLayoutHelper;
import com.photo.collagemaker.layer.straight.layout.StraightLayoutHelper;

import java.util.ArrayList;
import java.util.List;

public class CollageUtils {

    private CollageUtils() {}

    public static List<QueShotLayout> getCollageLayouts(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(SlantLayoutHelper.getAllThemeLayout(i));
        arrayList.addAll(StraightLayoutHelper.getAllThemeLayout(i));
        return arrayList;
    }
}
