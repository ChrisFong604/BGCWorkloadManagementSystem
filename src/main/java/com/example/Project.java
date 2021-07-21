package com.example;

import java.sql.Date;
import java.util.ArrayList;

public class Project {
    private String name;
    private Date projectStart;
    private Date projectEnd;
    private ArrayList<Employee> assignedEmployees;

    // number of weeks is the size of the array
    // which holds the number of employees per week (e.g. [5,7,5] means 5 in week 1,
    // 7 in week 2, 5 in week 3)

    public Project() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProjectStart(Date projectStart) {
        this.projectStart = projectStart;
    }

    public void setProjectEnd(Date projectEnd) {
        this.projectEnd = projectEnd;
    }

    public void setAssignedEmployees(ArrayList<Employee> assignedEmployees) {
        this.assignedEmployees = assignedEmployees;
    }

    public String getName() {
        return this.name;
    }

    public Date getProjectStart() {
        return this.projectStart;
    }

    public Date getProjectEnd() {
        return this.projectEnd;
    }

    public ArrayList<Employee> getAssignedEmployees() {
        return this.assignedEmployees;
    }
}