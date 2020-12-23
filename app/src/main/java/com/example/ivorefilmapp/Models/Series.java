package com.example.ivorefilmapp.Models;

public class Series {

    String episodeName;
    String episodeLength;

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public String getEpisodeLength() {
        return episodeLength;
    }

    public void setEpisodeLength(String episodeLength) {
        this.episodeLength = episodeLength;
    }

    public Series(String episodeName, String episodeLength) {
        this.episodeName = episodeName;
        this.episodeLength = episodeLength;
    }
}
