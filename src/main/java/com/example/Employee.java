package com.example;

/***
 * A Bean class for the Employee database
 */

public class Employee {
    private String name;
    private String role;
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

    public Employee(String name, String role, Boolean status, Integer capacity, String startdate, String enddate) {
        this.name = name;
        this.role = role;
        this.status = status;
        this.capacity = capacity;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getRole() {
        return this.role;
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
}
