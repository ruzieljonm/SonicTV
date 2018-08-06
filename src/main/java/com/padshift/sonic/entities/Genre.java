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
