package com.photo.collagemaker.activities.editor.collage_editor.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.databinding.ItemGridBinding;
import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.layer.slant.NumberSlantLayout;
import com.photo.collagemaker.layer.straight.NumberStraightLayout;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {
    private List<Bitmap> bitmapData = new ArrayList();
    private List<QueShotLayout> layoutData = new ArrayList();

    public OnItemClickListener onItemClickListener;

    public int selectedIndex = 0;

    public interface OnItemClickListener {
        void onItemClick(QueShotLayout puzzleLayout, int i);
    }

    public GridViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemGridBinding binding = ItemGridBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new GridViewHolder(binding);
    }


    public void onBindViewHolder(GridViewHolder holder, int i) {
        final QueShotLayout collageLayout = layoutData.get(i);
        holder.binding.squareCollageView.setNeedDrawLine(true);
        holder.binding.squareCollageView.setNeedDrawOuterLine(true);
        holder.binding.squareCollageView.setTouchEnable(false);
        holder.binding.squareCollageView.setLineSize(6);
        holder.binding.squareCollageView.setQueShotLayout(collageLayout);
        if (selectedIndex == i) {
            holder.binding.squareCollageView.setBackgroundColor(Color.parseColor("#333333"));
        } else {
            holder.binding.squareCollageView.setBackgroundColor(Color.parseColor("#9F9F9F"));
        }
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                int i1 = 0;
                if (collageLayout instanceof NumberSlantLayout) {
                    i1 = ((NumberSlantLayout) collageLayout).getTheme();
                } else if (collageLayout instanceof NumberStraightLayout) {
                    i1 = ((NumberStraightLayout) collageLayout).getTheme();
                }
                onItemClickListener.onItemClick(collageLayout, i1);
            }
            selectedIndex = holder.getLayoutPosition();
            notifyDataSetChanged();
        });
        if (bitmapData != null) {
            int size = bitmapData.size();
            if (collageLayout.getAreaCount() > size) {
                for (int i2 = 0; i2 < collageLayout.getAreaCount(); i2++) {
                    holder.binding.squareCollageView.addQuShotCollage(bitmapData.get(i2 % size));
                }
                return;
            }
            holder.binding.squareCollageView.addPieces(bitmapData);
        }
    }

    public int getItemCount() {
        if (layoutData == null) {
            return 0;
        }
        return layoutData.size();
    }

    public void refreshData(List<QueShotLayout> list, List<Bitmap> list2) {
        layoutData = list;
        bitmapData = list2;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        onItemClickListener = onItemClickListener2;
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder {
        ItemGridBinding binding;
        public GridViewHolder(ItemGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
