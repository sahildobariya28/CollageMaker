package com.photo.collagemaker.activities.editor.collage_editor.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.databinding.ItemGridItemToolBinding;
import com.photo.collagemaker.module.Module;

import java.util.ArrayList;
import java.util.List;

public class GridItemToolsAdapter extends RecyclerView.Adapter<GridItemToolsAdapter.ViewHolder> {

    public List<ToolModel> mToolList = new ArrayList<>();

    public OnPieceFuncItemSelected onPieceFuncItemSelected;

    public interface OnPieceFuncItemSelected {
        void onPieceFuncSelected(Module toolType);
    }

    public GridItemToolsAdapter(OnPieceFuncItemSelected onPieceFuncItemSelected2) {
        onPieceFuncItemSelected = onPieceFuncItemSelected2;
        mToolList.add(new ToolModel("Change", R.drawable.img_replace, Module.REPLACE));
        mToolList.add(new ToolModel("Vertical", R.drawable.img_vertical, Module.V_FLIP));
        mToolList.add(new ToolModel("Horizontal", R.drawable.img_horizontal, Module.H_FLIP));
        mToolList.add(new ToolModel("Rotate", R.drawable.img_rotate, Module.ROTATE));
        mToolList.add(new ToolModel("Crop", R.drawable.img_crop, Module.CROP));
        mToolList.add(new ToolModel("Filter", R.drawable.img_filter, Module.FILTER));
        mToolList.add(new ToolModel("Dodge", R.drawable.ic_dodge, Module.DODGE));
        mToolList.add(new ToolModel("Divide", R.drawable.ic_divide, Module.DIVIDE));
        mToolList.add(new ToolModel("Burn", R.drawable.ic_burn, Module.BURN));
    }

    class ToolModel {

        public int mToolIcon;

        public String mToolName;

        public Module mToolType;

        ToolModel(String str, int i, Module toolType) {
            mToolName = str;
            mToolIcon = i;
            mToolType = toolType;
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemGridItemToolBinding binding = ItemGridItemToolBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
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
        ItemGridItemToolBinding binding;
        ViewHolder(ItemGridItemToolBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(view1 -> {
                ToolModel toolModel = mToolList.get(getLayoutPosition());
                Module toolType = toolModel.mToolType;
                onPieceFuncItemSelected.onPieceFuncSelected(toolType);
            });
        }
    }
}
