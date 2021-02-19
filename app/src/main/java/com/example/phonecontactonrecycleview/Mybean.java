package com.example.phonecontactonrecycleview;

public class Mybean {

    public Mybean()
    {}

    String name,number;
    boolean expanded;

    public Mybean(String name, String number) {
        this.name = name;
        this.number = number;
        this.expanded=expanded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
