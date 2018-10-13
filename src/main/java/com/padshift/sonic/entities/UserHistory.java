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
    @GeneratedValue
    @Column(name="whid")
    private int whid;

    @Column(name="userId")
    private String userId;

    @Column(name="userName")
    private String userName;

    @Column(name="videoid")
    private String videoid;

    @Column(name="seqid")
    private String seqid;

    @Column(name="vidRating")
    private String vidRating;

    public String getSeqid() {
        return seqid;
    }

    public void setSeqid(String seqid) {
        this.seqid = seqid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVidRating() {
        return vidRating;
    }

    public void setVidRating(String vidRating) {
        this.vidRating = vidRating;
    }
}
