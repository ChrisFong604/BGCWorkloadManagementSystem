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
    private Date startdate;
    private Date enddate;

    /*
     * Name Role Working Capacity (time-based) Start date projected/hired full
     * time/part time/co-op Co-op end date
     * 
     * METHODS
     * 
     * all getters
     */

    public Employee() {
    }

    public Employee(String name, String position, Boolean status, Integer capacity, Date startdate, Date enddate) {
        this.name = name;
        this.position = position;
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

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public void setEnddate(Date enddate) {
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

    public Date getStartDate() {
        return this.startdate;
    }

    public Date getEndDate() {
        return this.enddate;
    }
}
