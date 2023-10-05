package com.photo.collagemaker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.assets.FontAsset;

import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.ViewHolder> {
    private Context context;
    private List<String> lstFonts;

    public ItemClickListener mClickListener;
    private LayoutInflater mInflater;

    public int selectedItem = 0;

    public interface ItemClickListener {
        void onItemClick(View view, int i);
    }

    public int getItemViewType(int i) {
        return i;
    }

    public FontAdapter(Context context2, List<String> list) {
        this.mInflater = LayoutInflater.from(context2);
        this.context = context2;
        this.lstFonts = list;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(mInflater.inflate(R.layout.item_font, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        int i2;
        FontAsset.setFontByName(context, viewHolder.font, lstFonts.get(i));
        ConstraintLayout constraintLayout = viewHolder.wrapperFontItems;
        if (selectedItem != i) {
            i2 = R.drawable.border_black_view;
        } else {
            i2 = R.drawable.border_view;
        }
        constraintLayout.setBackground(ContextCompat.getDrawable(context, i2));
    }

    public int getItemCount() {
        return lstFonts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView font;
        ConstraintLayout wrapperFontItems;

        ViewHolder(View view) {
            super(view);
            font = view.findViewById(R.id.text_view_font_item);
            wrapperFontItems = view.findViewById(R.id.constraint_layout_wrapper_font_item);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            selectedItem = getAdapterPosition();
            if (mClickListener != null) {
                mClickListener.onItemClick(view, selectedItem);
            }
            notifyDataSetChanged();
        }
    }

    public void setSelectedItem(int i) {
        selectedItem = i;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }
}
