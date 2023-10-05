package com.photo.collagemaker.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.photo.collagemaker.R;
import com.photo.collagemaker.interfac.OnSelectedPhoto;
import com.photo.collagemaker.model.ImageModel;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class SelectedPhotoAdapter extends RecyclerView.Adapter<SelectedPhotoAdapter.ViewHolder> {

    Context context;
    ArrayList<ImageModel> listPhoto;
    OnSelectedPhoto onSelectedPhoto;

    public SelectedPhotoAdapter(Context context, ArrayList<ImageModel> listPhoto, OnSelectedPhoto onSelectedPhoto){
        this.context = context;
        this.listPhoto = listPhoto;
        this.onSelectedPhoto = onSelectedPhoto;
    }

    @NonNull
    @Override
    public SelectedPhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album_selected, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedPhotoAdapter.ViewHolder holder, int position) {
        ImageModel imageModel = listPhoto.get(position);

        Glide.with(context).load(imageModel.getPathFile()).placeholder(R.drawable.image_show).into(holder.image_view_item);
        holder.image_view_remove.setOnClickListener(view -> {
            onSelectedPhoto.OnSelectedItemDelete(imageModel);
        });
    }



    @Override
    public int getItemCount() {
        return listPhoto.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view_remove;
        ImageView image_view_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_view_remove = itemView.findViewById(R.id.image_view_remove);
            image_view_item = itemView.findViewById(R.id.image_view_item);
        }
    }
}
