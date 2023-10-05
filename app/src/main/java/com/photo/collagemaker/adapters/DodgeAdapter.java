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
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_overlay, viewGroup, false));
    }

    public void reset() {
        selectIndex = 0;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.text_view_filter_name.setText(effectsCodeList.get(i).getName());
        viewHolder.text_view_filter_name.setTextColor(ContextCompat.getColor(context, R.color.itemColorBlack));
        viewHolder.round_image_view_filter_item.setImageBitmap(bitmapList.get(i));
        if (selectIndex == i) {
            viewHolder.text_view_filter_name.setTextColor(ContextCompat.getColor(context, R.color.itemColorBlack));
            viewHolder.viewSelected.setVisibility(View.VISIBLE);
            return;
        }
        viewHolder.text_view_filter_name.setTextColor(ContextCompat.getColor(context, R.color.itemColorBlack));
        viewHolder.viewSelected.setVisibility(View.GONE);

    }

    public int getItemCount() {
        return bitmapList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView round_image_view_filter_item;
        TextView text_view_filter_name;
        View viewSelected;

        ViewHolder(View view) {
            super(view);
            round_image_view_filter_item = view.findViewById(R.id.round_image_view_filter_item);
            text_view_filter_name = view.findViewById(R.id.text_view_filter_name);
            viewSelected = view.findViewById(R.id.view_selected);
            view.setOnClickListener(view1 -> {
                dodgeListener.onFilterSelected(effectsCodeList.get(getLayoutPosition()).getImage());
                notifyDataSetChanged();
            });
        }
    }
}
