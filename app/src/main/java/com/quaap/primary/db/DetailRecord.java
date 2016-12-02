package com.quaap.primary.db;

import java.util.Date;

/**
 * Created by tom on 12/1/16.
 */

public class DetailRecord {

    private ClassSubject classSubject;
    private User user;
    private Date date;
    private String problem;
    private String answer;
    private String userAnswer;
    private boolean correct;

    public DetailRecord() {
    }

    public DetailRecord(ClassSubject classSubject, User user, Date date, String problem, String answer, String userAnswer, boolean correct) {
        this.classSubject = classSubject;
        this.user = user;
        this.date = date;
        this.problem = problem;
        this.answer = answer;
        this.userAnswer = userAnswer;
        this.correct = correct;
    }

    public ClassSubject getClassSubject() {
        return classSubject;
    }

    public void setClassSubject(ClassSubject classSubject) {
        this.classSubject = classSubject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
