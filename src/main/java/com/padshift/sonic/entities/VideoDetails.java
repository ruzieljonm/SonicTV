package com.padshift.sonic.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by JUNSEM97 on 7/20/2018.
 */
@Entity
@Table(name="videodetails")
public class VideoDetails implements Serializable{
    @Id
    @Column(name="videoid")
    private String videoid;

    @Column(name="track")
    private String track;

    @Column(name="album")
    private String album;

    @Column(name="album date")
    private String date;

    @Column(name="genre")
    private String genre;

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
