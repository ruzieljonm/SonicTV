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

    @Column(name="title")
    private String title;

    @Column(name="artist")
    private String artist;


    @Column(name="date")
    private String date;

    @Column(name="genre")
    private String genre;

    @Column(name="sonicgenre")
    private String sonicgenre;

    @Column(name="viewCount")
    private String viewCount;

    @Column(name="likes")
    private String likes;


    @Column(name="dislikes")
    private String dislikes;


    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
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

    public String getSonicgenre() {
        return sonicgenre;
    }

    public void setSonicgenre(String sonicgenre) {
        this.sonicgenre = sonicgenre;
    }


    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }
}
