package com.photo.collagemaker.activities.editor.collage_editor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.photo.collagemaker.R;
import com.photo.collagemaker.activities.editor.single_editor.adapter.RecyclerTabLayout;
import com.photo.collagemaker.databinding.ItemTabStickerBinding;


public class StickerTabAdapter extends RecyclerTabLayout.Adapter<StickerTabAdapter.ViewHolder> {
    private Context context;
    private PagerAdapter mAdapater = mViewPager.getAdapter();

    public StickerTabAdapter(ViewPager viewPager, Context context2) {
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
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.img_smily));
                break;
            case 1:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.img_india));
                break;
            case 2:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.img_boom));
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
