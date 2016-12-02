package com.quaap.primary.db;

/**
 * Created by tom on 12/1/16.
 */

public class ClassSubject {
    private int id;
    private String subject;
    private String level;

    public ClassSubject() {

    }

    public ClassSubject(int id, String subject, String level) {
        this.id = id;
        this.subject = subject;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


}
