package com.example;

import java.sql.Date;

public class Project {
    private int id;
    private String name;
    private Date start;
    private Date end;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProjectStart(Date projectStart) {
        this.projectStart = projectStart;
    }

    public void setProjectEnd(Date projectEnd) {
        this.projectEnd = projectEnd;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Date getProjectStart() {
        return this.projectStart;
    }

    public Date getProjectEnd() {
        return this.projectEnd;
    }
}