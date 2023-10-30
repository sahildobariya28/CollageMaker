package com.photo.collagemaker.activities.material.util;

class PathModel {
        int width, height;
        String path;

        public PathModel(String path, int width, int height) {
            this.width = width;
            this.height = height;
            this.path = path;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }