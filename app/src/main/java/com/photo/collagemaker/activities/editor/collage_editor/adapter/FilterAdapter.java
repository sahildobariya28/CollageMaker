package com.photo.collagemaker.activities.editor.collage_editor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.databinding.ItemFilterBinding;
import com.photo.collagemaker.listener.FilterListener;
import com.photo.collagemaker.assets.FilterCodeAsset;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private List<Bitmap> bitmaps;
    private Context context;
    public List<FilterCodeAsset.FiltersCode> filterBeanList;
    public FilterListener filterListener;
    public int selectedIndex = 0;

    public FilterAdapter(List<Bitmap> bitmapList, FilterListener filterListener, Context mContext, List<FilterCodeAsset.FiltersCode> mfilterBeanList) {
        this.filterListener = filterListener;
        this.bitmaps = bitmapList;
        this.context = mContext;
        this.filterBeanList = mfilterBeanList;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemFilterBinding binding = ItemFilterBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new ViewHolder(binding);
    }

    public void reset() {
        selectedIndex = 0;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.binding.textViewFilterName.setText(filterBeanList.get(i).getName());
        viewHolder.binding.textViewFilterName.setTextColor(ContextCompat.getColor(context, R.color.text_color_dark));
        viewHolder.binding.roundImageViewFilterItem.setImageBitmap(bitmaps.get(i));
        if (selectedIndex == i) {
            viewHolder.binding.textViewFilterName.setTextColor(ContextCompat.getColor(context, R.color.text_color_theme));
            viewHolder.binding.viewSelected.setVisibility(View.VISIBLE);
            return;
        }
        viewHolder.binding.textViewFilterName.setTextColor(ContextCompat.getColor(context, R.color.text_color_dark));
        viewHolder.binding.viewSelected.setVisibility(View.GONE);

    }

    public int getItemCount() {
        return bitmaps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemFilterBinding binding;
        ViewHolder(ItemFilterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;



            binding.getRoot().setOnClickListener(view1 -> {
                selectedIndex = getLayoutPosition();
                filterListener.onFilterSelected(filterBeanList.get(getLayoutPosition()).getCode());
                notifyDataSetChanged();
            });
        }
    }
}
