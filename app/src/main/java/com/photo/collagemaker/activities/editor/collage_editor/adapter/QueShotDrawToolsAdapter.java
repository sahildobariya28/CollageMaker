package com.photo.collagemaker.activities.editor.collage_editor.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.R;
import com.photo.collagemaker.databinding.ItemEffetToolBinding;
import com.photo.collagemaker.module.Module;

import java.util.ArrayList;
import java.util.List;

public class QueShotDrawToolsAdapter extends RecyclerView.Adapter<QueShotDrawToolsAdapter.ViewHolder> {

    public OnQuShotDrawItemSelected onQuShotDrawItemSelected;
    public List<ModuleModel> toolModelArrayList = new ArrayList<>();

    public interface OnQuShotDrawItemSelected {
        void onQuShotDrawToolSelected(Module module);
    }

    public QueShotDrawToolsAdapter(OnQuShotDrawItemSelected onItemSelected) {
        onQuShotDrawItemSelected = onItemSelected;
        toolModelArrayList.add(new ModuleModel("Paint", R.drawable.ic_neon, Module.PAINT));
        toolModelArrayList.add(new ModuleModel("Neon", R.drawable.ic_paint, Module.NEON));
        toolModelArrayList.add(new ModuleModel("Mosaic", R.drawable.ic_mosaic, Module.MOSAIC));
        toolModelArrayList.add(new ModuleModel("Colored", R.drawable.ic_colored, Module.COLORED));
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
        ItemEffetToolBinding binding  = ItemEffetToolBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
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


            binding.getRoot().setOnClickListener(view1 ->
                    onQuShotDrawItemSelected.onQuShotDrawToolSelected((toolModelArrayList.get(getLayoutPosition())).toolType));
        }
    }
}
