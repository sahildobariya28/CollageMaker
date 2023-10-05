package com.photo.collagemaker.entity;

public class Photo {

    private int id;
    private String path;

    public Photo(int i, String str) {
        this.id = i;
        this.path = str;
    }

    public Photo() {
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj instanceof Photo) && this.id == ((Photo) obj).id) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public int getId() {
        return id;
    }

    public void setId(int i) {
        this.id = i;
    }
}
