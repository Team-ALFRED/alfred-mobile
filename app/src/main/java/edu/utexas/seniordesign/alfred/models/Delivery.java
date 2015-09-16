package edu.utexas.seniordesign.alfred.models;

public class Delivery {
    private String item;
    private String location;

    public Delivery() {}

    public Delivery(String item, String loc) {
        setItem(item);
        setLocation(loc);
    }

    public void setItem(String v) {
        this.item = v;
    }

    public String getItem() {
        return item;
    }

    public void setLocation(String v) {
        this.location = v;
    }

    public String getLocation() {
        return location;
    }

    public String toString() {
        return String.format("%s [%s]", item, location);
    }
}
