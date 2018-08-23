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
    @Column(name="userId")
    private int userId;

    @Column(name="genreId")
    private float genreId;

    @Column(name="prefWeight")
    private float prefWeight;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getGenreId() {
        return genreId;
    }

    public void setGenreId(float genreId) {
        this.genreId = genreId;
    }

    public float getPrefWeight() {
        return prefWeight;
    }

    public void setPrefWeight(float prefWeight) {
        this.prefWeight = prefWeight;
    }
}
