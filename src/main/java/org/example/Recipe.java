package org.example;

public class Recipe {
    private int id;
    private String name;
    private String duration;
    private String difficulty;
    private String author;

    public Recipe(int id, String name, String duration, String difficulty, String author) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.difficulty = difficulty;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return name + "\n" + duration + " • " + difficulty + " • by " + author;
    }
}
