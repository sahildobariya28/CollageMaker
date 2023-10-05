package com.photo.collagemaker.adapters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.grid.QueShotLayout;
import com.photo.collagemaker.queshot.QueShotSquareView;
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
        return new GridViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_grid, viewGroup, false));
    }

    public void setSelectedIndex(int i) {
        selectedIndex = i;
    }

    public void onBindViewHolder(GridViewHolder collageViewHolder, final int i) {
        final QueShotLayout collageLayout = layoutData.get(i);
        collageViewHolder.square_collage_view.setNeedDrawLine(true);
        collageViewHolder.square_collage_view.setNeedDrawOuterLine(true);
        collageViewHolder.square_collage_view.setTouchEnable(false);
        collageViewHolder.square_collage_view.setLineSize(6);
        collageViewHolder.square_collage_view.setQueShotLayout(collageLayout);
        if (selectedIndex == i) {
            collageViewHolder.square_collage_view.setBackgroundColor(Color.parseColor("#333333"));
        } else {
            collageViewHolder.square_collage_view.setBackgroundColor(Color.parseColor("#9F9F9F"));
        }
        collageViewHolder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                int i1 = 0;
                if (collageLayout instanceof NumberSlantLayout) {
                    i1 = ((NumberSlantLayout) collageLayout).getTheme();
                } else if (collageLayout instanceof NumberStraightLayout) {
                    i1 = ((NumberStraightLayout) collageLayout).getTheme();
                }
                onItemClickListener.onItemClick(collageLayout, i1);
            }
            selectedIndex = i;
            notifyDataSetChanged();
        });
        if (bitmapData != null) {
            int size = bitmapData.size();
            if (collageLayout.getAreaCount() > size) {
                for (int i2 = 0; i2 < collageLayout.getAreaCount(); i2++) {
                    collageViewHolder.square_collage_view.addQuShotCollage(bitmapData.get(i2 % size));
                }
                return;
            }
            collageViewHolder.square_collage_view.addPieces(bitmapData);
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
        QueShotSquareView square_collage_view;

        public GridViewHolder(View view) {
            super(view);
            square_collage_view = view.findViewById(R.id.squareCollageView);
        }
    }
}
