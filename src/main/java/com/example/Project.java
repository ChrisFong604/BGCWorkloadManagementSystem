package com.example;

import java.sql.Date;
import java.util.ArrayList;

public class Project {
    private String name;
    private Date projectStart;
    private Date projectEnd;
    private Double workload;
    private ArrayList<Date> Weeks;

    public Project() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart(Date start) {
        this.projectStart = start;
    }

    public void setEnd(Date end) {
        this.projectEnd = end;
    }

    public void setWorkLoad(Double workload) {
        this.workload = workload;
    }

    public String getName() {
        return this.name;
    }

    public Date getProjectStart() {
        return this.projectStart;
    }

    public Date getEnd() {
        return this.projectEnd;
    }

    public Double getWorkLoad() {
        return this.workload;
    }

}