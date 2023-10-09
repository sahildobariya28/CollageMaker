package com.photo.collagemaker.adapters;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.custom.RatioCustom;
import com.photo.collagemaker.databinding.ItemCropBinding;
import com.steelkiwi.cropiwa.AspectRatio;

import java.util.Arrays;
import java.util.List;

public class AspectAdapter extends RecyclerView.Adapter<AspectAdapter.ViewHolder> {
    public int lastSelectedView;
    public OnNewSelectedListener listener;
    public List<RatioCustom> ratios;
    public RatioCustom selectedRatio;
    Activity activity;

    public interface OnNewSelectedListener {
        void onNewAspectRatioSelected(AspectRatio aspectRatio);
    }

    public AspectAdapter(Activity activity) {
        this.activity = activity;
        ratios = Arrays.asList(new RatioCustom(10, 10, R.drawable.ic_crop_free),
                new RatioCustom(1, 1, R.drawable.ic_instagram1_1),
                new RatioCustom(4, 3, R.drawable.ic_facebook4_3),
                new RatioCustom(3, 4, R.drawable.ic_crop_3_4),
                new RatioCustom(5, 4, R.drawable.ic_crop_5_4),
                new RatioCustom(4, 5, R.drawable.ic_instagram4_5),
                new RatioCustom(3, 2, R.drawable.ic_crop_3_2),
                new RatioCustom(2, 3, R.drawable.ic_pinterest2_3),
                new RatioCustom(9, 16, R.drawable.ic_crop_9_16),
                new RatioCustom(16, 9, R.drawable.ic_crop_16_9));
        selectedRatio = ratios.get(0);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemCropBinding binding = ItemCropBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new ViewHolder(binding);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RatioCustom aspectRatioCustom = ratios.get(i);
        if (i == lastSelectedView) {
            viewHolder.binding.imageViewAspectRatio.setImageResource(aspectRatioCustom.getSelectedIem());
            viewHolder.binding.imageViewAspectRatio.setColorFilter(ContextCompat.getColor(activity, R.color.icon_color_light), PorterDuff.Mode.SRC_IN);
            viewHolder.binding.relativeLayoutCropper.setBackgroundResource(R.drawable.icon_bg_theme);
        } else {
            viewHolder.binding.imageViewAspectRatio.setImageResource(aspectRatioCustom.getSelectedIem());
            viewHolder.binding.imageViewAspectRatio.setColorFilter(ContextCompat.getColor(activity, R.color.icon_color_dark), PorterDuff.Mode.SRC_IN);
            viewHolder.binding.relativeLayoutCropper.setBackgroundResource(R.drawable.icon_bg_light);
        }
    }


    public void setLastSelectedView(int i) {
        lastSelectedView = i;
    }

    public int getItemCount() {
        return ratios.size();
    }

    public void setListener(OnNewSelectedListener onNewSelectedListener) {
        listener = onNewSelectedListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemCropBinding binding;
        public ViewHolder(ItemCropBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.imageViewAspectRatio.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (lastSelectedView != getAdapterPosition()) {
                selectedRatio = ratios.get(getAdapterPosition());
                lastSelectedView = getAdapterPosition();
                if (listener != null) {
                    listener.onNewAspectRatioSelected(selectedRatio);
                }
                notifyDataSetChanged();
            }
        }
    }
}
