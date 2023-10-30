package com.photo.collagemaker.layer.straight.layout;

import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.layer.straight.EighteenStraightLayout;
import com.photo.collagemaker.layer.straight.FifteenStraightLayout;
import com.photo.collagemaker.layer.straight.FourteenStraightLayout;
import com.photo.collagemaker.layer.straight.NineteenStraightLayout;
import com.photo.collagemaker.layer.straight.SeventeenStraightLayout;
import com.photo.collagemaker.layer.straight.SixteenStraightLayout;
import com.photo.collagemaker.layer.straight.ThirteenStraightLayout;
import com.photo.collagemaker.layer.straight.TwelveStraightLayout;
import com.photo.collagemaker.layer.straight.EightStraightLayout;
import com.photo.collagemaker.layer.straight.ElevanStraightLayout;
import com.photo.collagemaker.layer.straight.FiveStraightLayout;
import com.photo.collagemaker.layer.straight.FourStraightLayout;
import com.photo.collagemaker.layer.straight.NineStraightLayout;
import com.photo.collagemaker.layer.straight.OneStraightLayout;
import com.photo.collagemaker.layer.straight.SevenStraightLayout;
import com.photo.collagemaker.layer.straight.SixStraightLayout;
import com.photo.collagemaker.layer.straight.TenStraightLayout;
import com.photo.collagemaker.layer.straight.ThreeStraightLayout;
import com.photo.collagemaker.layer.straight.TwentyStraightLayout;
import com.photo.collagemaker.layer.straight.TwoStraightLayout;

import java.util.ArrayList;
import java.util.List;

public class StraightLayoutHelper {
    private StraightLayoutHelper() {
    }

    public static List<QueShotLayout> getAllThemeLayout(int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        switch (i) {
            case 1:
                while (i2 < 6) {
                    arrayList.add(new OneStraightLayout(i2));
                    i2++;
                }
                break;
            case 2:
                while (i2 < 8) {
                    arrayList.add(new TwoStraightLayout(i2));
                    i2++;
                }
                break;
            case 3:
                while (i2 < 6) {
                    arrayList.add(new ThreeStraightLayout(i2));
                    i2++;
                }
                break;
            case 4:
                while (i2 < 8) {
                    arrayList.add(new FourStraightLayout(i2));
                    i2++;
                }
                break;
            case 5:
                while (i2 < 17) {
                    arrayList.add(new FiveStraightLayout(i2));
                    i2++;
                }
                break;
            case 6:
                while (i2 < 12) {
                    arrayList.add(new SixStraightLayout(i2));
                    i2++;
                }
                break;
            case 7:
                while (i2 < 9) {
                    arrayList.add(new SevenStraightLayout(i2));
                    i2++;
                }
                break;
            case 8:
                while (i2 < 11) {
                    arrayList.add(new EightStraightLayout(i2));
                    i2++;
                }
                break;
            case 9:
                while (i2 < 8) {
                    arrayList.add(new NineStraightLayout(i2));
                    i2++;
                }
                break;
            case 10:
                while (i2 < 6) {
                    arrayList.add(new TenStraightLayout(i2));
                    i2++;
                }
                break;
            case 11:
                while (i2 < 5) {
                    arrayList.add(new ElevanStraightLayout(i2));
                    i2++;
                }
                break;
            case 12:
                while (i2 < 9) {
                    arrayList.add(new TwelveStraightLayout(i2));
                    i2++;
                }
                break;
            case 13:
                while (i2 < 6) {
                    arrayList.add(new ThirteenStraightLayout(i2));
                    i2++;
                }
                break;
            case 14:
                while (i2 < 6) {
                    arrayList.add(new FourteenStraightLayout(i2));
                    i2++;
                }
            case 15:
                while (i2 < 6) {
                    arrayList.add(new FifteenStraightLayout(i2));
                    i2++;
                }
                break;
            case 16:
                while (i2 < 6) {
                    arrayList.add(new SixteenStraightLayout(i2));
                    i2++;
                }
                break;
            case 17:
                while (i2 < 6) {
                    arrayList.add(new SeventeenStraightLayout(i2));
                    i2++;
                }
                break;
            case 18:
                while (i2 < 7) {
                    arrayList.add(new EighteenStraightLayout(i2));
                    i2++;
                }
                break;
            case 19:
                while (i2 < 6) {
                    arrayList.add(new NineteenStraightLayout(i2));
                    i2++;
                }
                break;
            case 20:
                while (i2 < 6) {
                    arrayList.add(new TwentyStraightLayout(i2));
                    i2++;
                }
                break;
        }
        return arrayList;
    }
}
