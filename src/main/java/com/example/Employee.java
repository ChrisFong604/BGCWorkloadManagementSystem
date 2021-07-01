package com.example;

import java.util.ArrayList;
import java.util.Date;

/***
 * A Bean class for the Employee database and a method to determine work
 * capacity ramp-up time for interns
 */

public class Employee {

    // PROPERTIES
    private String name;
    private String position; // Co-op or Intern (e.g QA, SW Dev, etc.)
    private String role; // eg. QA analyst, SW Dev, etc
    private String team;
    private Boolean status; // True -> confirmed, False -> projected
    private Float capacity;
    private String startdate;
    private String enddate;

    /*
     * Name Role Working Capacity (time-based) Start String projected/hired full
     * time/part time/co-op Co-op end date
     * 
     * METHODS
     * 
     * all getters
     */

    public Employee() {
    }

    public Employee(String name, String position, String role, String team, Boolean status, Float capacity,
            String startdate, String enddate) {
        this.name = name;
        this.position = position;
        this.role = role;
        this.team = team;
        this.status = status;
        this.capacity = capacity;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setCapacity(Float capacity) {
        this.capacity = capacity;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getName() {
        return this.name;
    }

    public String getPosition() {
        return this.position;
    }

    public String getRole() {
        return this.role;
    }

    public String getTeam() {
        return this.team;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Float getCapacity() {
        return this.capacity;
    }

    public String getStartDate() {
        return this.startdate;
    }

    public String getEndDate() {
        return this.enddate;
    }
}
