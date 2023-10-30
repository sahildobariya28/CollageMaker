package com.photo.collagemaker.activities.new_scrapbook;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.databinding.ItemGridToolsBinding;
import com.photo.collagemaker.module.Module;

import java.util.ArrayList;
import java.util.List;

public class ScrapBookToolsAdapter extends RecyclerView.Adapter<ScrapBookToolsAdapter.ViewHolder> {

    public OnItemSelected mOnItemSelected;

    public List<ToolModel> mToolList = new ArrayList<>();

    public interface OnItemSelected {
        void onToolSelected(Module toolType);
    }

    public ScrapBookToolsAdapter(OnItemSelected onItemSelected, boolean z) {
        this.mOnItemSelected = onItemSelected;
        mToolList.add(new ToolModel("Background", R.drawable.icon_background, Module.GRADIENT));
        mToolList.add(new ToolModel("Border", R.drawable.icon_border, Module.PADDING));
        mToolList.add(new ToolModel("Text", R.drawable.ic_text, Module.TEXT));
        mToolList.add(new ToolModel("Sticker", R.drawable.icon_sticker, Module.STICKER));
        mToolList.add(new ToolModel("Add", R.drawable.gallery_add, Module.ADDIMAGE));
        mToolList.add(new ToolModel("Filter", R.drawable.icon_filter, Module.FILTER));
        mToolList.add(new ToolModel("Draw", R.drawable.img_draw, Module.DRAW));
    }

    class ToolModel {

        public int mToolIcon;

        public String mToolName;

        public Module mToolType;

        ToolModel(String str, int i, Module toolType) {
            this.mToolName = str;
            this.mToolIcon = i;
            this.mToolType = toolType;
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ItemGridToolsBinding binding = ItemGridToolsBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new ViewHolder(binding);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ToolModel toolModel = mToolList.get(i);
        viewHolder.binding.textViewToolName.setText(toolModel.mToolName);
        viewHolder.binding.imageViewToolIcon.setImageResource(toolModel.mToolIcon);
    }

    public int getItemCount() {
        return mToolList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemGridToolsBinding binding;
        ViewHolder(ItemGridToolsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(view1 -> mOnItemSelected.onToolSelected((mToolList.get(getLayoutPosition())).mToolType));
        }
    }
}