package com.github.shawramland.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainWindow extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Main layout pane
        BorderPane rootLayout = new BorderPane();

        // Menu Bar
        MenuBar menuBar = new MenuBar();

        //File Menu
        Menu fileMenu = new Menu("File");
        MenuItem newEntryMenuItem = new MenuItem("New Entry");
        newEntryMenuItem.setOnAction(e -> NewEntryWindow.display());

        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.setOnAction(e -> saveEntry());

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(newEntryMenuItem, saveMenuItem, exitMenuItem);

        // Edit Menu
        Menu editMenu = new Menu("Edit");
        MenuItem editEntryMenuItem = new MenuItem("Edit Entry");
        editEntryMenuItem.setOnAction(e -> editEntry());

        MenuItem deleteMenuItem = new MenuItem("Delete Entry");
        deleteMenuItem.setOnAction(e -> deleteEntry());

        editMenu.getItems().addAll(editEntryMenuItem, deleteMenuItem);
        // Add items to edit menu as needed

        // Help Menu
        Menu helpMenu = new Menu("Help");
        //Add items to help menu as needed

        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        rootLayout.setTop(menuBar);

        // Set scene and stage
        Scene scene = new Scene(rootLayout, 800, 600);
        primaryStage.setTitle("Journey Journal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createNewEntry() {
        System.out.println("Creating a new Entry...");
    }

    private void saveEntry() {
        System.out.println("Saving the entry...");
    }

    private void editEntry() {
        System.out.println("Editing the entry...");
    }

    private void deleteEntry() {
        System.out.println("Deleting an entry...");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
