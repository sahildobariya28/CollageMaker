package com.photo.collagemaker.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

public class DrawModel {
    private Context context;
    private int from;
    private boolean isLoadBitmap;
    private boolean keepExactPosition;
    private List<Bitmap> lstBitmaps;
    private List<Integer> lstIconWhenDrawing;
    private List<BrushDrawingView.Vector2> mPositions = new ArrayList(100);
    private int mainIcon;

    private int to;

    public DrawModel(int i, List<Integer> list, boolean z, Context context2) {
        this.mainIcon = i;
        this.lstIconWhenDrawing = list;
        this.keepExactPosition = z;
        this.context = context2;
    }

    public void clearBitmap() {
        if (lstBitmaps != null && !lstBitmaps.isEmpty()) {
            lstBitmaps.clear();
        }
    }

    public int getMainIcon() {
        return mainIcon;
    }


    public List<Integer> getLstIconWhenDrawing() {
        return lstIconWhenDrawing;
    }


    public boolean isLoadBitmap() {
        return isLoadBitmap;
    }

    public void setLoadBitmap(boolean z) {
        isLoadBitmap = z;
    }


    public List<BrushDrawingView.Vector2> getmPositions() {
        return mPositions;
    }

    public Bitmap getBitmapByIndex(int i) {
        if (lstBitmaps == null || lstBitmaps.isEmpty()) {
            init();
        }
        return lstBitmaps.get(i);
    }

    public void init() {
        if (lstBitmaps == null || lstBitmaps.isEmpty()) {
            lstBitmaps = new ArrayList();
            for (Integer intValue : lstIconWhenDrawing) {
                lstBitmaps.add(BitmapFactory.decodeResource(context.getResources(), intValue.intValue()));
            }
        }
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int i) {
        from = i;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int i) {
        to = i;
    }
}
