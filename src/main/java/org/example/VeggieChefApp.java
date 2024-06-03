package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class VeggieChefApp extends Application {

    private DatabaseManager dbManager = new DatabaseManager();
    private BorderPane mainLayout;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("VeggieChef");

        mainLayout = new BorderPane();

        // Top section
        HBox topSection = createTopSection();
        mainLayout.setTop(topSection);

        // Bottom section
        HBox bottomSection = createBottomSection();
        mainLayout.setBottom(bottomSection);

        // Show initial view
        showRecipesView();

        Scene scene = new Scene(mainLayout, 600, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createTopSection() {
        HBox profileBox = new HBox(10);

        // Profile picture
        ImageView profileImage = new ImageView(loadImage("profile.png"));
        profileImage.setFitHeight(40);
        profileImage.setFitWidth(40);
        Label greetingLabel = new Label("Hello, James!\nCheck Amazing Recipes...");

        profileBox.getChildren().addAll(profileImage, greetingLabel);
        return profileBox;
    }

    private HBox createBottomSection() {
        HBox bottomBox = new HBox(10);
        bottomBox.setPadding(new Insets(10));
        bottomBox.setSpacing(10);
        bottomBox.setStyle("-fx-background-color: #cccccc;");
        bottomBox.setAlignment(Pos.CENTER);

        Button mainButton = new Button("Main");
        mainButton.setOnAction(e -> showRecipesView());

        Button recipesButton = new Button("Recipes");
        recipesButton.setOnAction(e -> showRecipesView());

        Button chefsButton = new Button("Chefs");
        chefsButton.setOnAction(e -> showChefsView());

        Button qrCodeButton = new Button("QR Code");
        qrCodeButton.setOnAction(e -> showQRCodeView());

        bottomBox.getChildren().addAll(mainButton, recipesButton, chefsButton, qrCodeButton);
        return bottomBox;
    }

    private void showRecipesView() {
        VBox recipesView = new VBox(10);
        recipesView.setPadding(new Insets(10));

        Label recipesLabel = new Label("Recipes");

        // Search bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search by ingredient, category, or chef");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> applySearchFilters(searchBar.getText(), recipesView));

        HBox searchBox = new HBox(10, searchBar, searchButton);
        searchBox.setPadding(new Insets(10));
        searchBox.setAlignment(Pos.CENTER_LEFT);

        VBox resultsBox = new VBox(10);
        VBox.setVgrow(resultsBox, Priority.ALWAYS);

        recipesView.getChildren().addAll(recipesLabel, searchBox, resultsBox);
        mainLayout.setCenter(recipesView);
    }

    private void applySearchFilters(String searchString, VBox recipesView) {
        VBox resultsBox = (VBox) recipesView.getChildren().get(2);
        resultsBox.getChildren().clear();

        List<Object> results = dbManager.search(searchString);
        if (results.isEmpty()) {
            resultsBox.getChildren().add(new Label("No results found."));
        } else {
            for (Object result : results) {
                if (result instanceof Recipe) {
                    Recipe recipe = (Recipe) result;
                    HBox recipeBox = new HBox(10);
                    String imageName = "recipe" + recipe.getId() + ".png";
                    ImageView recipeImage = new ImageView(loadImage(imageName));
                    recipeImage.setFitHeight(60);
                    recipeImage.setFitWidth(60);
                    Label recipeDetails = new Label(recipe.toString());

                    recipeBox.getChildren().addAll(recipeImage, recipeDetails);
                    resultsBox.getChildren().add(recipeBox);
                } else if (result instanceof Chef) {
                    Chef chef = (Chef) result;
                    HBox chefBox = new HBox(10);
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
    }


    private void showRecipesByChef(Chef chef) {
        VBox recipesByChefView = new VBox(10);
        recipesByChefView.setPadding(new Insets(10));

        Label recipesByChefLabel = new Label("Recipes by " + chef.getName());

        VBox recipesBox = new VBox(10);
        List<Recipe> recipes = dbManager.getRecipesByChefName(chef.getName());
        if (recipes.isEmpty()) {
            recipesBox.getChildren().add(new Label("No recipes found for this chef."));
        } else {
            for (Recipe recipe : recipes) {
                HBox recipeBox = new HBox(10);
                String imageName = "recipe" + recipe.getId() + ".png";
                ImageView recipeImage = new ImageView(loadImage(imageName));
                recipeImage.setFitHeight(60);
                recipeImage.setFitWidth(60);
                Label recipeDetails = new Label(recipe.toString());

                recipeBox.getChildren().addAll(recipeImage, recipeDetails);
                recipesBox.getChildren().add(recipeBox);
            }
        }

        recipesByChefView.getChildren().addAll(recipesByChefLabel, recipesBox);
        mainLayout.setCenter(recipesByChefView);
    }

    private void showChefsView() {
        VBox chefsView = new VBox(10);
        chefsView.setPadding(new Insets(10));

        Label chefsLabel = new Label("Top Chefs");

        VBox chefsBox = new VBox(10);
        List<Chef> chefs = dbManager.getAllChefs();
        for (Chef chef : chefs) {
            HBox chefBox = new HBox(10);
            chefBox.setAlignment(Pos.CENTER_LEFT);
            chefBox.setStyle("-fx-border-color: black; -fx-padding: 5; -fx-background-color: #f0f0f0;");

            ImageView chefImage = new ImageView(loadImage("chef" + chef.getId() + ".png"));
            chefImage.setFitHeight(40);
            chefImage.setFitWidth(40);
            Label chefName = new Label(chef.getName());

            chefBox.getChildren().addAll(chefImage, chefName);
            chefBox.setOnMouseClicked(e -> showRecipesByChef(chef));
            chefsBox.getChildren().add(chefBox);
        }

        chefsView.getChildren().addAll(chefsLabel, chefsBox);
        mainLayout.setCenter(chefsView);
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
