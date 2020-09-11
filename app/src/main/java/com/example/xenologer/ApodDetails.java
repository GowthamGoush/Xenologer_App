package com.example.xenologer;

public class ApodDetails {

    private String name;
    private String imageUrl;
    private String videoUrl;
    private String description;

    public ApodDetails(String name, String imageUrl, String videoUrl, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
