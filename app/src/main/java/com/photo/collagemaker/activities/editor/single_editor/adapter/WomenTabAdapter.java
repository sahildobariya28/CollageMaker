package com.photo.collagemaker.activities.editor.single_editor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.photo.collagemaker.R;
import com.photo.collagemaker.databinding.ItemTabStickerBinding;


public class WomenTabAdapter extends RecyclerTabLayout.Adapter<WomenTabAdapter.ViewHolder> {
    private Context context;
    private PagerAdapter mAdapater = mViewPager.getAdapter();

    public WomenTabAdapter(ViewPager viewPager, Context context2) {
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
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_crowns));
                break;
            case 1:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_snsla));
                break;
            case 2:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_hala9at));
                break;
            case 3:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_flow));
                break;
            case 4:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_glasses));
                break;
            case 5:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_chap));
                break;
            case 6:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_hairs));
                break;
            case 7:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_smil));
                break;
            case 8:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_hjban));
                break;
            case 9:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_chfer));
                break;
            case 10:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_zwaq));
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
            binding.getRoot().setOnClickListener(view -> getViewPager().setCurrentItem(getAdapterPosition()));
        }
    }
}
