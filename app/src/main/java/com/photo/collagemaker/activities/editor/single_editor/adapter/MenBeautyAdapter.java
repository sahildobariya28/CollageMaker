package com.photo.collagemaker.activities.editor.single_editor.adapter;

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
import com.photo.collagemaker.assets.MenBeautyAssets;

import java.util.List;

public class MenBeautyAdapter extends RecyclerView.Adapter<MenBeautyAdapter.ViewHolder> {

    public Context context;
    public int screenWidth;
    public OnClickBeautyItemListener onClickBeautyItemListener;
    public List<String> stickers;

    public interface OnClickBeautyItemListener {
        void addSticker(Bitmap bitmap);
    }

    public MenBeautyAdapter(Context context2, List<String> list, int i, OnClickBeautyItemListener onClickBeautyItemListener) {
        this.context = context2;
        this.stickers = list;
        this.screenWidth = i;
        this.onClickBeautyItemListener = onClickBeautyItemListener;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sticker, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Bitmap bitmap = MenBeautyAssets.mBitmapAssets(context, stickers.get(i));
        Glide.with(context).load(bitmap).into(viewHolder.sticker);
    }

    public int getItemCount() {
        return stickers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView sticker;

        public ViewHolder(View view) {
            super(view);
            sticker = view.findViewById(R.id.image_view_item_sticker);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            onClickBeautyItemListener.addSticker(MenBeautyAssets.mBitmapAssets(context, stickers.get(getAdapterPosition())));
        }
    }
}
