package com.zflabs.marveldatabase.data;

public class Thumbnail {

    public Thumbnail() {
    }

    public Thumbnail(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }

    String path;
    String extension;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
