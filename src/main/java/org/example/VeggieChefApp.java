package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class VeggieChefApp extends Application {

    private DatabaseManager dbManager = new DatabaseManager();
    private BorderPane mainLayout;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("VeggieChef");
        primaryStage.setResizable(false);

        mainLayout = new BorderPane();

        // Top section
        VBox topSection = createTopSection();
        mainLayout.setTop(topSection);

        // Bottom section
        HBox bottomSection = createBottomSection();
        mainLayout.setBottom(bottomSection);

        // Show initial view
        showMainView();

        Scene scene = new Scene(mainLayout, 450, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createTopSection() {
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(10));
        topSection.setStyle("-fx-background-color: #FF6F00; -fx-padding: 10px;");

        // Profile section
        HBox profileBox = new HBox(10);
        profileBox.setAlignment(Pos.CENTER_LEFT);

        // Profile picture
        ImageView profileImage = new ImageView(loadImage("logo.png"));
        profileImage.setFitHeight(40);
        profileImage.setFitWidth(40);
        Label greetingLabel = new Label("Hello, James!\nCheck Amazing Recipes...");
        greetingLabel.setTextFill(Color.WHITE);

        profileBox.getChildren().addAll(profileImage, greetingLabel);

        // Search bar
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_RIGHT);
        searchBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 20px; -fx-padding: 5px;");
        searchBox.setPadding(new Insets(5));

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search Any Recipe...");
        searchBar.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        HBox.setHgrow(searchBar, Priority.ALWAYS);

        Button searchButton = new Button();
        ImageView searchIcon = new ImageView(loadImage("search_icon.png"));
        searchIcon.setFitHeight(20);
        searchIcon.setFitWidth(20);
        searchButton.setGraphic(searchIcon);
        searchButton.setStyle("-fx-background-color: transparent;");
        searchButton.setOnAction(e -> {
            applySearchFilters(searchBar.getText());
        });

        Button showAllButton = new Button("Show All");
        showAllButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: transparent; -fx-text-fill: #FF6F00;");
        showAllButton.setOnAction(e -> showAllRecipesView());

        searchBox.getChildren().addAll(searchBar, searchButton, showAllButton);

        topSection.getChildren().addAll(profileBox, searchBox);
        return topSection;
    }

    private HBox createBottomSection() {
        HBox bottomBox = new HBox(10);
        bottomBox.setPadding(new Insets(10));
        bottomBox.setSpacing(10);
        bottomBox.setStyle("-fx-background-color: #FF6F00;");
        bottomBox.setAlignment(Pos.CENTER);

        Button homeButton = createBottomButton("Home", "home_icon.png");
        homeButton.setOnAction(e -> showMainView());

        Button browseButton = createBottomButton("Browse", "browse_icon.png");
        browseButton.setOnAction(e -> showAllRecipesView());

        Button favoriteButton = createBottomButton("Favorite", "favorite_icon.png");
        favoriteButton.setOnAction(e -> showFavoritesView());

        Button chefButton = createBottomButton("Chef", "chef_icon.png");
        chefButton.setOnAction(e -> showChefsView());

        Button profileButton = createBottomButton("Profile", "profile_icon.png");
        profileButton.setOnAction(e -> showQRCodeView());

        bottomBox.getChildren().addAll(homeButton, browseButton, favoriteButton, chefButton, profileButton);
        return bottomBox;
    }

    private Button createBottomButton(String text, String iconPath) {
        Button button = new Button(text);
        ImageView icon = new ImageView(loadImage(iconPath));
        icon.setFitHeight(20);
        icon.setFitWidth(20);
        button.setGraphic(icon);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        return button;
    }

    private void showMainView() {
        VBox mainView = new VBox(10);
        mainView.setPadding(new Insets(10));

        Label categoriesLabel = new Label("Categories");
        categoriesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        HBox categoriesBox = new HBox(10);
        categoriesBox.setAlignment(Pos.CENTER_LEFT);
        // Add dummy categories
        for (int i = 1; i <= 4; i++) {
            ImageView categoryImage = new ImageView(loadImage("category" + i + ".png"));
            categoryImage.setFitHeight(80);
            categoryImage.setFitWidth(80);
            categoriesBox.getChildren().add(categoryImage);
        }

        Label popularRecipesLabel = new Label("Popular Recipes");
        popularRecipesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        VBox popularRecipesBox = new VBox(10);
        List<Recipe> popularRecipes = dbManager.getPopularRecipes();
        for (Recipe recipe : popularRecipes) {
            HBox recipeBox = new HBox(10);
            String imageName = "recipe" + recipe.getId() + ".png";
            ImageView recipeImage = new ImageView(loadImage(imageName));
            recipeImage.setFitHeight(60);
            recipeImage.setFitWidth(60);
            Label recipeDetails = new Label(recipe.toString());

            // Favorite icon
            Button favoriteButton = new Button();
            ImageView favoriteIcon = new ImageView(loadImage(recipe.getFavorite() == 1 ? "filled_heart.png" : "unfilled_heart.png"));
            favoriteIcon.setFitHeight(20);
            favoriteIcon.setFitWidth(20);
            favoriteButton.setGraphic(favoriteIcon);
            favoriteButton.setStyle("-fx-background-color: transparent;");
            favoriteButton.setOnAction(e -> toggleFavorite(recipe, favoriteButton));

            recipeBox.getChildren().addAll(recipeImage, recipeDetails, favoriteButton);
            popularRecipesBox.getChildren().add(recipeBox);
        }

        Label topChefsLabel = new Label("Top Chefs");
        topChefsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        HBox topChefsBox = new HBox(10);
        topChefsBox.setAlignment(Pos.CENTER_LEFT);
        List<Chef> topChefs = dbManager.getAllChefs();
        for (Chef chef : topChefs) {
            VBox chefBox = new VBox(5);
            ImageView chefImage = new ImageView(loadImage("chef" + chef.getId() + ".png"));
            chefImage.setFitHeight(40);
            chefImage.setFitWidth(40);
            Label chefName = new Label(chef.getName());
            chefBox.getChildren().addAll(chefImage, chefName);
            topChefsBox.getChildren().add(chefBox);
        }

        mainView.getChildren().addAll(categoriesLabel, categoriesBox, popularRecipesLabel, popularRecipesBox, topChefsLabel, topChefsBox);
        mainLayout.setCenter(new ScrollPane(mainView));
    }

    private void showAllRecipesView() {
        VBox allRecipesView = new VBox(10);
        allRecipesView.setPadding(new Insets(10));

        Label allRecipesLabel = new Label("All Recipes");
        allRecipesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox recipesBox = new VBox(10);
        List<Recipe> recipes = dbManager.getPopularRecipes();
        if (recipes.isEmpty()) {
            recipesBox.getChildren().add(new Label("No recipes found."));
        } else {
            for (Recipe recipe : recipes) {
                HBox recipeBox = new HBox(10);
                recipeBox.setPadding(new Insets(5));
                recipeBox.setStyle("-fx-border-color: #ddd; -fx-background-color: #f9f9f9; -fx-background-radius: 10px;");
                String imageName = "recipe" + recipe.getId() + ".png";
                ImageView recipeImage = new ImageView(loadImage(imageName));
                recipeImage.setFitHeight(60);
                recipeImage.setFitWidth(60);
                Label recipeDetails = new Label(recipe.toString());

                // Favorite icon
                Button favoriteButton = new Button();
                ImageView favoriteIcon = new ImageView(loadImage(recipe.getFavorite() == 1 ? "filled_heart.png" : "unfilled_heart.png"));
                favoriteIcon.setFitHeight(20);
                favoriteIcon.setFitWidth(20);
                favoriteButton.setGraphic(favoriteIcon);
                favoriteButton.setStyle("-fx-background-color: transparent;");
                favoriteButton.setOnAction(e -> toggleFavorite(recipe, favoriteButton));

                recipeBox.getChildren().addAll(recipeImage, recipeDetails, favoriteButton);
                recipesBox.getChildren().add(recipeBox);
            }
        }

        allRecipesView.getChildren().addAll(allRecipesLabel, recipesBox);
        mainLayout.setCenter(new ScrollPane(allRecipesView));
    }

    private void applySearchFilters(String searchString) {
        VBox recipesView = new VBox(10);
        recipesView.setPadding(new Insets(10));

        Label recipesLabel = new Label("Recipes");
        recipesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox resultsBox = new VBox(10);
        List<Object> results = dbManager.search(searchString);
        if (results.isEmpty()) {
            resultsBox.getChildren().add(new Label("No results found."));
        } else {
            for (Object result : results) {
                if (result instanceof Recipe) {
                    Recipe recipe = (Recipe) result;
                    HBox recipeBox = new HBox(10);
                    recipeBox.setPadding(new Insets(5));
                    recipeBox.setStyle("-fx-border-color: #ddd; -fx-background-color: #f9f9f9; -fx-background-radius: 10px;");
                    String imageName = "recipe" + recipe.getId() + ".png";
                    ImageView recipeImage = new ImageView(loadImage(imageName));
                    recipeImage.setFitHeight(60);
                    recipeImage.setFitWidth(60);
                    Label recipeDetails = new Label(recipe.toString());

                    // Favorite icon
                    Button favoriteButton = new Button();
                    ImageView favoriteIcon = new ImageView(loadImage(recipe.getFavorite() == 1 ? "filled_heart.png" : "unfilled_heart.png"));
                    favoriteIcon.setFitHeight(20);
                    favoriteIcon.setFitWidth(20);
                    favoriteButton.setGraphic(favoriteIcon);
                    favoriteButton.setStyle("-fx-background-color: transparent;");
                    favoriteButton.setOnAction(e -> toggleFavorite(recipe, favoriteButton));

                    recipeBox.getChildren().addAll(recipeImage, recipeDetails, favoriteButton);
                    resultsBox.getChildren().add(recipeBox);
                } else if (result instanceof Chef) {
                    Chef chef = (Chef) result;
                    HBox chefBox = new HBox(10);
                    chefBox.setPadding(new Insets(5));
                    chefBox.setStyle("-fx-border-color: #ddd; -fx-background-color: #f9f9f9; -fx-background-radius: 10px;");
                    ImageView chefImage = new ImageView(loadImage("chef" + chef.getId() + ".png"));
                    chefImage.setFitHeight(40);
                    chefImage.setFitWidth(40);
                    Label chefDetails = new Label(chef.toString());

                    chefBox.getChildren().addAll(chefImage, chefDetails);
                    resultsBox.getChildren().add(chefBox);

                    chefBox.setOnMouseClicked(e -> showRecipesByChef(chef));
                }
            }
        }

        recipesView.getChildren().addAll(recipesLabel, resultsBox);
        mainLayout.setCenter(new ScrollPane(recipesView));
    }

    private void toggleFavorite(Recipe recipe, Button favoriteButton) {
        int newStatus = recipe.getFavorite() == 1 ? 0 : 1;
        dbManager.updateFavoriteStatus(recipe.getId(), newStatus);
        recipe.setFavorite(newStatus);
        ImageView newIcon = new ImageView(loadImage(newStatus == 1 ? "filled_heart.png" : "unfilled_heart.png"));
        newIcon.setFitHeight(20);
        newIcon.setFitWidth(20);
        favoriteButton.setGraphic(newIcon);
    }

    private void showRecipesByChef(Chef chef) {
        VBox recipesByChefView = new VBox(10);
        recipesByChefView.setPadding(new Insets(10));

        Label recipesByChefLabel = new Label("Recipes by " + chef.getName());
        recipesByChefLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox recipesBox = new VBox(10);
        List<Recipe> recipes = dbManager.getRecipesByChefName(chef.getName());
        if (recipes.isEmpty()) {
            recipesBox.getChildren().add(new Label("No recipes found for this chef."));
        } else {
            for (Recipe recipe : recipes) {
                HBox recipeBox = new HBox(10);
                recipeBox.setPadding(new Insets(5));
                recipeBox.setStyle("-fx-border-color: #ddd; -fx-background-color: #f9f9f9; -fx-background-radius: 10px;");
                ImageView recipeImage = new ImageView(loadImage("recipe" + recipe.getId() + ".png"));
                recipeImage.setFitHeight(60);
                recipeImage.setFitWidth(60);
                Label recipeDetails = new Label(recipe.toString());

                // Favorite icon
                Button favoriteButton = new Button();
                ImageView favoriteIcon = new ImageView(loadImage(recipe.getFavorite() == 1 ? "filled_heart.png" : "unfilled_heart.png"));
                favoriteIcon.setFitHeight(20);
                favoriteIcon.setFitWidth(20);
                favoriteButton.setGraphic(favoriteIcon);
                favoriteButton.setStyle("-fx-background-color: transparent;");
                favoriteButton.setOnAction(e -> toggleFavorite(recipe, favoriteButton));

                recipeBox.getChildren().addAll(recipeImage, recipeDetails, favoriteButton);
                recipesBox.getChildren().add(recipeBox);
            }
        }

        recipesByChefView.getChildren().addAll(recipesByChefLabel, recipesBox);
        mainLayout.setCenter(new ScrollPane(recipesByChefView));
    }

    private void showFavoritesView() {
        VBox favoritesView = new VBox(10);
        favoritesView.setPadding(new Insets(10));

        Label favoritesLabel = new Label("Favorites");
        favoritesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox favoritesBox = new VBox(10);
        List<Recipe> favoriteRecipes = dbManager.getFavoriteRecipes();
        if (favoriteRecipes.isEmpty()) {
            favoritesBox.getChildren().add(new Label("No favorite recipes found."));
        } else {
            for (Recipe recipe : favoriteRecipes) {
                HBox recipeBox = new HBox(10);
                recipeBox.setPadding(new Insets(5));
                recipeBox.setStyle("-fx-border-color: #ddd; -fx-background-color: #f9f9f9; -fx-background-radius: 10px;");
                ImageView recipeImage = new ImageView(loadImage("recipe" + recipe.getId() + ".png"));
                recipeImage.setFitHeight(60);
                recipeImage.setFitWidth(60);
                Label recipeDetails = new Label(recipe.toString());

                // Favorite icon (always filled for favorites view)
                Button favoriteButton = new Button();
                ImageView favoriteIcon = new ImageView(loadImage("filled_heart.png"));
                favoriteIcon.setFitHeight(20);
                favoriteIcon.setFitWidth(20);
                favoriteButton.setGraphic(favoriteIcon);
                favoriteButton.setStyle("-fx-background-color: transparent;");
                favoriteButton.setOnAction(e -> toggleFavorite(recipe, favoriteButton));

                recipeBox.getChildren().addAll(recipeImage, recipeDetails, favoriteButton);
                favoritesBox.getChildren().add(recipeBox);
            }
        }

        favoritesView.getChildren().addAll(favoritesLabel, favoritesBox);
        mainLayout.setCenter(new ScrollPane(favoritesView));
    }

    private void showChefsView() {
        VBox chefsView = new VBox(10);
        chefsView.setPadding(new Insets(10));

        Label chefsLabel = new Label("Top Chefs");
        chefsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox chefsBox = new VBox(10);
        List<Chef> chefs = dbManager.getAllChefs();
        for (Chef chef : chefs) {
            HBox chefBox = new HBox(10);
            chefBox.setAlignment(Pos.CENTER_LEFT);
            chefBox.setPadding(new Insets(5));
            chefBox.setStyle("-fx-border-color: #ddd; -fx-background-color: #f9f9f9; -fx-background-radius: 10px;");

            ImageView chefImage = new ImageView(loadImage("chef" + chef.getId() + ".png"));
            chefImage.setFitHeight(40);
            chefImage.setFitWidth(40);
            Label chefName = new Label(chef.getName());

            chefBox.getChildren().addAll(chefImage, chefName);
            chefBox.setOnMouseClicked(e -> showRecipesByChef(chef));
            chefsBox.getChildren().add(chefBox);
        }

        chefsView.getChildren().addAll(chefsLabel, chefsBox);
        mainLayout.setCenter(new ScrollPane(chefsView));
    }

    private void showQRCodeView() {
        Label qrCodeLabel = new Label("QR Code View");
        VBox qrCodeView = new VBox(qrCodeLabel);
        qrCodeView.setPadding(new Insets(10));
        VBox.setVgrow(qrCodeView, Priority.ALWAYS);
        mainLayout.setCenter(qrCodeView);
    }

    private Image loadImage(String imageName) {
        return new Image(getClass().getResourceAsStream("/" + imageName));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
