package com.photo.collagemaker.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.assets.BrushColorAsset;
import com.photo.collagemaker.databinding.ItemSquareBinding;

import java.util.ArrayList;
import java.util.List;

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.ViewHolder> {

    public BackgroundListener backgroundListener;
    private Context context;

    public int selectedIndex;

    public List<SquareView> squareViewList = new ArrayList();

    public interface BackgroundListener {
        void onBackgroundSelected(SquareView squareView);
    }

    public BackgroundAdapter(Context context, BackgroundListener backgroundListener) {
        this.context = context;
        this.backgroundListener = backgroundListener;
        this.squareViewList.add(new SquareView(context.getColor(R.color.transparent), "Transparent", true));
        List<String> lstColorForBrush = BrushColorAsset.listColorBrush();
        for (int i = 0; i < lstColorForBrush.size() - 2; i++) {
            this.squareViewList.add(new SquareView(Color.parseColor(lstColorForBrush.get(i)), "", true));
        }
    }

    public BackgroundAdapter(Context context2, BackgroundListener backgroundChangeListener2, boolean z) {
        context = context2;
        backgroundListener = backgroundChangeListener2;
        squareViewList.add(new SquareView(R.drawable.gradient_1, "Gradient_1"));
        squareViewList.add(new SquareView(R.drawable.gradient_2, "Gradient_2"));
        squareViewList.add(new SquareView(R.drawable.gradient_3, "Gradient_3"));
        squareViewList.add(new SquareView(R.drawable.gradient_4, "Gradient_4"));
        squareViewList.add(new SquareView(R.drawable.gradient_5, "Gradient_5"));
        squareViewList.add(new SquareView(R.drawable.gradient_6, "Gradient_6"));
        squareViewList.add(new SquareView(R.drawable.gradient_7, "Gradient_7"));
        squareViewList.add(new SquareView(R.drawable.gradient_8, "Gradient_8"));
        squareViewList.add(new SquareView(R.drawable.gradient_9, "Gradient_9"));
        squareViewList.add(new SquareView(R.drawable.gradient_10, "Gradient_10"));
        squareViewList.add(new SquareView(R.drawable.gradient_11, "Gradient_11"));
        squareViewList.add(new SquareView(R.drawable.gradient_12, "Gradient_12"));
        squareViewList.add(new SquareView(R.drawable.gradient_13, "Gradient_13"));
        squareViewList.add(new SquareView(R.drawable.gradient_14, "Gradient_14"));
        squareViewList.add(new SquareView(R.drawable.gradient_15, "Gradient_15"));
        squareViewList.add(new SquareView(R.drawable.gradient_16, "Gradient_16"));
        squareViewList.add(new SquareView(R.drawable.gradient_17, "Gradient_17"));
        squareViewList.add(new SquareView(R.drawable.gradient_18, "Gradient_18"));
        squareViewList.add(new SquareView(R.drawable.gradient_19, "Gradient_19"));
        squareViewList.add(new SquareView(R.drawable.gradient_20, "Gradient_20"));
        squareViewList.add(new SquareView(R.drawable.gradient_21, "Gradient_21"));
        squareViewList.add(new SquareView(R.drawable.gradient_22, "Gradient_22"));
        squareViewList.add(new SquareView(R.drawable.gradient_23, "Gradient_23"));
        squareViewList.add(new SquareView(R.drawable.gradient_24, "Gradient_24"));
        squareViewList.add(new SquareView(R.drawable.gradient_25, "Gradient_25"));
        squareViewList.add(new SquareView(R.drawable.gradient_26, "Gradient_26"));
        squareViewList.add(new SquareView(R.drawable.gradient_27, "Gradient_27"));
        squareViewList.add(new SquareView(R.drawable.gradient_28, "Gradient_28"));
        squareViewList.add(new SquareView(R.drawable.gradient_29, "Gradient_29"));
        squareViewList.add(new SquareView(R.drawable.gradient_30, "Gradient_30"));
        squareViewList.add(new SquareView(R.drawable.gradient_31, "Gradient_31"));
        squareViewList.add(new SquareView(R.drawable.gradient_32, "Gradient_32"));
        squareViewList.add(new SquareView(R.drawable.gradient_33, "Gradient_33"));
        squareViewList.add(new SquareView(R.drawable.gradient_34, "Gradient_34"));
        squareViewList.add(new SquareView(R.drawable.gradient_35, "Gradient_35"));
        squareViewList.add(new SquareView(R.drawable.gradient_36, "Gradient_36"));
        squareViewList.add(new SquareView(R.drawable.gradient_37, "Gradient_37"));
        squareViewList.add(new SquareView(R.drawable.gradient_38, "Gradient_38"));
        squareViewList.add(new SquareView(R.drawable.gradient_39, "Gradient_39"));
        squareViewList.add(new SquareView(R.drawable.gradient_40, "Gradient_40"));
        squareViewList.add(new SquareView(R.drawable.gradient_41, "Gradient_41"));
        squareViewList.add(new SquareView(R.drawable.gradient_42, "Gradient_42"));
        squareViewList.add(new SquareView(R.drawable.gradient_43, "Gradient_43"));
        squareViewList.add(new SquareView(R.drawable.gradient_44, "Gradient_44"));
        squareViewList.add(new SquareView(R.drawable.gradient_45, "Gradient_45"));
        squareViewList.add(new SquareView(R.drawable.gradient_46, "Gradient_46"));
        squareViewList.add(new SquareView(R.drawable.gradient_47, "Gradient_47"));
        squareViewList.add(new SquareView(R.drawable.gradient_48, "Gradient_48"));
        squareViewList.add(new SquareView(R.drawable.gradient_49, "Gradient_49"));
        squareViewList.add(new SquareView(R.drawable.gradient_50, "Gradient_50"));
        squareViewList.add(new SquareView(R.drawable.gradient_51, "Gradient_51"));
        squareViewList.add(new SquareView(R.drawable.gradient_52, "Gradient_52"));
        squareViewList.add(new SquareView(R.drawable.gradient_53, "Gradient_53"));
        squareViewList.add(new SquareView(R.drawable.gradient_54, "Gradient_54"));
        squareViewList.add(new SquareView(R.drawable.gradient_55, "Gradient_55"));
        squareViewList.add(new SquareView(R.drawable.gradient_56, "Gradient_56"));
        squareViewList.add(new SquareView(R.drawable.gradient_57, "Gradient_57"));
        squareViewList.add(new SquareView(R.drawable.gradient_58, "Gradient_58"));
        squareViewList.add(new SquareView(R.drawable.gradient_59, "Gradient_59"));
        squareViewList.add(new SquareView(R.drawable.gradient_60, "Gradient_60"));
        squareViewList.add(new SquareView(R.drawable.gradient_61, "Gradient_61"));
        squareViewList.add(new SquareView(R.drawable.gradient_62, "Gradient_62"));
        squareViewList.add(new SquareView(R.drawable.gradient_63, "Gradient_63"));
        squareViewList.add(new SquareView(R.drawable.gradient_64, "Gradient_64"));
        squareViewList.add(new SquareView(R.drawable.gradient_65, "Gradient_65"));
    }

    public BackgroundAdapter(Context context2, BackgroundListener backgroundChangeListener2, List<Drawable> list) {
        context = context2;
        backgroundListener = backgroundChangeListener2;
        for (Drawable squareView : list) {
            squareViewList.add(new SquareView(1, "", false, true, squareView));
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemSquareBinding binding = ItemSquareBinding.inflate(LayoutInflater.from(viewGroup.getContext()),viewGroup, false);
        return new ViewHolder(binding);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SquareView squareView = squareViewList.get(i);
        if (squareView.isColor) {
            viewHolder.binding.squareView.setBackgroundColor(squareView.drawableId);
        } else if (squareView.drawable != null) {
            viewHolder.binding.squareView.setVisibility(View.GONE);
            viewHolder.binding.imageViewSquare.setVisibility(View.VISIBLE);
            viewHolder.binding.imageViewSquare.setImageDrawable(squareView.drawable);
        } else {
            viewHolder.binding.squareView.setBackgroundResource(squareView.drawableId);
        }
        if (selectedIndex == i) {
            viewHolder.binding.constraintLayoutWrapperSquareView.setBackground(context.getDrawable(R.drawable.border_view));
        } else {
            viewHolder.binding.constraintLayoutWrapperSquareView.setBackground(context.getDrawable(R.drawable.border_transparent_view));
        }
    }

    public void setSelectedIndex(int i) {
        selectedIndex = i;
    }

    public int getItemCount() {
        return squareViewList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemSquareBinding binding;
        public ViewHolder(ItemSquareBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.imageViewSquare.setVisibility(View.GONE);
            binding.getRoot().setOnClickListener(view -> {
                selectedIndex = getAdapterPosition();
                backgroundListener.onBackgroundSelected((SquareView) squareViewList.get(selectedIndex));
                notifyDataSetChanged();
            });
        }
    }

    public static class SquareView {
        public Drawable drawable;
        public int drawableId;
        public boolean isBitmap;
        public boolean isColor;
        public String text;

        SquareView(int i, String str) {
            drawableId = i;
            text = str;
        }

        public SquareView(int i, String str, boolean z) {
            drawableId = i;
            text = str;
            isColor = z;
        }

        public SquareView(int i, String str, boolean z, boolean z2, Drawable drawable2) {
            drawableId = i;
            text = str;
            isColor = z;
            isBitmap = z2;
            drawable = drawable2;
        }
    }
}
