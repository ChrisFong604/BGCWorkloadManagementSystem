package com.example;

public class RampUp {
    private double week1;
    private double week2;
    private double week3;
    private double week4;
    private double week5;

    // ft --> 0.1 0.25 0.5 0.875 0.875
    // coop --> 0.1 0.25 0.4 0.65 0.65

    public void setWeek1(double w1) {
        this.week1 = w1;
    }

    public void setWeek2(double w2) {
        this.week2 = w2;
    }

    public void setWeek3(double w3) {
        this.week3 = w3;
    }

    public void setWeek4(double w4) {
        this.week4 = w4;
    }

    public void setWeek5(double w5) {
        this.week5 = w5;
    }

    public double getWeek1() {
        return this.week1;
    }

    public double getWeek2() {
        return this.week2;
    }

    public double getWeek3() {
        return this.week3;
    }

    public double getWeek4() {
        return this.week4;
    }

    public double getWeek5() {
        return this.week5;
    }

}