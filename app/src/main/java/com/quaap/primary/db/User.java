package com.quaap.primary.db;

import java.util.Date;

/**
 * Created by tom on 12/1/16.
 */

public class User {
    private int id;
    private String name;
    private String pass;
    private Date date;

    public User() {

    }

    public User(Date date, int id, String name, String pass) {
        this.date = date;
        this.id = id;
        this.name = name;
        this.pass = pass;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


}
