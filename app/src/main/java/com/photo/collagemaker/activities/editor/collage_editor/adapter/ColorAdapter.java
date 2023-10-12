package com.photo.collagemaker.activities.editor.collage_editor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.assets.BrushColorAsset;
import com.photo.collagemaker.databinding.ItemColorBinding;
import com.photo.collagemaker.listener.BrushColorListener;

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
        ItemColorBinding binding = ItemColorBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new ViewHolder(binding);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.binding.squareView.setBackgroundColor(Color.parseColor(colors.get(i)));
        if (selectedColorIndex == i) {
            viewHolder.binding.constraintLayoutWrapperSquareView.setBackground(context.getDrawable(R.drawable.border_view));
        } else {
            viewHolder.binding.constraintLayoutWrapperSquareView.setBackground(context.getDrawable(R.drawable.border_transparent_view));
        }
    }

    public int getItemCount() {
        return colors.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemColorBinding binding;
        ViewHolder(ItemColorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.squareView.setOnClickListener(view -> {
                selectedColorIndex = getLayoutPosition();
                brushColorListener.onColorChanged(colors.get(selectedColorIndex));
                notifyDataSetChanged();
            });

        }
    }

    public void setSelectedColorIndex(int i) {
        selectedColorIndex = i;
    }
}
