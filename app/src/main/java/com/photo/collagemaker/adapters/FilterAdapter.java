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

import com.photo.collagemaker.R;
import com.photo.collagemaker.listener.FilterListener;
import com.photo.collagemaker.assets.FilterCodeAsset;
import com.github.siyamed.shapeimageview.RoundedImageView;

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
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_filter, viewGroup, false));
    }

    public void reset() {
        selectedIndex = 0;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.text_view_filter_name.setText(filterBeanList.get(i).getName());
        viewHolder.text_view_filter_name.setTextColor(ContextCompat.getColor(context, R.color.itemColorBlack));
        viewHolder.round_image_view_filter_item.setImageBitmap(bitmaps.get(i));
        if (selectedIndex == i) {
            viewHolder.text_view_filter_name.setTextColor(ContextCompat.getColor(context, R.color.text_color_dark));
            viewHolder.viewSelected.setVisibility(View.VISIBLE);
            return;
        }
        viewHolder.text_view_filter_name.setTextColor(ContextCompat.getColor(context, R.color.text_color_dark));
        viewHolder.viewSelected.setVisibility(View.GONE);

    }

    public int getItemCount() {
        return bitmaps.size();
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
                filterListener.onFilterSelected(filterBeanList.get(getLayoutPosition()).getCode());
                notifyDataSetChanged();
            });
        }
    }
}
