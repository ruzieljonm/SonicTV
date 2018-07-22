package com.padshift.sonic.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ruzieljonm on 22/07/2018.
 */

@Entity
@Table(name="userhistory")
public class UserHistory implements Serializable{

    @Id
    @Column(name="userId")
    private int userId;

    @Column(name="videoid")
    private float videoid;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getVideoid() {
        return videoid;
    }

    public void setVideoid(float videoid) {
        this.videoid = videoid;
    }
}
