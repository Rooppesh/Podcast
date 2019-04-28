package com.android.podcast;

public class PodcastEntity {

    public PodcastEntity(String id, String publisher, String title, String tumbnail) {
        this.id = id;
        this.publisher = publisher;
        this.title = title;
        this.tumbnail = tumbnail;
    }

    String id;
    String publisher;
    String title;
    String tumbnail;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTumbnail() {
        return tumbnail;
    }

    public void setTumbnail(String tumbnail) {
        this.tumbnail = tumbnail;
    }


}
