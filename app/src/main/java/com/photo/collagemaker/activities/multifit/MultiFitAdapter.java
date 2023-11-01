package com.photo.collagemaker.activities.multifit;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.activities.editor.collage_editor.adapter.BackgroundGridAdapter;
import com.photo.collagemaker.databinding.CollageEditorBinding;
import com.photo.collagemaker.databinding.ItemSquareBinding;
import com.photo.collagemaker.utils.FilterUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MultiFitAdapter extends RecyclerView.Adapter<MultiFitAdapter.ViewHolder> {
    ArrayList<BackgroundGridAdapter.SquareView> imageBackgroundList = new ArrayList<>();
    ArrayList<String> imageList = new ArrayList<>();
    Activity activity;

    public MultiFitAdapter(Activity activity,ArrayList<String> imageList, ArrayList<BackgroundGridAdapter.SquareView> imageBackgroundList) {
        this.activity = activity;
        this.imageList = imageList;
        this.imageBackgroundList = imageBackgroundList;
    }

    @NonNull
    @Override
    public MultiFitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CollageEditorBinding binding = CollageEditorBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiFitAdapter.ViewHolder holder, int position) {
        BackgroundGridAdapter.SquareView squareView = imageBackgroundList.get(position);
        Picasso.get().load("file:///" + imageList.get(position)).into(holder.binding.collageView);


        if (squareView.isColor) {
            holder.binding.collageView.setBackgroundColor(squareView.drawableId);
        } else if (squareView.drawable != null) {
            AsyncTask<Void, Bitmap, Bitmap> asyncTask = new AsyncTask<Void, Bitmap, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voidArr) {
                    return FilterUtils.getBlurImageFromBitmap(((BitmapDrawable) squareView.drawable).getBitmap(), 5.0f);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    holder.binding.collageView.setBackground(new BitmapDrawable(activity.getResources(), bitmap));
                }
            };
            asyncTask.execute();
        } else {
            holder.binding.collageView.setBackgroundResource(squareView.drawableId);
        }
    }



    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CollageEditorBinding binding;

        public ViewHolder(CollageEditorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
