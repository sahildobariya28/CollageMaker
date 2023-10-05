package com.photo.collagemaker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.module.Module;

import java.util.ArrayList;
import java.util.List;

public class GridToolsAdapter extends RecyclerView.Adapter<GridToolsAdapter.ViewHolder> {

    public OnItemSelected mOnItemSelected;

    public List<ToolModel> mToolList = new ArrayList<>();

    public interface OnItemSelected {
        void onToolSelected(Module toolType);
    }

    public GridToolsAdapter(OnItemSelected onItemSelected, boolean z) {
        this.mOnItemSelected = onItemSelected;
        mToolList.add(new ToolModel("Collage", R.drawable.icon_layout, Module.LAYER));
        mToolList.add(new ToolModel("Padding", R.drawable.icon_border, Module.PADDING));
        mToolList.add(new ToolModel("Ratio", R.drawable.ic_ratio, Module.RATIO));
        mToolList.add(new ToolModel("Background", R.drawable.icon_background, Module.GRADIENT));
        mToolList.add(new ToolModel("Filter", R.drawable.icon_filter, Module.FILTER));
        mToolList.add(new ToolModel("Sticker", R.drawable.icon_sticker, Module.STICKER));
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
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_grid_tools, viewGroup, false));
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
        RelativeLayout relative_layout_wrapper_tool;

        ViewHolder(View view) {
            super(view);
            image_view_tool_icon = view.findViewById(R.id.image_view_tool_icon);
            text_view_tool_name = view.findViewById(R.id.text_view_tool_name);
            relative_layout_wrapper_tool = view.findViewById(R.id.relative_layout_wrapper_tool);
            relative_layout_wrapper_tool.setOnClickListener(view1 -> mOnItemSelected.onToolSelected((mToolList.get(getLayoutPosition())).mToolType));
        }
    }
}
