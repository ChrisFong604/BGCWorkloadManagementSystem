package com.example;

import java.sql.Date;
import java.util.ArrayList;

/***
 * A Bean class for the Employee database and a method to determine work
 * capacity ramp-up time for interns
 */

public class Employee {

    // PROPERTIES
    private String id;
    private String name;
    private String position; // Co-op/Intern or Permanent/Full-Time hire
    private String role; // eg. QA analyst, SW Dev, etc
    private String team;
    private Boolean status; // True -> confirmed, False -> projected
    private Date start;
    private Date end;

    private ArrayList<Double> rampUp;

    /*
     * Name Role Working Capacity (time-based) Start String projected/hired full
     * time/part time/co-op Co-op end date
     * 
     * METHODS
     * 
     * all getters
     */

    public void setId(String id) {
        this.id = id;
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

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setRampUp(ArrayList<Double> rampUp) {
        this.rampUp = rampUp;
    }

    public String getId() {
        return this.id;
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

    public Date getStart() {
        return this.start;
    }

    public Date getEnd() {
        return this.end;
    }

    public ArrayList<Double> getRampUp() {
        return this.rampUp;
    }
}
