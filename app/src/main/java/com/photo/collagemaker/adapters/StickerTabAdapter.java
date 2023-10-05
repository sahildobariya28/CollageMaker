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


public class StickerTabAdapter extends RecyclerTabLayout.Adapter<StickerTabAdapter.ViewHolder> {
    private Context context;
    private PagerAdapter mAdapater = mViewPager.getAdapter();

    public StickerTabAdapter(ViewPager viewPager, Context context2) {
        super(viewPager);
        this.context = context2;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tab_sticker, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        switch (i) {
            case 0:
                viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.img_smily));
                break;
            case 1:
                viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.img_india));
                break;
            case 2:
                viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.img_boom));
                break;
        }
        viewHolder.imageView.setSelected(i == getCurrentIndicatorPosition());
    }

    public int getItemCount() {
        return mAdapater.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            view.setOnClickListener(view1 -> getViewPager().setCurrentItem(getAdapterPosition()));
        }
    }
}
