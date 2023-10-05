package com.photo.collagemaker.entity;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.photo.collagemaker.picker.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class PhotoDirectory implements Comparable<PhotoDirectory> {
    private String coverPath;
    private long dateAdded;

    private String id;
    private String name;
    private List<Photo> photos = new ArrayList();

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PhotoDirectory)) {
            return false;
        }
        PhotoDirectory photoDirectory = (PhotoDirectory) obj;
        boolean z = !TextUtils.isEmpty(id);
        boolean isEmpty = true ^ TextUtils.isEmpty(photoDirectory.id);
        if (!z || !isEmpty || !TextUtils.equals(id, photoDirectory.id)) {
            return false;
        }
        return TextUtils.equals(name, photoDirectory.name);
    }

    public int hashCode() {
        if (!TextUtils.isEmpty(id)) {
            int hashCode = id.hashCode();
            if (TextUtils.isEmpty(name)) {
                return hashCode;
            }
            return (hashCode * 31) + name.hashCode();
        } else if (TextUtils.isEmpty(name)) {
            return 0;
        } else {
            return name.hashCode();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String str) {
        id = str;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String str) {
        coverPath = str;
    }

    public String getName() {
        return name;
    }

    public void setName(String str) {
        name = str;
    }


    public void setDateAdded(long j) {
        dateAdded = j;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> list) {
        if (list != null) {
            int size = list.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                Photo photo = list.get(i);
                if (photo == null || !FileUtils.fileIsExists(photo.getPath())) {
                    list.remove(i);
                } else {
                    i++;
                }
            }
            photos = list;
        }
    }

    public List<String> getPhotoPaths() {
        ArrayList arrayList = new ArrayList(photos.size());
        for (Photo path : photos) {
            arrayList.add(path.getPath());
        }
        return arrayList;
    }

    public void addPhoto(int i, String str) {
        if (FileUtils.fileIsExists(str)) {
            photos.add(new Photo(i, str));
        }
    }

    public int compareTo(@NonNull PhotoDirectory photoDirectory) {
        try {
            return name.compareTo(photoDirectory.getName());
        } catch (Exception e) {
            return 1;
        }
    }
}
