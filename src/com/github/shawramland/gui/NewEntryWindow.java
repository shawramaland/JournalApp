package com.github.shawramland.gui;

import com.github.shawramland.services.DatabaseService;
import com.github.shawramland.Entry;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.web.HTMLEditor;
public class NewEntryWindow {

    private static TextField titleField;
    private static HTMLEditor contentField;

    private static Entry currentEditingEntry;

    public static void display(Entry entryToEdit) {
        currentEditingEntry = entryToEdit;
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(entryToEdit != null ? "Edit Entry" : "New Entry");
        window.setMinWidth(500); // Adjusted width to accommodate HTMLEditor
        window.setMinHeight(400); // Adjusted height

        Label label = new Label();
        label.setText("Enter your new journal entry below:");

        // Creating two text fields
        titleField = new TextField();
        titleField.setPromptText("Title");

        contentField = new HTMLEditor();
        if(entryToEdit != null) {
            titleField.setText(entryToEdit.getTitle());
            contentField.setHtmlText(entryToEdit.getContent());
        }

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
        String css = NewEntryWindow.class.getResource("styles.css").toExternalForm();
        scene.getStylesheets().add(css);

        window.setScene(scene);
        window.showAndWait();

    }
    public static void saveEntry() {
        String title = titleField.getText();
        String content = contentField.getHtmlText();

        if(title.isEmpty() || content.isEmpty()) {
            // Show some error message of being empty
            System.out.println("Title or content is empty");
            return;
        }

        if(currentEditingEntry != null) {
            currentEditingEntry.setTitle(title);
            currentEditingEntry.setContent(content);
            DatabaseService.updateEntry(currentEditingEntry.getId(), title, content);
            System.out.println("Entry Updated Successfully.");
        } else {
            boolean isSaved = DatabaseService.addEntry(1, title, content);
            if(isSaved) {
                System.out.println("Entry saved successfully");
            } else {
                System.out.println("An error occurred while saving the entry.");
            }
        }
        MainWindow.refreshListView();
    }
}
