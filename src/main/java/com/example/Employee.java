package com.example;

import java.util.ArrayList;

/***
 * A Bean class for the Employee database and a method to determine work
 * capacity ramp-up time for interns
 */

public class Employee {

    // PROPERTIES
    private String name;
    private String position;
    private Boolean status; // True -> confirmed, False -> projected
    private Integer capacity;
    private String startdate;
    private String enddate;

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

    public Employee(String name, String position, Boolean status, Integer capacity, String startdate, String enddate) {
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

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setCapacity(Integer capacity) {
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

    public Boolean getStatus() {
        return this.status;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public String getStartDate() {
        return this.startdate;
    }

    public String getEndDate() {
        return this.enddate;
    }

    // Non-bean methods
    public ArrayList<String> rampPeriodHandler() throws Exception {
        if (this.position != "intern") {
            return null;
        }
        ArrayList<String> dates = new ArrayList<String>();

        return dates;
    }
}
