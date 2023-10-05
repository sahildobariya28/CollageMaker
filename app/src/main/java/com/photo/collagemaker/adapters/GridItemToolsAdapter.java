package com.photo.collagemaker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
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
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_grid_item_tool, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ToolModel toolModel = mToolList.get(i);
        viewHolder.text_view_tool_name.setText(toolModel.mToolName);
        viewHolder.image_view_tool_icon.setImageResource(toolModel.mToolIcon);
    }

    public int getItemCount() {
        return mToolList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view_tool_icon;
        TextView text_view_tool_name;

        ViewHolder(View view) {
            super(view);
            image_view_tool_icon = view.findViewById(R.id.image_view_tool_icon);
            text_view_tool_name = view.findViewById(R.id.text_view_tool_name);
            view.setOnClickListener(view1 -> {
                ToolModel toolModel = mToolList.get(getLayoutPosition());
                Module toolType = toolModel.mToolType;
                onPieceFuncItemSelected.onPieceFuncSelected(toolType);
            });
        }
    }
}
