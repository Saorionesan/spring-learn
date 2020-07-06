package com.epoint.redis.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Student
{
    private Integer ID;
    private String userName;
    private Integer rank;
    //使用对应的格式存入redis中
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date date;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getDate() {
        return date;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public void setDate(Date date) {
        this.date = date;
    }

    public Student(Integer ID, String userName, Integer rank,Date date) {
        this.ID = ID;
        this.userName = userName;
        this.rank = rank;
        this.date=date;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Student() {
    }
}
