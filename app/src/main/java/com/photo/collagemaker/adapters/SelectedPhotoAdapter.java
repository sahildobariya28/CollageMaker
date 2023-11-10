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
import com.photo.collagemaker.databinding.ItemAlbumSelectedBinding;
import com.photo.collagemaker.databinding.ItemSquareGridBinding;
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
        ItemAlbumSelectedBinding binding = ItemAlbumSelectedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedPhotoAdapter.ViewHolder holder, int position) {
        ImageModel imageModel = listPhoto.get(position);

        Glide.with(context).load(imageModel.getPathFile()).placeholder(R.drawable.image_show).into(holder.binding.imageViewItem);
        holder.binding.imageViewRemove.setOnClickListener(view -> {
            onSelectedPhoto.OnSelectedItemDelete(imageModel);
        });
    }



    @Override
    public int getItemCount() {
        return listPhoto.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemAlbumSelectedBinding binding;
        public ViewHolder(ItemAlbumSelectedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
