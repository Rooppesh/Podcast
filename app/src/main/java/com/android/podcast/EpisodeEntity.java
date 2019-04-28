package com.android.podcast;

public class EpisodeEntity {

    String title;
    String description;
    String audio;
    String listennotes_url;
    String pub_date_ms;

    public String getAudio_length() {
        return audio_length;
    }

    public void setAudio_length(String audio_length) {
        this.audio_length = audio_length;
    }

    String thumbnail;
    String image;
    String audio_length;

    public EpisodeEntity(String title, String description, String audio, String listennotes_url, String pub_date_ms, String thumbnail, String image, String audio_length) {
        this.title = title;
        this.description = description;
        this.audio = audio;
        this.listennotes_url = listennotes_url;
        this.pub_date_ms = pub_date_ms;
        this.thumbnail = thumbnail;
        this.image = image;
        this.audio_length = audio_length;
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

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getListennotes_url() {
        return listennotes_url;
    }

    public void setListennotes_url(String listennotes_url) {
        this.listennotes_url = listennotes_url;
    }

    public String getPub_date_ms() {
        return pub_date_ms;
    }

    public void setPub_date_ms(String pub_date_ms) {
        this.pub_date_ms = pub_date_ms;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
