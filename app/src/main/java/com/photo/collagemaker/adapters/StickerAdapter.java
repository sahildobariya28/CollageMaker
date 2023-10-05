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
import com.photo.collagemaker.assets.StickersAsset;

import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

    public Context context;
    public int screenWidth;
    public OnClickSplashListener onClickSplashListener;

    public List<String> stringList;

    public interface OnClickSplashListener {
        void addSticker(Bitmap bitmap);
    }

    public StickerAdapter(Context context2, List<String> list, int i, OnClickSplashListener onClickSplashListener) {
        this.context = context2;
        this.stringList = list;
        this.screenWidth = i;
        this.onClickSplashListener = onClickSplashListener;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sticker, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Bitmap bitmap = StickersAsset.loadBitmapFromAssets(context, stringList.get(i));
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
            onClickSplashListener.addSticker(StickersAsset.loadBitmapFromAssets(context, stringList.get(getAdapterPosition())));
        }
    }
}
