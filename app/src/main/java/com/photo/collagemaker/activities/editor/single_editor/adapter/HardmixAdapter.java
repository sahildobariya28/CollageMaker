package com.photo.collagemaker.activities.editor.single_editor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.assets.EffectCodeAsset;
import com.photo.collagemaker.databinding.ItemOverlayBinding;
import com.photo.collagemaker.listener.HardmixListener;

import java.util.List;

public class HardmixAdapter extends RecyclerView.Adapter<HardmixAdapter.ViewHolder> {
    private List<Bitmap> bitmaps;
    private Context context;
    public List<EffectCodeAsset.EffectsCode> effectsCodeList;
    public HardmixListener hardmixListener;
    public int selectIndex = 0;

    public HardmixAdapter(List<Bitmap> bitmapList, HardmixListener hardmixListener, Context mContext, List<EffectCodeAsset.EffectsCode> effectsCodeList) {
        this.hardmixListener = hardmixListener;
        this.bitmaps = bitmapList;
        this.context = mContext;
        this.effectsCodeList = effectsCodeList;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemOverlayBinding binding = ItemOverlayBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new ViewHolder(binding);
    }

    public void reset() {
        selectIndex = 0;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.binding.textViewFilterName.setText(effectsCodeList.get(i).getName());
        viewHolder.binding.textViewFilterName.setTextColor(ContextCompat.getColor(context, R.color.itemColorBlack));
        viewHolder.binding.roundImageViewFilterItem.setImageBitmap(bitmaps.get(i));
        if (selectIndex == i) {
            viewHolder.binding.textViewFilterName.setTextColor(ContextCompat.getColor(context, R.color.itemColorBlack));
            viewHolder.binding.viewSelected.setVisibility(View.VISIBLE);
            return;
        }
        viewHolder.binding.textViewFilterName.setTextColor(ContextCompat.getColor(context, R.color.itemColorBlack));
        viewHolder.binding.viewSelected.setVisibility(View.GONE);

    }

    public int getItemCount() {
        return bitmaps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemOverlayBinding binding;
        ViewHolder(ItemOverlayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


            binding.getRoot().setOnClickListener(view1 -> {
                selectIndex = getLayoutPosition();
                hardmixListener.onFilterSelected(effectsCodeList.get(selectIndex).getImage());
                notifyDataSetChanged();
            });
        }
    }
}
