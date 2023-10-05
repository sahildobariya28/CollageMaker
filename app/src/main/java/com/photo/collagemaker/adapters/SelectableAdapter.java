package com.photo.collagemaker.adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.photo.collagemaker.entity.Photo;
import com.photo.collagemaker.entity.PhotoDirectory;
import com.photo.collagemaker.event.Selectable;

import java.util.ArrayList;
import java.util.List;

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements Selectable {
    private static final String TAG = "SelectableAdapter";
    public int currentDirectoryIndex = 0;
    protected List<PhotoDirectory> photoDirectories = new ArrayList();
    protected List<String> selectedPhotos = new ArrayList();

    public boolean isSelected(Photo photo) {
        return getSelectedPhotos().contains(photo.getPath());
    }

    public void toggleSelection(Photo photo) {
        if (selectedPhotos.contains(photo.getPath())) {
            selectedPhotos.remove(photo.getPath());
        } else {
            selectedPhotos.add(photo.getPath());
        }
    }

    public void clearSelection() {
        selectedPhotos.clear();
    }

    public int getSelectedItemCount() {
        return selectedPhotos.size();
    }

    public void setCurrentDirectoryIndex(int i) {
        currentDirectoryIndex = i;
    }

    public List<Photo> getCurrentPhotos() {
        if (photoDirectories.size() <= currentDirectoryIndex) {
            currentDirectoryIndex = photoDirectories.size() - 1;
        }
        return photoDirectories.get(currentDirectoryIndex).getPhotos();
    }

    public List<String> getCurrentPhotoPaths() {
        ArrayList arrayList = new ArrayList(getCurrentPhotos().size());
        for (Photo path : getCurrentPhotos()) {
            arrayList.add(path.getPath());
        }
        return arrayList;
    }

    public List<String> getSelectedPhotos() {
        return selectedPhotos;
    }
}
