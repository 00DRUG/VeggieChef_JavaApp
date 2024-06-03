package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection connect() {
        String url = "jdbc:sqlite:C:\\Users\\gurie\\IdeaProjects\\VeggieChef_Maven\\veggiechef.db";  // Replace with your actual path
        Connection conn = null;
        try {
            System.out.println("Connecting to database at: " + url);  // Print the database path
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public List<Recipe> getPopularRecipes() {
        String sql = "SELECT id, name, duration, difficulty, author FROM Recipes";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String duration = rs.getString("duration");
                String difficulty = rs.getString("difficulty");
                String author = rs.getString("author");
                recipes.add(new Recipe(id, name, duration, difficulty, author));
                System.out.println("Fetched Recipe: " + name + ", " + duration + ", " + difficulty + ", " + author);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public List<Chef> getAllChefs() {
        String sql = "SELECT * FROM Chef";
        List<Chef> chefs = new ArrayList<>();

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Chef chef = new Chef(rs.getInt("id"), rs.getString("name"));
                chefs.add(chef);
                System.out.println("Fetched Chef: " + chef.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chefs;
    }

    public List<Recipe> getRecipesByChefName(String chefName) {
        String sql = "SELECT id, name, duration, difficulty, author FROM Recipes WHERE author = ?";
        List<Recipe> recipes = new ArrayList<>();

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, chefName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Recipe recipe = new Recipe(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("duration"),
                        rs.getString("difficulty"),
                        rs.getString("author")
                );
                recipes.add(recipe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipes;
    }

    public String getRecipeByQRCode(String qrCode) {
        String sql = "SELECT Recipes.recipe FROM Recipes JOIN FoodPacks ON Recipes.packId = FoodPacks.id WHERE FoodPacks.qrCode = ?";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, qrCode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("recipe");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Object> search(String searchString) {
        List<Object> results = new ArrayList<>();

        // Search in Recipes table
        String recipeSql = "SELECT id, name, duration, difficulty, author FROM Recipes WHERE name LIKE ? OR duration LIKE ? OR difficulty LIKE ? OR author LIKE ?";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(recipeSql)) {
            String searchPattern = "%" + searchString + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Recipe recipe = new Recipe(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("duration"),
                        rs.getString("difficulty"),
                        rs.getString("author")
                );
                results.add(recipe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Search in Chef table
        String chefSql = "SELECT id, name FROM Chef WHERE name LIKE ?";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(chefSql)) {
            pstmt.setString(1, "%" + searchString + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Chef chef = new Chef(rs.getInt("id"), rs.getString("name"));
                results.add(chef);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}
