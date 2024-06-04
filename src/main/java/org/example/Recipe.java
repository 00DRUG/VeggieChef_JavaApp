package org.example;

public class Recipe {
    private int id;
    private String name;
    private String duration;
    private String difficulty;
    private String author;
    private String category;  // New field for category
    private int favorite;

    public Recipe(int id, String name, String duration, String difficulty, String author, String category, int favorite) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.difficulty = difficulty;
        this.author = author;
        this.category = category;  // Initialize new field
        this.favorite = favorite;
    }

    // Getters and setters for all fields, including the new category field

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return name + " - " + duration + " - " + difficulty + " - " + author + " - " + category + " - " + (favorite == 1 ? "Favorite" : "Not Favorite");
    }
}
