package com.photo.collagemaker.activities.multifit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.activities.PhotoShareActivity;
import com.photo.collagemaker.activities.editor.collage_editor.adapter.BackgroundGridAdapter;
import com.photo.collagemaker.databinding.CollageEditorBinding;
import com.photo.collagemaker.utils.FilterUtils;
import com.photo.collagemaker.utils.SaveFileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MultiFitAdapter extends RecyclerView.Adapter<MultiFitAdapter.ViewHolder> {

    MultiFitViewModel viewModel;
    boolean isSave = false;


    public MultiFitAdapter(MultiFitViewModel multiFitViewModel) {
        this.viewModel = multiFitViewModel;

    }

    @NonNull
    @Override
    public MultiFitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CollageEditorBinding binding = CollageEditorBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiFitAdapter.ViewHolder holder, int position) {
        BackgroundGridAdapter.SquareView squareView = viewModel.imageModelsList.get(position);

//        Picasso.get().load("file:///" + imageList.get(position)).into(holder.binding.collageView);


        if (squareView.isColor) {
            holder.binding.collageView.setBackgroundColor(squareView.drawableId);
            holder.binding.collageView.post(() -> {
                holder.binding.collageView.setImageDrawable(viewModel.imageDrawableList.get(position));
                holder.binding.collageView.post(() -> {
                    viewModel.multiFitImageList.add(holder.binding.collageView);
                });
            });

        } else if (squareView.drawable != null) {
            AsyncTask<Void, Bitmap, Bitmap> asyncTask = new AsyncTask<Void, Bitmap, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voidArr) {
                    return FilterUtils.getBlurImageFromBitmap(((BitmapDrawable) squareView.drawable).getBitmap(), 5.0f);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    holder.binding.collageView.setBackground(new BitmapDrawable(viewModel.activity.getResources(), bitmap));
                    holder.binding.collageView.post(() -> {
                        holder.binding.collageView.setImageDrawable(viewModel.imageDrawableList.get(position));
                        holder.binding.collageView.post(() -> {
                            viewModel.multiFitImageList.add(holder.binding.collageView);
                        });
                    });
                }
            };
            asyncTask.execute();
        } else {
            holder.binding.collageView.setBackgroundResource(squareView.drawableId);
            holder.binding.collageView.post(() -> {
                holder.binding.collageView.setImageDrawable(viewModel.imageDrawableList.get(position));
                holder.binding.collageView.post(() -> {
                    viewModel.multiFitImageList.add(holder.binding.collageView);
                });
            });
        }
//        if (isSave) {
//            holder.binding.collageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    // Remove the listener to prevent multiple calls
//                    holder.binding.collageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//                    // Now capture the bitmap after the view is fully loaded
//                    Bitmap bitmap = getBitmapFromView(holder.binding.collageView);
//                    try {
//                        File image = SaveFileUtils.saveBitmapFileCollage(viewModel.activity, bitmap, new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()));
//
//                        if (position == (viewModel.imageList.size() - 1)) {
//                            Intent intent = new Intent(viewModel.activity, PhotoShareActivity.class);
//                            intent.putExtra("path", image.getAbsolutePath());
//                            viewModel.activity.startActivity(intent);
//                        }
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            });
//        }
//        if (isSave) {
//            holder.binding.collageView.post(() -> {
//                Bitmap savedBitmap = getBitmapFromView(holder.binding.collageView);
//                try {
//                    File image = SaveFileUtils.saveBitmapFileCollage(viewModel.activity, savedBitmap, new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()));
//
//                    if (position == (viewModel.imageList.size() - 1)) {
//                        Intent intent = new Intent(viewModel.activity, PhotoShareActivity.class);
//                        intent.putExtra("path", image.getAbsolutePath());
//                        viewModel.activity.startActivity(intent);
//                    }
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        }

    }


    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    @Override
    public int getItemCount() {
        return viewModel.imageDrawableList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CollageEditorBinding binding;

        public ViewHolder(CollageEditorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
