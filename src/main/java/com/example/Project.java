package com.example;

import java.sql.Date;
import com.fasterxml.jackson.databind.util.JSONPObject;

public class Project {
    private Integer id;
    private String name;
    private Date start;
    private Date end;

    // JSON in string format
    private String resources;
    private String capacities;
    private String capacities2;

    public void setId(Integer id) {
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

    public void setCapacities(String capacities) {
        this.capacities = capacities;
    }

    public void setCapacities2(String capacities2) {
        this.capacities2 = capacities2;
    }

    public Integer getId() {
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

    public String getCapacities() {
        return this.capacities;
    }

    public String getCapacities2() {
        return this.capacities2;
    }

}