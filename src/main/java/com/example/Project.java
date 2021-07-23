package com.example;

import java.sql.Date;
import java.util.ArrayList;

public class Project {
    private int id;
    private String name;
    private Date start;
    private Date end;
    private Double workload;
    private ArrayList<Date> Weeks;

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

    public void setWorkLoad(Double workload) {
        this.workload = workload;
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

    public Double getWorkLoad() {
        return this.workload;
    }

}