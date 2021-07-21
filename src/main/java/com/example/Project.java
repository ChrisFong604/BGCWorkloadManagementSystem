package com.example;

import java.sql.Date;
import java.util.ArrayList;

public class Project {
    private String name;
    private Date start;  
    private Date end;
    private ArrayList<Integer> workers;
    //number of weeks is the size of the array 
    //which holds the number of employees per week (e.g. [5,7,5] means 5 in week 1, 7 in week 2, 5 in week 3)

    public Project() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setWorkers(ArrayList<Integer> workers) {
        this.workers = workers;
    }

    public void setEnd(Date end) {
        this.end = end;
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

    public ArrayList<Integer> getWorkers() {
        return this.workers;
    }

}