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
        MenuItem saveMenuItem = new MenuItem("Save");
        MenuItem exitMenuItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(newEntryMenuItem, saveMenuItem, exitMenuItem);

        // Edit Menu
        Menu editMenu = new Menu("Edit");
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

    public static void main(String[] args) {
        launch(args);
    }
}
