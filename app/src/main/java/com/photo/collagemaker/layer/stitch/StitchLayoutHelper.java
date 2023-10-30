package com.photo.collagemaker.layer.stitch;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.layer.slant.FourSlantLayout;
import com.photo.collagemaker.layer.slant.OneSlantLayout;
import com.photo.collagemaker.layer.slant.SevenSlantLayout;
import com.photo.collagemaker.layer.slant.SixSlantLayout;
import com.photo.collagemaker.layer.slant.ThreeSlantLayout;
import com.photo.collagemaker.layer.slant.TwoSlantLayout;

import java.util.ArrayList;
import java.util.List;

public class StitchLayoutHelper {
    private StitchLayoutHelper() {
    }

    public static List<QueShotLayout> getAllThemeLayout(int imageListSize, boolean isVertical) {
        ArrayList arrayList = new ArrayList();
        int i2 = 0;

        while (i2 < 1) {
            arrayList.add(new DynamicStraightLayout(i2, isVertical, imageListSize));
            i2++;
        }
        return arrayList;
    }
}
