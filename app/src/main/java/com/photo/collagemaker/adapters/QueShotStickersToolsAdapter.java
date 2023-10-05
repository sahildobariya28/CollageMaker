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

public class QueShotStickersToolsAdapter extends RecyclerView.Adapter<QueShotStickersToolsAdapter.ViewHolder> {

    public OnQuShotStickerItemSelected onQuShotDrawItemSelected;
    public List<ModuleModel> toolModelArrayList = new ArrayList<>();

    public interface OnQuShotStickerItemSelected {
        void onQuShotStickerToolSelected(Module module);
    }

    public QueShotStickersToolsAdapter(OnQuShotStickerItemSelected onItemSelected) {
        onQuShotDrawItemSelected = onItemSelected;
        toolModelArrayList.add(new ModuleModel("Clothes", R.drawable.ic_women, Module.BEAUTY));
        toolModelArrayList.add(new ModuleModel("Emoji", R.drawable.ic_emoji, Module.STICKER));
        toolModelArrayList.add(new ModuleModel("Style", R.drawable.ic_men, Module.MACKUER));
    }

    class ModuleModel {
        public int toolIcon;
        public String toolName;
        public Module toolType;

        ModuleModel(String str, int i, Module toolType) {
            this.toolName = str;
            this.toolIcon = i;
            this.toolType = toolType;
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_effet_tool, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ModuleModel toolModel = toolModelArrayList.get(i);
        viewHolder.text_view_tool_name.setText(toolModel.toolName);
        viewHolder.image_view_tool_icon.setImageResource(toolModel.toolIcon);
    }

    public int getItemCount() {
        return toolModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view_tool_icon;
        TextView text_view_tool_name;
        RelativeLayout relative_layout_wrapper_tool;

        ViewHolder(View view) {
            super(view);
            image_view_tool_icon = view.findViewById(R.id.image_view_adjust_icon);
            text_view_tool_name = view.findViewById(R.id.text_view_adjust_name);
            relative_layout_wrapper_tool = view.findViewById(R.id.linear_layout_wrapper_adjust);
            relative_layout_wrapper_tool.setOnClickListener(view1 ->
                    onQuShotDrawItemSelected.onQuShotStickerToolSelected((toolModelArrayList.get(getLayoutPosition())).toolType));
        }
    }
}
