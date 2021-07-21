package com.example;

import java.sql.Date;
import java.util.ArrayList;

public class Project {
    private String name;
<<<<<<< HEAD
    private Date start;  
    private Date end;
    private ArrayList<Integer> workers;
=======
    private Date projectStart;
    private Date projectEnd;
    private Double workload;
    private ArrayList<Date> Weeks;
>>>>>>> e33863c76ee7afed94031fb6ac330d89b95e14dd
    //number of weeks is the size of the array 
    //which holds the number of employees per week (e.g. [5,7,5] means 5 in week 1, 7 in week 2, 5 in week 3)

    public Project() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart(Date start) {
        this.projectStart = start;
    }

<<<<<<< HEAD
    public void setWorkers(ArrayList<Integer> workers) {
        this.workers = workers;
=======
    public void setEnd(Date end) {
        this.projectEnd = end;
>>>>>>> e33863c76ee7afed94031fb6ac330d89b95e14dd
    }

    public void setEnd(Date end) {
        this.end = end;
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

    public ArrayList<Integer> getWorkers() {
        return this.workers;
    }

}