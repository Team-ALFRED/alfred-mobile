package edu.utexas.seniordesign.alfred.models;

public class Delivery {
    private String item;
    private Float[] location;

    public Delivery() {}

    public Delivery(String item, Float[] loc) {
        setItem(item);
        setLocation(loc);
    }

    public void setItem(String v) {
        this.item = v;
    }

    public String getItem() {
        return item;
    }

    public void setLocation(Float[] v) {
        this.location = v;
    }

    public Float[] getLocation() {
        return location;
    }

    public String toString() {
        return String.format("%s [%d, %d]", item, location[0], location[1]);
    }
}
