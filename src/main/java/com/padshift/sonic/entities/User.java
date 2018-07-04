package com.padshift.sonic.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by ruzieljonm on 26/06/2018.
 */

//@NamedQuery(name="user.findAll", query="SELECT t FROM user t")
@Entity
@Table(name="user")
public class User implements Serializable {

    @Id
    @Column(name="userId")
    private int userId;

    @Column(name="userName")
    private String userName;


    @Column(name="userPass")
    private String userPass;

    @Column(name="userEmail")
    private String userEmail;




    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }



}
