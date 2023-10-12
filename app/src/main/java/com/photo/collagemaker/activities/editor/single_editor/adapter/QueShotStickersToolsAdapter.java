package com.photo.collagemaker.activities.editor.single_editor.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.databinding.ItemEffetToolBinding;
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
        ItemEffetToolBinding binding = ItemEffetToolBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new ViewHolder(binding);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ModuleModel toolModel = toolModelArrayList.get(i);
        viewHolder.binding.textViewAdjustName.setText(toolModel.toolName);
        viewHolder.binding.imageViewAdjustIcon.setImageResource(toolModel.toolIcon);
    }

    public int getItemCount() {
        return toolModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemEffetToolBinding binding;
        ViewHolder(ItemEffetToolBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.linearLayoutWrapperAdjust.setOnClickListener(view1 ->
                    onQuShotDrawItemSelected.onQuShotStickerToolSelected((toolModelArrayList.get(getLayoutPosition())).toolType));
        }
    }
}
