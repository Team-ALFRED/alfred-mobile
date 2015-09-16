package edu.utexas.seniordesign.alfred.models;

public class Item {
    private String name;
    private int quantity;
    private String error;

    public Item() {}

    public void setName(String v) {
        this.name = v;
    }

    public String getName() {
        return name;
    }

    public void setQuantity(int v) {
        this.quantity = v;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setError(String v) { this.error = v; }

    public String getError() { return error; }

    public String toString() {
        return String.format("%s [%d]", name, quantity);
    }
}
