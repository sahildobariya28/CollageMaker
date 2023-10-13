package com.photo.collagemaker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.photo.collagemaker.R;
import com.photo.collagemaker.constants.Constants;
import com.photo.collagemaker.databinding.ItemMosaicBinding;
import com.photo.collagemaker.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class MosaicAdapter extends RecyclerView.Adapter<MosaicAdapter.ViewHolder> {
    private int borderWidth;
    private Context context;
    public MosaicChangeListener mosaicChangeListener;
    public List<MosaicItem> mosaicItems = new ArrayList();
    public int selectedSquareIndex;

    public enum BLUR {
        BLUR,
        MOSAIC,
        SHADER
    }

    public interface MosaicChangeListener {
        void onSelected(MosaicItem mosaicItem);
    }

    public MosaicAdapter(Context context2, MosaicChangeListener mosaicChangeListener2) {
        this.context = context2;
        this.mosaicChangeListener = mosaicChangeListener2;
        this.borderWidth = SystemUtil.dpToPx(context2, Constants.BORDER_WIDTH);
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_1, 0, BLUR.BLUR));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_2, 0, BLUR.MOSAIC));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_3, R.drawable.mosaic_3, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_4, R.drawable.mosaic_4, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_5, R.drawable.mosaic_5, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_6, R.drawable.mosaic_6, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_7, R.drawable.mosaic_7, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_8, R.drawable.mosaic_8, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_9, R.drawable.mosaic_9, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_10, R.drawable.mosaic_10, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_11, R.drawable.mosaic_11, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_12, R.drawable.mosaic_12, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_13, R.drawable.mosaic_13, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_14, R.drawable.mosaic_14, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_15, R.drawable.mosaic_15, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_16, R.drawable.mosaic_16, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_17, R.drawable.mosaic_17, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_18, R.drawable.mosaic_18, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_19, R.drawable.mosaic_19, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_20, R.drawable.mosaic_20, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_21, R.drawable.mosaic_21, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_22, R.drawable.mosaic_22, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_23, R.drawable.mosaic_23, BLUR.SHADER));
        mosaicItems.add(new MosaicItem(R.drawable.mosaic_24, R.drawable.mosaic_24, BLUR.SHADER));

    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemMosaicBinding binding = ItemMosaicBinding.inflate(LayoutInflater.from(viewGroup.getContext()),viewGroup, false);
        return new ViewHolder(binding);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(context).load(Integer.valueOf(mosaicItems.get(i).frameId)).into(viewHolder.binding.roundImageViewMosaicItem);
        if (selectedSquareIndex == i) {
            viewHolder.binding.roundImageViewMosaicItem.setBorderColor(ContextCompat.getColor(context, R.color.theme_color));
            viewHolder.binding.roundImageViewMosaicItem.setBorderWidth(borderWidth);
            return;
        }
        viewHolder.binding.roundImageViewMosaicItem.setBorderColor(0);
        viewHolder.binding.roundImageViewMosaicItem.setBorderWidth(borderWidth);
    }

    public int getItemCount() {
        return mosaicItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemMosaicBinding binding;

        public ViewHolder(ItemMosaicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(view -> {
                selectedSquareIndex = getAdapterPosition();
                if (selectedSquareIndex < mosaicItems.size()) {
                    mosaicChangeListener.onSelected(mosaicItems.get(selectedSquareIndex));
                }
                notifyDataSetChanged();
            });
        }
    }

    public static class MosaicItem {
        int frameId;
        public int shaderId;
        public BLUR mode;

        public MosaicItem(int frameId, int shaderId, BLUR mode2) {
            this.frameId = frameId;
            this.shaderId = shaderId;
            this.mode = mode2;
        }
    }
}
