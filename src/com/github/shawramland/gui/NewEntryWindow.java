package com.github.shawramland.gui;

import com.github.shawramland.services.DatabaseService;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
public class NewEntryWindow {

    private static TextField titleField;
    private static TextArea contentField;

    public static void display() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("New Entry");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText("Enter your new journal entry below:");

        // Creating two text fields
        titleField = new TextField();
        titleField.setPromptText("Title");

        contentField = new TextArea();
        contentField.setPromptText("Content");

        Button closeButton = new Button("Save and Close");
        closeButton.setOnAction(e -> {
            saveEntry();
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, titleField, contentField, closeButton);
        layout.setAlignment(Pos.CENTER);

        // Display widow and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
    public static void saveEntry() {
        String title = titleField.getText();
        String content = contentField.getText();

        if(title.isEmpty() || content.isEmpty()) {
            // Show some error message of being empty
            System.out.println("Title or content is empty");
            return;
        }

        boolean isSaved = DatabaseService.addEntry(1, title, content);

        if(isSaved) {
            System.out.println("Entry Saved Successfully.");
        } else {
            System.out.println("An error occurred while saving the entry.");
        }

    }
}
