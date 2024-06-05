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
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public List<Recipe> getPopularRecipes() {
        String sql = "SELECT id, name, duration, difficulty, author, favorite, ingredients, instructions, nutritions FROM recipes";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String duration = rs.getString("duration");
                String difficulty = rs.getString("difficulty");
                String author = rs.getString("author");
                int favorite = rs.getInt("favorite");
                String ingredients = rs.getString("ingredients");
                String instructions = rs.getString("instructions");
                String nutritions = rs.getString("nutritions");
                recipes.add(new Recipe(id, name, duration, difficulty, author, favorite, ingredients, instructions, nutritions));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public List<Recipe> getRecipesByCategory(String category) {
        String sql = "SELECT id, name, duration, difficulty, author, favorite, ingredients, instructions, nutritions FROM recipes WHERE category = ?";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String duration = rs.getString("duration");
                String difficulty = rs.getString("difficulty");
                String author = rs.getString("author");
                int favorite = rs.getInt("favorite");
                String ingredients = rs.getString("ingredients");
                String instructions = rs.getString("instructions");
                String nutritions = rs.getString("nutritions");
                recipes.add(new Recipe(id, name, duration, difficulty, author, favorite, ingredients, instructions, nutritions));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public List<String> getAllCategories() {
        String sql = "SELECT DISTINCT category FROM recipes";
        List<String> categories = new ArrayList<>();
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    public List<Recipe> getFavoriteRecipes() {
        String sql = "SELECT id, name, duration, difficulty, author, favorite, ingredients, instructions, nutritions FROM recipes WHERE favorite = 1";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String duration = rs.getString("duration");
                String difficulty = rs.getString("difficulty");
                String author = rs.getString("author");
                int favorite = rs.getInt("favorite");
                recipes.add(new Recipe(id, name, duration, difficulty, author, favorite));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public List<Object> search(String searchString) {
        String recipeSql = "SELECT id, name, duration, difficulty, author, favorite, ingredients, instructions, nutritions FROM recipes WHERE name LIKE ? OR author LIKE ? OR category LIKE ?";
        List<Object> results = new ArrayList<>();
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(recipeSql)) {
            pstmt.setString(1, "%" + searchString + "%");
            pstmt.setString(2, "%" + searchString + "%");
            pstmt.setString(3, "%" + searchString + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String duration = rs.getString("duration");
                String difficulty = rs.getString("difficulty");
                String author = rs.getString("author");
                int favorite = rs.getInt("favorite");
                results.add(new Recipe(id, name, duration, difficulty, author, favorite));
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

    public void updateFavoriteStatus(int recipeId, int favoriteStatus) {
        String sql = "UPDATE Recipes SET favorite = ? WHERE id = ?";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, favoriteStatus);
            pstmt.setInt(2, recipeId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Chef> getAllChefs() {
        String sql = "SELECT * FROM Chef";
        List<Chef> chefs = new ArrayList<>();

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Chef chef = new Chef(rs.getInt("id"), rs.getString("name"));
                chefs.add(chef);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chefs;
    }

    public List<Recipe> getRecipesByChefName(String chefName) {
        String sql = "SELECT id, name, duration, difficulty, author, favorite FROM Recipes WHERE author = ?";
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
                        rs.getString("author"),
                        rs.getInt("favorite")
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
}
