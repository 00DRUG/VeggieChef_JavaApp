package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class VeggieChefApp extends Application {

    private DatabaseManager dbManager = new DatabaseManager();
    private BorderPane mainLayout;
    private ComboBox<String> categoryComboBox;

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

        // Initialize category filter
        categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().add("ALL");
        categoryComboBox.getItems().addAll(dbManager.getAllCategories());
        categoryComboBox.setOnAction(e -> {
            String selectedCategory = categoryComboBox.getValue();
            if (selectedCategory.equals("ALL")) {
                showAllRecipesView();
            } else {
                showRecipesByCategory(selectedCategory);
            }
        });

        // Show initial view
        showMainView();

        Scene scene = new Scene(mainLayout, 470, 800);
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
        mainView.setMaxWidth(450);

        Label categoriesLabel = new Label("Categories");
        categoriesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox categoriesBox = createCategoriesSlider();

        Label popularRecipesLabel = new Label("Popular Recipes");
        popularRecipesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        VBox popularRecipesBox = new VBox(10);
        List<Recipe> popularRecipes = dbManager.getPopularRecipes();
        for (Recipe recipe : popularRecipes) {
            HBox recipeBox = new HBox(10);
            String imageName = "recipe" + recipe.getId() + ".png";
            ImageView recipeImage = new ImageView(loadImage(imageName));
            if (recipeImage.getImage().isError()) {
                System.out.println("Failed to load image: " + imageName);
            }
            recipeImage.setFitHeight(60);
            recipeImage.setFitWidth(60);
            Label recipeDetails = new Label(recipe.toString());
            recipeDetails.setWrapText(true);

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
        HBox topChefsBox = createChefsSlider();

        mainView.getChildren().addAll(categoriesLabel, categoriesBox, popularRecipesLabel, popularRecipesBox, topChefsLabel, topChefsBox);
        ScrollPane scrollPane = new ScrollPane(mainView);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        mainLayout.setCenter(scrollPane);
    }
    private HBox createChefsSlider() {
        HBox chefsBox = new HBox(10);
        chefsBox.setAlignment(Pos.CENTER_LEFT);
        chefsBox.setPadding(new Insets(10));

        List<Chef> chefs = dbManager.getAllChefs();
        for (Chef chef : chefs) {
            VBox chefBox = new VBox(5);
            chefBox.setAlignment(Pos.CENTER);
            ImageView chefImage = new ImageView(loadImage("chef" + chef.getId() + ".png"));
            chefImage.setFitHeight(80);
            chefImage.setFitWidth(80);
            Label chefName = new Label(chef.getName());
            chefBox.getChildren().addAll(chefImage, chefName);
            chefBox.setOnMouseClicked(e -> showRecipesByChef(chef));
            chefsBox.getChildren().add(chefBox);
        }

        ScrollPane scrollPane = new ScrollPane(chefsBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true); // Prevent vertical scrolling
        scrollPane.setPrefViewportWidth(400);
        scrollPane.setMinViewportHeight(120); // Set minimum height to prevent vertical scrolling

        Button leftButton = new Button("<");
        leftButton.setStyle("-fx-background-color: #FF6F00; -fx-text-fill: white;");
        leftButton.setOnAction(e -> scrollPane.setHvalue(scrollPane.getHvalue() - 0.1));

        Button rightButton = new Button(">");
        rightButton.setStyle("-fx-background-color: #FF6F00; -fx-text-fill: white;");
        rightButton.setOnAction(e -> scrollPane.setHvalue(scrollPane.getHvalue() + 0.1));

        HBox slider = new HBox(leftButton, scrollPane, rightButton);
        slider.setAlignment(Pos.CENTER);
        slider.setPrefHeight(140); // Adjust height as needed

        return slider;
    }
    private HBox createCategoriesSlider() {
        HBox categoriesBox = new HBox(10);
        categoriesBox.setAlignment(Pos.CENTER_LEFT);
        categoriesBox.setPadding(new Insets(10));

        List<String> categories = dbManager.getAllCategories();
        for (String category : categories) {
            VBox categoryBox = new VBox(5);
            categoryBox.setAlignment(Pos.CENTER);
            Label categoryLabel = new Label(category);
            categoryLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            ImageView categoryImage = new ImageView(loadImage("category" + categories.indexOf(category) + ".png"));
            categoryImage.setFitHeight(80);
            categoryImage.setFitWidth(80);
            categoryBox.getChildren().addAll(categoryLabel, categoryImage);
            categoryBox.setOnMouseClicked(e -> showRecipesByCategory(category));
            categoriesBox.getChildren().add(categoryBox);
        }

        ScrollPane scrollPane = new ScrollPane(categoriesBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true); // Prevent vertical scrolling
        scrollPane.setPrefViewportWidth(400);
        scrollPane.setMinViewportHeight(120); // Set minimum height to prevent vertical scrolling

        Button leftButton = new Button("<");
        leftButton.setStyle("-fx-background-color: #FF6F00; -fx-text-fill: white;");
        leftButton.setOnAction(e -> scrollPane.setHvalue(scrollPane.getHvalue() - 0.1));

        Button rightButton = new Button(">");
        rightButton.setStyle("-fx-background-color: #FF6F00; -fx-text-fill: white;");
        rightButton.setOnAction(e -> scrollPane.setHvalue(scrollPane.getHvalue() + 0.1));

        HBox slider = new HBox(leftButton, scrollPane, rightButton);
        slider.setAlignment(Pos.CENTER);
        slider.setPrefHeight(140); // Adjust height as needed

        return slider;
    }

    private void showRecipesByCategory(String category) {
        VBox recipesByCategoryView = new VBox(10);
        recipesByCategoryView.setPadding(new Insets(10));
        recipesByCategoryView.setMaxWidth(450);

        Label recipesByCategoryLabel = new Label("Recipes in " + category);
        recipesByCategoryLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox recipesBox = new VBox(10);
        List<Recipe> recipes = dbManager.getRecipesByCategory(category);
        if (recipes.isEmpty()) {
            recipesBox.getChildren().add(new Label("No recipes found in this category."));
        } else {
            for (Recipe recipe : recipes) {
                HBox recipeBox = new HBox(10);
                recipeBox.setPadding(new Insets(5));
                recipeBox.setStyle("-fx-border-color: #ddd; -fx-background-color: #f9f9f9; -fx-background-radius: 10px;");
                String imageName = "recipe" + recipe.getId() + ".png";
                ImageView recipeImage = new ImageView(loadImage(imageName));
                if (recipeImage.getImage().isError()) {
                    System.out.println("Failed to load image: " + imageName);
                }
                recipeImage.setFitHeight(60);
                recipeImage.setFitWidth(60);
                Label recipeDetails = new Label(recipe.toString());
                recipeDetails.setWrapText(true);

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

        recipesByCategoryView.getChildren().addAll(recipesByCategoryLabel, recipesBox);
        ScrollPane recipesScrollPane = new ScrollPane(recipesByCategoryView);
        recipesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        recipesScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        recipesScrollPane.setFitToWidth(true);
        recipesScrollPane.setFitToHeight(true);

        VBox combinedView = new VBox(createCategoryFilterBox(), recipesScrollPane); // Combine category filter and recipes
        combinedView.setSpacing(10);
        mainLayout.setCenter(combinedView);
    }

    private void showAllRecipesView() {
        VBox allRecipesView = new VBox(10);
        allRecipesView.setPadding(new Insets(10));
        allRecipesView.setMaxWidth(450);

        Label allRecipesLabel = new Label("All Recipes");
        allRecipesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Categories filter
        HBox categoriesFilterBox = createCategoryFilterBox();

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
                if (recipeImage.getImage().isError()) {
                    System.out.println("Failed to load image: " + imageName);
                }
                recipeImage.setFitHeight(60);
                recipeImage.setFitWidth(60);
                Label recipeDetails = new Label(recipe.toString());
                recipeDetails.setWrapText(true);

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

        allRecipesView.getChildren().addAll(allRecipesLabel, categoriesFilterBox, recipesBox);
        ScrollPane scrollPane = new ScrollPane(allRecipesView);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        VBox combinedView = new VBox(createCategoryFilterBox(), scrollPane); // Combine category filter and all recipes
        combinedView.setSpacing(10);
        mainLayout.setCenter(combinedView);
    }

    private HBox createCategoryFilterBox() {
        HBox categoriesFilterBox = new HBox(10);
        categoriesFilterBox.setAlignment(Pos.CENTER_LEFT);
        categoriesFilterBox.setPadding(new Insets(10));

        Label filterLabel = new Label("Filter by Category: ");
        categoriesFilterBox.getChildren().add(filterLabel);

        categoriesFilterBox.getChildren().add(categoryComboBox);
        return categoriesFilterBox;
    }

    private void applySearchFilters(String searchString) {
        VBox recipesView = new VBox(10);
        recipesView.setPadding(new Insets(10));
        recipesView.setMaxWidth(450);

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
                    if (recipeImage.getImage().isError()) {
                        System.out.println("Failed to load image: " + imageName);
                    }
                    recipeImage.setFitHeight(60);
                    recipeImage.setFitWidth(60);
                    Label recipeDetails = new Label(recipe.toString());
                    recipeDetails.setWrapText(true);

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
                    if (chefImage.getImage().isError()) {
                        System.out.println("Failed to load image: chef" + chef.getId() + ".png");
                    }
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
        ScrollPane scrollPane = new ScrollPane(recipesView);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        VBox combinedView = new VBox(createCategoryFilterBox(), scrollPane); // Combine category filter and search results
        combinedView.setSpacing(10);
        mainLayout.setCenter(combinedView);
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
        recipesByChefView.setMaxWidth(450);

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
                if (recipeImage.getImage().isError()) {
                    System.out.println("Failed to load image: recipe" + recipe.getId() + ".png");
                }
                recipeImage.setFitHeight(60);
                recipeImage.setFitWidth(60);
                Label recipeDetails = new Label(recipe.toString());
                recipeDetails.setWrapText(true);

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
        ScrollPane recipesScrollPane = new ScrollPane(recipesByChefView);
        recipesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        recipesScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        recipesScrollPane.setFitToWidth(true);
        recipesScrollPane.setFitToHeight(true);

        VBox combinedView = new VBox(createCategoryFilterBox(), recipesScrollPane); // Combine category filter and recipes by chef
        combinedView.setSpacing(10);
        mainLayout.setCenter(combinedView);
    }

    private void showFavoritesView() {
        VBox favoritesView = new VBox(10);
        favoritesView.setPadding(new Insets(10));
        favoritesView.setMaxWidth(450);

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
                if (recipeImage.getImage().isError()) {
                    System.out.println("Failed to load image: recipe" + recipe.getId() + ".png");
                }
                recipeImage.setFitHeight(60);
                recipeImage.setFitWidth(60);
                Label recipeDetails = new Label(recipe.toString());
                recipeDetails.setWrapText(true);

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
        ScrollPane scrollPane = new ScrollPane(favoritesView);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        VBox combinedView = new VBox(createCategoryFilterBox(), scrollPane); // Combine category filter and favorites
        combinedView.setSpacing(10);
        mainLayout.setCenter(combinedView);
    }

    private void showChefsView() {
        VBox chefsView = new VBox(10);
        chefsView.setPadding(new Insets(10));
        chefsView.setMaxWidth(450);

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
            if (chefImage.getImage().isError()) {
                System.out.println("Failed to load image: chef" + chef.getId() + ".png");
            }
            chefImage.setFitHeight(100);
            chefImage.setFitWidth(100);
            Label chefName = new Label(chef.getName());
            chefName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            chefBox.getChildren().addAll(chefImage, chefName);
            chefBox.setOnMouseClicked(e -> showRecipesByChef(chef));
            chefsBox.getChildren().add(chefBox);
        }

        chefsView.getChildren().addAll(chefsLabel, chefsBox);

        ScrollPane scrollPane = new ScrollPane(chefsView);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        mainLayout.setCenter(scrollPane);
    }



    private void showQRCodeView() {
        Label qrCodeLabel = new Label("QR Code View");
        VBox qrCodeView = new VBox(qrCodeLabel);
        qrCodeView.setPadding(new Insets(10));
        qrCodeView.setMaxWidth(450);
        VBox.setVgrow(qrCodeView, Priority.ALWAYS);
        ScrollPane scrollPane = new ScrollPane(qrCodeView);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        mainLayout.setCenter(scrollPane);
    }

    private Image loadImage(String imageName) {
        Image image = null;
        try {
            image = new Image(getClass().getResourceAsStream("/" + imageName));
            if (image.isError()) {
                System.err.println("Error loading image: " + imageName);
            }
        } catch (NullPointerException e) {
            System.err.println("Image not found: " + imageName);
        }
        return image;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

