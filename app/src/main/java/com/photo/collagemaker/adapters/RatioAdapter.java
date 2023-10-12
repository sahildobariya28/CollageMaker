package com.photo.collagemaker.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.assets.BrushColorAsset;
import com.photo.collagemaker.databinding.ItemSquareBinding;

import java.util.ArrayList;
import java.util.List;

public class RatioAdapter extends RecyclerView.Adapter<RatioAdapter.ViewHolder> {
    public RatioListener ratioListener;
    private Context context;
    public int selectedIndex;
    public List<SquareView> squareViews = new ArrayList();

    public interface RatioListener {
        void onBackgroundSelected(SquareView squareView);
    }

    public RatioAdapter(Context context2, RatioListener ratioListener) {
        this.context = context2;
        this.ratioListener = ratioListener;
        List<String> lstColorForBrush = BrushColorAsset.listColorBrush();
        for (int i = 0; i < lstColorForBrush.size() - 2; i++) {
            squareViews.add(new SquareView(Color.parseColor(lstColorForBrush.get(i)), "", true));
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemSquareBinding binding = ItemSquareBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new ViewHolder(binding);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SquareView squareView = squareViews.get(i);
        if (squareView.isColor) {
            viewHolder.binding.squareView.setBackgroundColor(squareView.drawableId);
        } else {
            viewHolder.binding.squareView.setBackgroundResource(squareView.drawableId);
        }
        if (selectedIndex == i) {
            viewHolder.binding.constraintLayoutWrapperSquareView.setBackground(context.getDrawable(R.drawable.border_view));
        } else {
            viewHolder.binding.constraintLayoutWrapperSquareView.setBackground(context.getDrawable(R.drawable.border_transparent_view));
        }
    }

    public int getItemCount() {
        return squareViews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemSquareBinding binding;
        public ViewHolder(ItemSquareBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


            binding.getRoot().setOnClickListener(this);
        }

        public void onClick(View view) {
            selectedIndex = getAdapterPosition();
            ratioListener.onBackgroundSelected(squareViews.get(selectedIndex));
            notifyDataSetChanged();
        }
    }

    public class SquareView {
        public int drawableId;
        public boolean isColor;
        public String text;

        SquareView(int i, String str) {
            this.drawableId = i;
            this.text = str;
        }

        SquareView(int i, String str, boolean z) {
            this.drawableId = i;
            this.text = str;
            this.isColor = z;
        }
    }
}
