package edu.utexas.seniordesign.alfred.models;

public class Delivery {
    private String name;
    private String location;

    public Delivery() {}

    public Delivery(String name, String loc) {
        setName(name);
        setLocation(loc);
    }

    public void setName(String v) {
        this.name = v;
    }

    public String getName() {
        return name;
    }

    public void setLocation(String v) {
        this.location = v;
    }

    public String getLocation() {
        return location;
    }

    public String toString() {
        return String.format("%s [%s]", name, location);
    }
}
