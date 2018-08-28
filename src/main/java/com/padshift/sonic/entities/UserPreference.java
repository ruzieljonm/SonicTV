package com.padshift.sonic.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ruzieljonm on 19/07/2018.
 */
@Entity
@Table(name="userpreference")
public class UserPreference implements Serializable {

    @Id
    @GeneratedValue
    @Column(name="userprefId")
    private int userprefid;


    @Column(name="userId")
    private int userId;

    @Column(name="genreId")
    private int genreId;

    @Column(name="prefWeight")
    private float prefWeight;


    public int getUserprefid() {
        return userprefid;
    }

    public void setUserprefid(int userprefid) {
        this.userprefid = userprefid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public float getPrefWeight() {
        return prefWeight;
    }

    public void setPrefWeight(float prefWeight) {
        this.prefWeight = prefWeight;
    }
}
