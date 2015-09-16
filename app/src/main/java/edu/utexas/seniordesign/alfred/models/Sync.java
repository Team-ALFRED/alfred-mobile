package edu.utexas.seniordesign.alfred.models;

import java.util.List;

public class Sync {
    private List<Item> inv;
    private List<String> map;

    public Sync() {}

    public void setInv(List<Item> v) {
        this.inv = v;
    }

    public List<Item> getInv() {
        return inv;
    }

    public void setMap(List<String> v) {
        this.map = v;
    }

    public List<String> getMap() {
        return map;
    }
}
