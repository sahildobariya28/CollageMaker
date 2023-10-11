package com.photo.collagemaker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.photo.collagemaker.R;
import com.photo.collagemaker.databinding.ItemTabStickerBinding;

public class MenTabAdapter extends RecyclerTabLayout.Adapter<MenTabAdapter.ViewHolder> {
    private Context context;
    private PagerAdapter mAdapater = mViewPager.getAdapter();

    public MenTabAdapter(ViewPager viewPager, Context context2) {
        super(viewPager);
        this.context = context2;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemTabStickerBinding binding = ItemTabStickerBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new ViewHolder(binding);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        switch (i) {
            case 0:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_hair));
                break;
            case 1:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_glasses));
                break;
            case 2:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_moustache));
                break;
            case 3:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_lhya));
                break;
            case 4:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_scarf));
                break;
            case 5:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_tie));
                break;
            case 6:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_tatoo));
                break;
            case 7:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_chain));
                break;
        }
        viewHolder.binding.image.setSelected(i == getCurrentIndicatorPosition());
    }

    public int getItemCount() {
        return mAdapater.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemTabStickerBinding binding;
        public ViewHolder(ItemTabStickerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(view1 -> getViewPager().setCurrentItem(getAdapterPosition()));
        }
    }
}
