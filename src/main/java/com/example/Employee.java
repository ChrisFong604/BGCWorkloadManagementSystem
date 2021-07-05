package com.example;

import java.sql.Date;

/***
 * A Bean class for the Employee database and a method to determine work
 * capacity ramp-up time for interns
 */

public class Employee {

    // PROPERTIES
    private int id;
    private String name;
    private String position; // Co-op or Intern (e.g QA, SW Dev, etc.)
    private String role; // eg. QA analyst, SW Dev, etc
    private String team;
    private Boolean status; // True -> confirmed, False -> projected
    private Float capacity;
    private Date start;
    private Date end;

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

    public Employee(int id, String name, String position, String role, String team, Boolean status, Float capacity, Date start,
            Date end) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.role = role;
        this.team = team;
        this.status = status;
        this.capacity = capacity;
        this.start = start;
        this.end = end;
    }

    public void setId(int id) {
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

    public void setCapacity(Float capacity) {
        this.capacity = capacity;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getId() {
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

    public Float getCapacity() {
        return this.capacity;
    }

    public Date getStart() {
        return this.start;
    }

    public Date getEnd() {
        return this.end;
    }

    
}
