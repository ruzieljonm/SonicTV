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

    @Column(name="pop")
    private float pop;


    @Column(name="classical")
    private float classical;

    @Column(name="country")
    private float country;

    @Column(name="rnb")
    private float rnb;

    @Column(name="electronic")
    private float electronic;

    @Column(name="rock")
    private float rock;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getPop() {
        return pop;
    }

    public void setPop(float pop) {
        this.pop = pop;
    }

    public float getClassical() {
        return classical;
    }

    public void setClassical(float classical) {
        this.classical = classical;
    }

    public float getCountry() {
        return country;
    }

    public void setCountry(float country) {
        this.country = country;
    }

    public float getRnb() {
        return rnb;
    }

    public void setRnb(float rnb) {
        this.rnb = rnb;
    }

    public float getElectronic() {
        return electronic;
    }

    public void setElectronic(float electronic) {
        this.electronic = electronic;
    }

    public float getRock() {
        return rock;
    }

    public void setRock(float rock) {
        this.rock = rock;
    }
}
