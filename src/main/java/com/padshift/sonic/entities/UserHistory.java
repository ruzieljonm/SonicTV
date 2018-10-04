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
    private int userId;

    @Column(name="videoid")
    private String videoid;

    @Column(name="seqid")
    private String seqid;

    public String getSeqid() {
        return seqid;
    }

    public void setSeqid(String seqid) {
        this.seqid = seqid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }
}
