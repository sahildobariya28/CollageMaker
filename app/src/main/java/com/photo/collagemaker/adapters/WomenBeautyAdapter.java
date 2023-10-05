package com.photo.collagemaker.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.photo.collagemaker.R;
import com.photo.collagemaker.assets.WomenBeautyAssets;

import java.util.List;

public class WomenBeautyAdapter extends RecyclerView.Adapter<WomenBeautyAdapter.ViewHolder> {

    public Context context;
    public int screenWidth;
    public OnClickBeautyItemListener onClickBeautyItemListener;
    public List<String> stringList;

    public interface OnClickBeautyItemListener {
        void addSticker(Bitmap bitmap);
    }

    public WomenBeautyAdapter(Context context, List<String> list, int i, OnClickBeautyItemListener onClickBeautyItemListener) {
        this.context = context;
        this.stringList = list;
        this.screenWidth = i;
        this.onClickBeautyItemListener = onClickBeautyItemListener;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sticker, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Bitmap bitmap = WomenBeautyAssets.mBitmapAssets(context, stringList.get(i));
        Glide.with(context).load(bitmap).into(viewHolder.sticker);
    }

    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView sticker;

        public ViewHolder(View view) {
            super(view);
            sticker = view.findViewById(R.id.image_view_item_sticker);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            onClickBeautyItemListener.addSticker(WomenBeautyAssets.mBitmapAssets(context,  stringList.get(getAdapterPosition())));
        }
    }
}
