package com.zflabs.marveldatabase.data;

public class Comic {

    private int id;

    private String title;

    private String description;

    private Thumbnail thumbnail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl(){
        return thumbnail.path+"."+thumbnail.extension;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    @Override
    public String toString() {
        return "Comic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
