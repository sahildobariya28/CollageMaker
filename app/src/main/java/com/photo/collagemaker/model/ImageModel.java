package com.photo.collagemaker.model;

import androidx.annotation.NonNull;

public class ImageModel implements Comparable<ImageModel> {

    int id;
    String name;
    String pathFile;
    String pathFolder;

    public ImageModel(String str, String str2, String str3) {
        this.name = str;
        this.pathFile = str2;
        this.pathFolder = str3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public String getPathFolder() {
        return pathFolder;
    }

    public void setPathFolder(String pathFolder) {
        this.pathFolder = pathFolder;
    }

    public int compareTo(@NonNull ImageModel imageModel) {
        return this.pathFolder.compareTo(imageModel.getPathFolder());
    }
}
