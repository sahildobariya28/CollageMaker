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
            case 3:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_hair));
                break;
            case 4:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_glasses));
                break;
            case 5:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_moustache));
                break;
            case 6:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_lhya));
                break;
            case 7:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_scarf));
                break;
            case 8:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_tie));
                break;
            case 9:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_tatoo));
                break;
            case 10:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_chain));
                break;
            case 11:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_crowns));
                break;
            case 12:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_snsla));
                break;
            case 13:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_hala9at));
                break;
            case 14:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_flow));
                break;
            case 15:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_glasses));
                break;
            case 16:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_chap));
                break;
            case 17:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_hairs));
                break;
            case 18:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_smil));
                break;
            case 19:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_hjban));
                break;
            case 20:
                viewHolder.binding.image.setImageDrawable(context.getDrawable(R.drawable.ic_chfer));
                break;
            case 21:
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

            binding.getRoot().setOnClickListener(view1 -> getViewPager().setCurrentItem(getAdapterPosition()));
        }
    }
}
