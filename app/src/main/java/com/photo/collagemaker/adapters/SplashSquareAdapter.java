package com.photo.collagemaker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.constants.Constants;
import com.photo.collagemaker.custom_view.SplashSticker;
import com.photo.collagemaker.assets.StickersAsset;
import com.photo.collagemaker.databinding.ItemSplashBinding;
import com.photo.collagemaker.utils.SystemUtil;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class SplashSquareAdapter extends RecyclerView.Adapter<SplashSquareAdapter.ViewHolder> {
    private int borderWidth;
    private Context context;

    public int selectedSquareIndex;

    public SplashChangeListener splashChangeListener;

    public List<SplashItem> splashList = new ArrayList();

    public interface SplashChangeListener {
        void onSelected(SplashSticker splashSticker);
    }

    public SplashSquareAdapter(Context context2, SplashChangeListener splashChangeListener, boolean z) {
        this.context = context2;
        this.splashChangeListener = splashChangeListener;
        this.borderWidth = SystemUtil.dpToPx(context2, Constants.BORDER_WIDTH);
        if (z) {
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m1.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f1.png")), R.drawable.num_1));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m2.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f2.png")), R.drawable.num_2));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m3.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f3.png")), R.drawable.num_3));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m4.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f4.png")), R.drawable.num_4));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m5.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f5.png")), R.drawable.num_5));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m6.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f6.png")), R.drawable.num_6));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m7.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f7.png")), R.drawable.num_7));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m8.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f8.png")), R.drawable.num_8));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m9.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f9.png")), R.drawable.num_9));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m10.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f10.png")), R.drawable.num_10));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m11.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f11.png")), R.drawable.num_11));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m12.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f12.png")), R.drawable.num_12));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m13.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f13.png")), R.drawable.num_13));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m14.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f14.png")), R.drawable.num_14));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m15.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f15.png")), R.drawable.num_15));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m16.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f16.png")), R.drawable.num_16));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m17.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f17.png")), R.drawable.num_17));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m18.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f18.png")), R.drawable.num_18));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m19.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f19.png")), R.drawable.num_19));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m20.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f20.png")), R.drawable.num_20));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m21.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f21.png")), R.drawable.num_21));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m22.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f22.png")), R.drawable.num_22));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m23.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f23.png")), R.drawable.num_23));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m24.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f24.png")), R.drawable.num_24));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m25.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f25.png")), R.drawable.num_25));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m26.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f26.png")), R.drawable.num_26));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m27.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f27.png")), R.drawable.num_27));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m28.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f28.png")), R.drawable.num_28));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m29.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f29.png")), R.drawable.num_29));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m30.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f30.png")), R.drawable.num_30));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m31.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f31.png")), R.drawable.num_31));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m32.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f32.png")), R.drawable.num_32));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m33.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f33.png")), R.drawable.num_33));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m34.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f34.png")), R.drawable.num_34));
            splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m35.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f35.png")), R.drawable.num_35));

            return;
        }
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m1.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f1.png")), R.drawable.num_1));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m2.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f2.png")), R.drawable.num_2));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m3.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f3.png")), R.drawable.num_3));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m4.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f4.png")), R.drawable.num_4));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m5.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f5.png")), R.drawable.num_5));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m6.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f6.png")), R.drawable.num_6));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m7.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f7.png")), R.drawable.num_7));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m8.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f8.png")), R.drawable.num_8));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m9.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f9.png")), R.drawable.num_9));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m10.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f10.png")), R.drawable.num_10));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m11.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f11.png")), R.drawable.num_11));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m12.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f12.png")), R.drawable.num_12));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m13.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f13.png")), R.drawable.num_13));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m14.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f14.png")), R.drawable.num_14));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m15.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f15.png")), R.drawable.num_15));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m16.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f16.png")), R.drawable.num_16));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m17.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f17.png")), R.drawable.num_17));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m18.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f18.png")), R.drawable.num_18));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m19.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f19.png")), R.drawable.num_19));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m20.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f20.png")), R.drawable.num_20));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m21.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f21.png")), R.drawable.num_21));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m22.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f22.png")), R.drawable.num_22));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m23.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f23.png")), R.drawable.num_23));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m24.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f24.png")), R.drawable.num_24));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m25.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f25.png")), R.drawable.num_25));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m26.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f26.png")), R.drawable.num_26));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m27.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f27.png")), R.drawable.num_27));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m28.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f28.png")), R.drawable.num_28));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m29.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f29.png")), R.drawable.num_29));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m30.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f30.png")), R.drawable.num_30));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m31.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f31.png")), R.drawable.num_31));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m32.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f32.png")), R.drawable.num_32));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m33.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f33.png")), R.drawable.num_33));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m34.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f34.png")), R.drawable.num_34));
        splashList.add(new SplashItem(new SplashSticker(StickersAsset.loadBitmapFromAssets(context2, "square/mask/m35.png"), StickersAsset.loadBitmapFromAssets(context2, "square/frame/f35.png")), R.drawable.num_35));
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemSplashBinding binding  = ItemSplashBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new ViewHolder(binding);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.binding.roundImageViewSplashItem.setImageResource(splashList.get(i).drawableId);
        if (selectedSquareIndex == i) {
            viewHolder.binding.roundImageViewSplashItem.setBorderColor(ContextCompat.getColor(context, R.color.colorAccent));
            viewHolder.binding.roundImageViewSplashItem.setBorderWidth(borderWidth);
            return;
        }
        viewHolder.binding.roundImageViewSplashItem.setBorderColor(0);
        viewHolder.binding.roundImageViewSplashItem.setBorderWidth(0);
    }

    public int getItemCount() {
        return splashList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemSplashBinding binding;
        public ViewHolder(ItemSplashBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(view -> {
                selectedSquareIndex = getAdapterPosition();
                if (selectedSquareIndex < 0) {
                    selectedSquareIndex = 0;
                }
                if (selectedSquareIndex >= splashList.size()) {
                    selectedSquareIndex = splashList.size() - 1;
                }
                splashChangeListener.onSelected((splashList.get(selectedSquareIndex)).splashSticker);
                notifyDataSetChanged();
            });
        }

    }

    class SplashItem {
        int drawableId;
        SplashSticker splashSticker;

        SplashItem(SplashSticker splashSticker2, int i) {
            this.splashSticker = splashSticker2;
            this.drawableId = i;
        }
    }
}
