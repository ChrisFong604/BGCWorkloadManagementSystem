package com.example;

import java.sql.Date;

public class Project {
    private String name;
    private Date start;
    private Date end;

    public void setName(String name) {
        this.name = name;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getName() {
        return this.name;
    }

    public Date getStart() {
        return this.start;
    }

    public Date getEnd() {
        return this.end;
    }
}