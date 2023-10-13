package com.photo.collagemaker.activities.editor.single_editor.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.databinding.ItemAdjustBinding;
import com.photo.collagemaker.listener.AdjustListener;
import com.photo.collagemaker.custom_view.CustomEditor;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class AdjustAdapter extends RecyclerView.Adapter<AdjustAdapter.ViewHolder> {
    public String ADJUST = " @adjust brightness 0 @adjust contrast 1 @adjust saturation 1 @adjust sharpen 0 @adjust exposure 0 @adjust hue 0 ";

    public AdjustListener adjustListener;
    private Context context;
    public List<AdjustModel> adjustModelList;
    public int selectedIndex = 0;

    public class AdjustModel {
        String code;
        Drawable icon;
        public int index;
        public float intensity;
        public float maxValue;
        public float minValue;
        public String name;
        public float originValue;
        public float seekbarIntensity = 0.5f;

        AdjustModel(int index, String name, String code, Drawable icon, float minValue, float originValue, float maxValue) {
            this.index = index;
            this.name = name;
            this.code = code;
            this.icon = icon;
            this.minValue = minValue;
            this.originValue = originValue;
            this.maxValue = maxValue;
        }

        public void setSeekBarIntensity(CustomEditor photoCustomEditor, float mFloat, boolean mBoolean) {
            if (photoCustomEditor != null) {
                seekbarIntensity = mFloat;
                intensity = calcIntensity(mFloat);
                photoCustomEditor.setFilterIntensityForIndex(intensity, index, mBoolean);
            }
        }


        public float calcIntensity(float f) {
            if (f <= 0.0f) {
                return minValue;
            }
            if (f >= 1.0f) {
                return maxValue;
            }
            if (f <= 0.5f) {
                return minValue + ((originValue - minValue) * f * 2.0f);
            }
            return maxValue + ((originValue - maxValue) * (1.0f - f) * 2.0f);
        }
    }

    public AdjustAdapter(Context mContext, AdjustListener mAdjustListener) {
        context = mContext;
        adjustListener = mAdjustListener;
        init();
    }

    public void setSelectedAdjust(int i) {
        adjustListener.onAdjustSelected(adjustModelList.get(i));
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemAdjustBinding binding = ItemAdjustBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new ViewHolder(binding);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.binding.textViewAdjustName.setText(adjustModelList.get(i).name);
        viewHolder.binding.imageViewAdjustIcon.setImageDrawable(selectedIndex != i ? adjustModelList.get(i).icon : adjustModelList.get(i).icon);
        if (selectedIndex == i) {
            viewHolder.binding.textViewAdjustName.setTextColor(ContextCompat.getColor(context, R.color.text_color_theme));
            viewHolder.binding.imageViewAdjustIcon.setColorFilter(ContextCompat.getColor(context, R.color.text_color_theme));

        } else {
            viewHolder.binding.textViewAdjustName.setTextColor(ContextCompat.getColor(context, R.color.text_color_dark));
            viewHolder.binding.imageViewAdjustIcon.setColorFilter(ContextCompat.getColor(context, R.color.text_color_dark));
        }
    }

    public int getItemCount() {
        return adjustModelList.size();
    }

    public String getFilterConfig() {
        String str = ADJUST;
        return MessageFormat.format(str,
                adjustModelList.get(0).originValue + "",
                adjustModelList.get(1).originValue + "",
                adjustModelList.get(2).originValue + "",
                adjustModelList.get(3).originValue + "",
                adjustModelList.get(4).originValue + "",
                Float.valueOf(adjustModelList.get(5).originValue) );
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemAdjustBinding binding;
        ViewHolder(ItemAdjustBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


            binding.getRoot().setOnClickListener(view -> {
                selectedIndex = ViewHolder.this.getLayoutPosition();
                adjustListener.onAdjustSelected( adjustModelList.get(selectedIndex));
                notifyDataSetChanged();
            });
        }
    }

    public AdjustModel getCurrentAdjustModel() {
        return adjustModelList.get(selectedIndex);
    }

    private void init() {
        adjustModelList = new ArrayList();
        adjustModelList.add(new AdjustModel(0, context.getString(R.string.brightness), "brightness", context.getDrawable(R.drawable.ic_brightness), -1.0f, 0.0f, 1.0f));
        adjustModelList.add(new AdjustModel(1, context.getString(R.string.contrast), "contrast", context.getDrawable(R.drawable.ic_contrast), 0.1f, 1.0f, 3.0f));
        adjustModelList.add(new AdjustModel(2, context.getString(R.string.saturation), "saturation", context.getDrawable(R.drawable.ic_saturation),  0.0f, 1.0f, 3.0f));
        adjustModelList.add(new AdjustModel(5, context.getString(R.string.hue), "hue", context.getDrawable(R.drawable.ic_hue), -2.0f, 0.0f, 2.0f));
        adjustModelList.add(new AdjustModel(3, context.getString(R.string.sharpen), "sharpen", context.getDrawable(R.drawable.ic_sharpen), -1.0f, 0.0f, 10.0f));
        adjustModelList.add(new AdjustModel(4, context.getString(R.string.exposure), "exposure", context.getDrawable(R.drawable.ic_exposure), -2.0f, 0.0f, 2.0f));
    }

}
