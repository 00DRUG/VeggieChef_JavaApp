package org.example;

public class Chef {
    private int id;
    private String name;

    public Chef(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Chef: " + name;
    }
}
