package com.photo.collagemaker.activities.material;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.material.material_assert.MaterialModel;

import java.util.ArrayList;

public class MaterialAdapter extends ArrayAdapter<MaterialModel> {
    Context context;
    int layoutResourceId;
    ArrayList<MaterialModel> data;
    OnCollageMaterial onCollageMaterial;

    public interface OnCollageMaterial {
        void OnItemListCollageMaterialSelect(MaterialModel imageModel, int position);
    }

    static class RecordHolder {

        ImageView imgCollageMaterial;

        RecordHolder() {
        }
    }


    public MaterialAdapter(@NonNull Context context, int layoutResourceId, ArrayList<MaterialModel> arrayList, OnCollageMaterial onCollageMaterial) {
        super(context, layoutResourceId, arrayList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = arrayList;
        this.onCollageMaterial = onCollageMaterial;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        MaterialModel materialModel = data.get(i);
        RecordHolder recordHolder;
        if (view == null) {
            view = ((Activity) context).getLayoutInflater().inflate(layoutResourceId, viewGroup, false);
            recordHolder = new RecordHolder();
            recordHolder.imgCollageMaterial = view.findViewById(R.id.imgCollageMaterial);

            recordHolder.imgCollageMaterial.setImageResource(materialModel.getDrawableId());

        }
        view.setOnClickListener(view1 -> {
            if (onCollageMaterial != null) {
                onCollageMaterial.OnItemListCollageMaterialSelect(materialModel, i);
                notifyDataSetChanged();

            }
        });
        return view;
    }
}
