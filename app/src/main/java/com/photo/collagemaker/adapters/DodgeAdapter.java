package com.photo.collagemaker.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.photo.collagemaker.R;
import com.photo.collagemaker.databinding.ItemOverlayBinding;
import com.photo.collagemaker.listener.DodgeListener;
import com.photo.collagemaker.assets.EffectCodeAsset;

import java.util.List;

public class DodgeAdapter extends RecyclerView.Adapter<DodgeAdapter.ViewHolder> {
    private List<Bitmap> bitmapList;
    private Context context;
    public List<EffectCodeAsset.EffectsCode> effectsCodeList;
    public DodgeListener dodgeListener;
    public int selectIndex = 0;

    public DodgeAdapter(List<Bitmap> bitmapList, DodgeListener dodgeListener, Context mContext, List<EffectCodeAsset.EffectsCode> effectsCodeList) {
        this.dodgeListener = dodgeListener;
        this.bitmapList = bitmapList;
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
        viewHolder.binding.textViewFilterName.setTextColor(ContextCompat.getColor(context, R.color.text_color_light));
        viewHolder.binding.roundImageViewFilterItem.setImageBitmap(bitmapList.get(i));
        if (selectIndex == i) {
            viewHolder.binding.textViewFilterName.setTextColor(ContextCompat.getColor(context, R.color.text_color_light));
            viewHolder.binding.viewSelected.setVisibility(View.VISIBLE);
            return;
        }
        viewHolder.binding.textViewFilterName.setTextColor(ContextCompat.getColor(context, R.color.text_color_light));
        viewHolder.binding.viewSelected.setVisibility(View.GONE);

    }

    public int getItemCount() {
        return bitmapList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemOverlayBinding binding;
        ViewHolder(ItemOverlayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(view1 -> {
                dodgeListener.onFilterSelected(effectsCodeList.get(getLayoutPosition()).getImage());
                notifyDataSetChanged();
            });
        }
    }
}
