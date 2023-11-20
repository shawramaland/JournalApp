package com.github.shawramland.gui;

import com.github.shawramland.services.DatabaseService;
import com.github.shawramland.Entry;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.scene.web.WebView;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import java.io.File;
import java.io.IOException;

public class MainWindow extends Application {

    public static ListView<String> entryListView = new ListView<>();
    private final TextField searchField = new TextField();
    @Override
    public void start(Stage primaryStage) {

        // Main layout pane
        DatabaseService.initializeDatabase();
        BorderPane rootLayout = new BorderPane();

        WebView webView = new WebView();
        webView.getStyleClass().add("web-view");

        WebEngine webEngine = webView.getEngine();

        // Creating ListView to display Entries
        MainWindow.entryListView = new ListView<>();
        List<String> entryTitles = DatabaseService.getEntryTitles();
        entryListView.getItems().addAll(entryTitles);

        entryListView.setOnMouseClicked(e -> {
            String selectedEntryTitle = entryListView.getSelectionModel().getSelectedItem();
            Entry selectedEntry = DatabaseService.getEntryDetails(selectedEntryTitle);

            if(selectedEntry != null) {
                String fullContent = "<h1>" + selectedEntry.getTitle() + "</h1>" +
                                     "<div class='html-content'>" + selectedEntry.getContent() + "</div>" +
                                     "<p>Timestamp: " + selectedEntry.getTimeStamp() + "</p>";
                webEngine.loadContent(fullContent);
            }
        });

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(entryListView, webView);

        rootLayout.setCenter(splitPane);

        // Menu Bar
        MenuBar menuBar = new MenuBar();

        //File Menu
        Menu fileMenu = new Menu("File");
        MenuItem newEntryMenuItem = new MenuItem("New Entry");
        newEntryMenuItem.setOnAction(e -> NewEntryWindow.display(null));

        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.setOnAction(e -> saveEntry());

        // Export and Import menu

        MenuItem exportMenuItem = new MenuItem("Export Entries");
        exportMenuItem.setOnAction(e -> exportEntries(primaryStage));

        MenuItem importMenuItem = new MenuItem("Import Entries");
        importMenuItem.setOnAction(e -> importEntries(primaryStage));


        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(newEntryMenuItem, saveMenuItem, importMenuItem, exportMenuItem, exitMenuItem);

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
        Scene scene = new Scene(rootLayout, 1000, 800);

        URL cssURL = MainWindow.class.getResource("styles.css");
        if(cssURL != null) {
            String css = cssURL.toExternalForm();
            scene.getStylesheets().add(css);
        } else {
            System.out.println("Could not find CSS file: styles.css");
        }

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
            int entryId = DatabaseService.getEntryIdByTitle(selectedTitle);
            DatabaseService.deleteEntryById(entryId);
            refreshListView();
        }
    }

    private void performSearch() {
        String searchText = searchField.getText();
        List<String> searchResults = DatabaseService.searchEntryTitles(searchText);
        entryListView.getItems().setAll(searchResults);
    }

    private void exportEntries(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Entries");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Journal Files", "*.journal"));

        String selectedTitle = entryListView.getSelectionModel().getSelectedItem();
        if(selectedTitle != null) {
            fileChooser.setInitialFileName(selectedTitle + ".journal");

            File file = fileChooser.showSaveDialog(primaryStage);
            if(file != null) {
                try {
                    // Get the ID of the selected entry
                    int entryId = DatabaseService.getEntryIdByTitle(selectedTitle);
                    List<Integer> entryIds = new ArrayList<>();
                    entryIds.add(entryId);

                    // Pass both file and list of entry IDs
                    DatabaseService.exportEntriesToFile(file, entryIds);
                } catch (IOException e) {
                    showAlert("Export Failed", "Could not export entries: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } else {
            // Handle the case where no entry is selected
            showAlert("Export Failed", "No entry selected", Alert.AlertType.ERROR);
        }
    }

    private void importEntries(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Entries");
        File file = fileChooser.showOpenDialog(primaryStage);
        if(file != null) {
            try {
                DatabaseService.importEntriesFromFile(file);
                refreshListView();
            } catch (IOException | ClassNotFoundException e) {
                showAlert("Import Failed", "Could not import entries: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
