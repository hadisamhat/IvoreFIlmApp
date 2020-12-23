package com.example.ivorefilmapp.Models;

public class BannerMovies {


    Integer id;
    String movieName;
    String description;
    String type;
    String imageUrl;
    String fileUrl;



    public BannerMovies(Integer id, String movieName, String description, String type, String imageUrl, String fileUrl) {
        this.id = id;
        this.movieName = movieName;
        this.description = description;
        this.type = type;
        this.imageUrl = imageUrl;
        this.fileUrl = fileUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
