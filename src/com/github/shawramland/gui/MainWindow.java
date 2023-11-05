package com.github.shawramland.gui;

import com.github.shawramland.services.DatabaseService;
import com.github.shawramland.Entry;
import javafx.application.Application;
import javafx.scene.Scene;
import java.util.List;
import javafx.geometry.Insets;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

public class MainWindow extends Application {

    public static ListView<String> entryListView = new ListView<>();
    private final TextField searchField = new TextField();
    @Override
    public void start(Stage primaryStage) {
        // Main layout pane
        DatabaseService.initializeDatabase();
        BorderPane rootLayout = new BorderPane();

        TextArea textArea = new TextArea();
        textArea.setEditable(false);

        // Creating ListView to display Entries
        MainWindow.entryListView = new ListView<>();
        List<String> entryTitles = DatabaseService.getEntryTitles();
        entryListView.getItems().addAll(entryTitles);

        entryListView.setOnMouseClicked(e -> {
            String selectedEntryTitle = entryListView.getSelectionModel().getSelectedItem();
            Entry selectedEntry = DatabaseService.getEntryDetails(selectedEntryTitle);

            if(selectedEntry != null) {
                textArea.setText("Title" + selectedEntry.getTitle() + "\n\n"
                                 + "Content: " + selectedEntry.getContent() + "\n\n"
                                 + "Timestamp: " + selectedEntry.getTimeStamp());
            }
        });

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(entryListView, textArea);

        rootLayout.setCenter(splitPane);

        // Menu Bar
        MenuBar menuBar = new MenuBar();

        //File Menu
        Menu fileMenu = new Menu("File");
        MenuItem newEntryMenuItem = new MenuItem("New Entry");
        newEntryMenuItem.setOnAction(e -> NewEntryWindow.display(null));

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

        // Search bar setup
        searchField.setPromptText("Search entries...");
        searchField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                performSearch();
            }
        });

        HBox searchBox = new HBox(searchField);
        searchBox.setPadding(new Insets(10, 10, 10, 10));

        VBox topContainer = new VBox(menuBar, searchBox);
        rootLayout.setTop(topContainer);

        // Set scene and stage
        Scene scene = new Scene(rootLayout, 800, 600);

        String css = MainWindow.class.getResource("styles.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setTitle("Journey Journal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void refreshListView() {
        entryListView.getItems().clear();
        List<String> entryTitles = DatabaseService.getEntryTitles();
        entryListView.getItems().addAll(entryTitles);
    }

    private void saveEntry() {
        System.out.println("Saving the entry...");
    }

    private void editEntry() {
        String selectedTitle = entryListView.getSelectionModel().getSelectedItem();
        if(selectedTitle != null) {
            Entry selectedEntry = DatabaseService.getEntryDetails(selectedTitle);
            NewEntryWindow.display(selectedEntry);
        }
    }

    private void deleteEntry() {
        String selectedTitle = entryListView.getSelectionModel().getSelectedItem();
        if(selectedTitle != null) {
            DatabaseService.deleteEntryByTitle(selectedTitle);
            refreshListView();
        }
    }

    private void performSearch() {
        String searchText = searchField.getText();
        List<String> searchResults = DatabaseService.searchEntryTitles(searchText);
        entryListView.getItems().setAll(searchResults);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
