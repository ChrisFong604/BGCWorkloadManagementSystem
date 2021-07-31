package com.example;

import java.sql.Date;
import com.fasterxml.jackson.databind.util.JSONPObject;

public class Project {
    private int id;
    private String name;
    private Date start;
    private Date end;

    // JSON in string format
    private String resources;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public int getId() {
        return this.id;
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

    public String getResources() {
        return this.resources;
    }

}