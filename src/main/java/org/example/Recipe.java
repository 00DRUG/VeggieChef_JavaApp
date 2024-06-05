package org.example;

public class Recipe {
    private int id;
    private String name;
    private String duration;
    private String difficulty;
    private String author;
    private int favorite;
    private String ingredients;
    private String instructions;
    private String nutritions;

    public Recipe(int id, String name, String duration, String difficulty, String author, int favorite, String ingredients, String instructions, String nutritions) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.difficulty = difficulty;
        this.author = author;
        this.favorite = favorite;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.nutritions = nutritions;
    }
    public Recipe(int id, String name, String duration, String difficulty, String author, int favorite) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.difficulty = difficulty;
        this.author = author;
        this.favorite = favorite;

    }

    // Getters
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

    public int getFavorite() {
        return favorite;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getNutritions() {
        return nutritions;
    }

    // Setters
    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return name + " (" + duration + ", " + difficulty +", "+ author + ")" ;
    }
}
