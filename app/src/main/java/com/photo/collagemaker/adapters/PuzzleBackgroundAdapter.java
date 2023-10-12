package com.photo.collagemaker.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.photo.collagemaker.R;
import com.photo.collagemaker.assets.BrushColorAsset;
import com.photo.collagemaker.databinding.ItemSquareBinding;

import java.util.ArrayList;
import java.util.List;

public class PuzzleBackgroundAdapter extends RecyclerView.Adapter<PuzzleBackgroundAdapter.ViewHolder> {

    public BackgroundChangeListener backgroundChangeListener;
    private Context context;

    public int selectedSquareIndex;

    public List<SquareView> squareViews = new ArrayList();

    public interface BackgroundChangeListener {
        void onBackgroundSelected(SquareView squareView);
    }

    public PuzzleBackgroundAdapter(Context context2, BackgroundChangeListener backgroundChangeListener2) {
        this.context = context2;
        this.backgroundChangeListener = backgroundChangeListener2;
        this.squareViews.add(new SquareView(Color.parseColor("#ffffff"), "White", true));
        this.squareViews.add(new SquareView(R.color.black, "Black"));
        List<String> lstColorForBrush = BrushColorAsset.listColorBrush();
        for (int i = 0; i < lstColorForBrush.size() - 2; i++) {
            squareViews.add(new SquareView(Color.parseColor(lstColorForBrush.get(i)), "", true));
        }
    }

    public PuzzleBackgroundAdapter(Context context2, BackgroundChangeListener backgroundChangeListener2, boolean z) {
        this.context = context2;
        this.backgroundChangeListener = backgroundChangeListener2;
        squareViews.add(new SquareView(R.drawable.gradient_1, "G1"));
        squareViews.add(new SquareView(R.drawable.gradient_2, "G2"));
        squareViews.add(new SquareView(R.drawable.gradient_3, "G3"));
        squareViews.add(new SquareView(R.drawable.gradient_4, "G4"));
        squareViews.add(new SquareView(R.drawable.gradient_5, "G5"));
        squareViews.add(new SquareView(R.drawable.gradient_11, "G11"));
        squareViews.add(new SquareView(R.drawable.gradient_10, "G10"));
        squareViews.add(new SquareView(R.drawable.gradient_6, "G6"));
        squareViews.add(new SquareView(R.drawable.gradient_7, "G7"));
        squareViews.add(new SquareView(R.drawable.gradient_13, "G13"));
        squareViews.add(new SquareView(R.drawable.gradient_14, "G14"));
        squareViews.add(new SquareView(R.drawable.gradient_16, "G16"));
        squareViews.add(new SquareView(R.drawable.gradient_17, "G17"));
        squareViews.add(new SquareView(R.drawable.gradient_18, "G18"));
    }

    public PuzzleBackgroundAdapter(Context context2, BackgroundChangeListener backgroundChangeListener2, List<Drawable> list) {
        this.context = context2;
        this.backgroundChangeListener = backgroundChangeListener2;
        for (Drawable squareView : list) {
            squareViews.add(new SquareView(1, "", false, true, squareView));
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemSquareBinding binding = ItemSquareBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new ViewHolder(binding);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SquareView squareView = squareViews.get(i);
        if (squareView.isColor) {
            viewHolder.binding.squareView.setBackgroundColor(squareView.drawableId);
        } else if (squareView.drawable != null) {
            viewHolder.binding.squareView.setVisibility(View.GONE);
            viewHolder.binding.imageViewSquare.setVisibility(View.VISIBLE);
            viewHolder.binding.imageViewSquare.setImageDrawable(squareView.drawable);
        } else {
            viewHolder.binding.squareView.setBackgroundResource(squareView.drawableId);
        }
        if (selectedSquareIndex == i) {
            viewHolder.binding.constraintLayoutWrapperSquareView.setBackground(context.getDrawable(R.drawable.border_view));
        } else {
            viewHolder.binding.constraintLayoutWrapperSquareView.setBackground(context.getDrawable(R.drawable.border_transparent_view));
        }
    }

    public void setSelectedSquareIndex(int i) {
        selectedSquareIndex = i;
    }

    public int getItemCount() {
        return squareViews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ItemSquareBinding binding;

        public ViewHolder(ItemSquareBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.imageViewSquare.setVisibility(View.GONE);
            binding.getRoot().setOnClickListener(this);
        }

        public void onClick(View view) {
            selectedSquareIndex = getAdapterPosition();
            backgroundChangeListener.onBackgroundSelected( squareViews.get(selectedSquareIndex));
            notifyDataSetChanged();
        }
    }

    public static class SquareView {
        public Drawable drawable;
        public int drawableId;
        public boolean isBitmap;
        public boolean isColor;
        public String text;

        SquareView(int i, String str) {
            this.drawableId = i;
            this.text = str;
        }

        public SquareView(int i, String str, boolean z) {
            this.drawableId = i;
            this.text = str;
            this.isColor = z;
        }

        public SquareView(int i, String str, boolean z, boolean z2, Drawable drawable2) {
            this.drawableId = i;
            this.text = str;
            this.isColor = z;
            this.isBitmap = z2;
            this.drawable = drawable2;
        }
    }
}
