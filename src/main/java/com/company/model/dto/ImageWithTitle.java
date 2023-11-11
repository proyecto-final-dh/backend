package com.company.model.dto;

public class ImageWithTitle {
    private String url;
    private String title;

    public ImageWithTitle(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public ImageWithTitle() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
