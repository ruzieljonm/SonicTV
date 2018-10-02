package com.padshift.sonic.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ruzieljonm on 01/08/2018.
 */

@Entity
@Table(name="genre")
public class Genre implements Serializable{
    @Id
    @Column(name="genreId")
    private int genreId;

    @Column(name="genreName")
    private String genreName;

    @Column(name="genrePhoto")
    private String genrePhoto;

    @Column(name="explorePhoto")
    private String explorePhoto;


    public String getExplorePhoto() {
        return explorePhoto;
    }

    public void setExplorePhoto(String explorePhoto) {
        this.explorePhoto = explorePhoto;
    }

    public String getGenrePhoto() {
        return genrePhoto;
    }

    public void setGenrePhoto(String genrePhoto) {
        this.genrePhoto = genrePhoto;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }


}
