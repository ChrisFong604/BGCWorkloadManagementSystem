package com.example;

public class Property {
    private String filterBy;
    private String value;

    public void setFilterBy(String filter) {
        this.filterBy = filter;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFilterBy() {
        return this.filterBy;
    }

    public String getValue() {
        return this.value;
    }

}