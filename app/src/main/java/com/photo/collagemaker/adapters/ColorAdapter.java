package com.photo.collagemaker.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.listener.BrushColorListener;
import com.photo.collagemaker.assets.BrushColorAsset;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    public BrushColorListener brushColorListener;
    public List<String> colors = BrushColorAsset.listColorBrush();
    private Context context;
    public int selectedColorIndex;

    public ColorAdapter(Context context2, BrushColorListener brushColorListener2) {
        this.context = context2;
        this.brushColorListener = brushColorListener2;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_color, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.squareView.setBackgroundColor(Color.parseColor(colors.get(i)));
        if (selectedColorIndex == i) {
            viewHolder.constraint_layout_wrapper_square_view.setBackground(context.getDrawable(R.drawable.border_view));
        } else {
            viewHolder.constraint_layout_wrapper_square_view.setBackground(context.getDrawable(R.drawable.border_transparent_view));
        }
    }

    public int getItemCount() {
        return colors.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View squareView;
        public ConstraintLayout constraint_layout_wrapper_square_view;

        ViewHolder(View view) {
            super(view);
            squareView = view.findViewById(R.id.square_view);
            constraint_layout_wrapper_square_view = view.findViewById(R.id.constraint_layout_wrapper_square_view);
            squareView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    selectedColorIndex = getLayoutPosition();
                    brushColorListener.onColorChanged(colors.get(selectedColorIndex));
                    notifyDataSetChanged();
                }
            });

        }
    }

    public void setSelectedColorIndex(int i) {
        selectedColorIndex = i;
    }
}
