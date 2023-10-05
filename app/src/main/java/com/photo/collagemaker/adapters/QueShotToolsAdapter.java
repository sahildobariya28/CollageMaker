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

public class QueShotToolsAdapter extends RecyclerView.Adapter<QueShotToolsAdapter.ViewHolder> {

    public OnQuShotItemSelected onQuShotItemSelected;
    public List<ModuleModel> toolModelArrayList = new ArrayList<>();

    public interface OnQuShotItemSelected {
        void onQuShotToolSelected(Module module);
    }

    public QueShotToolsAdapter(OnQuShotItemSelected onItemSelected) {
        this.onQuShotItemSelected = onItemSelected;
        toolModelArrayList.add(new ModuleModel("Crop", R.drawable.ic_crop, Module.CROP));
        toolModelArrayList.add(new ModuleModel("Filter", R.drawable.icon_filter, Module.FILTER));
        toolModelArrayList.add(new ModuleModel("Adjust", R.drawable.ic_adjust, Module.ADJUST));
        toolModelArrayList.add(new ModuleModel("Overlay", R.drawable.ic_overlay, Module.OVERLAY));
        toolModelArrayList.add(new ModuleModel("Ratio", R.drawable.ic_ratio, Module.RATIO));
        toolModelArrayList.add(new ModuleModel("Text", R.drawable.ic_text, Module.TEXT));
        toolModelArrayList.add(new ModuleModel("Sticker", R.drawable.icon_sticker, Module.EMOJI));
        toolModelArrayList.add(new ModuleModel("Blur", R.drawable.ic_blur, Module.BLUR));
        toolModelArrayList.add(new ModuleModel("Rotate", R.drawable.ic_rotate, Module.ROTATE));
        toolModelArrayList.add(new ModuleModel("s-Splash", R.drawable.ic_splash_square, Module.SQUARESPLASH));
        toolModelArrayList.add(new ModuleModel("Draw", R.drawable.ic_paint, Module.DRAW));
        toolModelArrayList.add(new ModuleModel("Frame", R.drawable.ic_frame, Module.BACKGROUND));
        toolModelArrayList.add(new ModuleModel("Splash", R.drawable.ic_splash, Module.SPLASH));
        toolModelArrayList.add(new ModuleModel("s-Blur", R.drawable.ic_blur_square, Module.SQUAEBLUR));
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
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_editing, viewGroup, false));
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
            image_view_tool_icon = view.findViewById(R.id.image_view_tool_icon);
            text_view_tool_name = view.findViewById(R.id.text_view_tool_name);
            relative_layout_wrapper_tool = view.findViewById(R.id.relative_layout_wrapper_tool);
            relative_layout_wrapper_tool.setOnClickListener(view1 ->
                    onQuShotItemSelected.onQuShotToolSelected((toolModelArrayList.get(getLayoutPosition())).toolType));
        }
    }
}
