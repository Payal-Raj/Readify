package com.example.readify;

import com.example.readify.Database.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {

    @FXML private TextField bookIdField;
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private ComboBox<String> categoryBox;
    @FXML private TextField publisherField;
    @FXML private TextField isbnField;
    @FXML private TextField copiesField;
    @FXML private ComboBox<String> statusBox;
    @FXML private DatePicker dateAddedPicker;

    @FXML private StackPane stackPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        categoryBox.getItems().addAll(
                "Programming",
                "Science",
                "Literature",
                "History",
                "Islamic Studies"
        );

        statusBox.getItems().addAll(
                "Available",
                "Issued"
        );

        dateAddedPicker.setValue(LocalDate.now());
    }


    public void addBook(String bookId, String title, String author, String category,
                        String publisher, String isbn, int copies,
                        String status, LocalDate dateAdded) {

        String sql = "INSERT INTO books " +
                "(book_id, title, author, category, publisher, isbn, total_copies, status, date_added) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, bookId);
            pst.setString(2, title);
            pst.setString(3, author);
            pst.setString(4, category);
            pst.setString(5, publisher);
            pst.setString(6, isbn);
            pst.setInt(7, copies);
            pst.setString(8, status);
            pst.setDate(9, java.sql.Date.valueOf(dateAdded));

            pst.executeUpdate();
            System.out.println("Book added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void clearFields() {
        bookIdField.clear();
        titleField.clear();
        authorField.clear();
        publisherField.clear();
        isbnField.clear();
        copiesField.clear();
        categoryBox.getSelectionModel().clearSelection();
        statusBox.getSelectionModel().clearSelection();
        dateAddedPicker.setValue(LocalDate.now());
    }


    @FXML
    private void handleSaveBook() {

        String bookId = bookIdField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        String category = categoryBox.getValue();
        String publisher = publisherField.getText();
        String isbn = isbnField.getText();
        String copiesText = copiesField.getText();
        String status = statusBox.getValue();
        LocalDate dateAdded = dateAddedPicker.getValue();


        if (bookId.isEmpty()) {
            showJFXAlert("Validation Error", "Please enter the Book ID.");
            return;
        }
        if (title.isEmpty()) {
            showJFXAlert("Validation Error", "Please enter the book title.");
            return;
        }
        if (author.isEmpty()) {
            showJFXAlert("Validation Error", "Please enter the author name.");
            return;
        }
        if (category == null) {
            showJFXAlert("Validation Error", "Please select a category.");
            return;
        }
        if (copiesText.isEmpty()) {
            showJFXAlert("Validation Error", "Please enter total copies.");
            return;
        }

        int copies;
        try {
            copies = Integer.parseInt(copiesText);
        } catch (NumberFormatException e) {
            showJFXAlert("Validation Error", "Total copies must be a number.");
            return;
        }

        if (status == null) {
            showJFXAlert("Validation Error", "Please select book status.");
            return;
        }
        if (dateAdded == null) {
            showJFXAlert("Validation Error", "Please select the date added.");
            return;
        }


        try {
            addBook(bookId, title, author, category, publisher, isbn, copies, status, dateAdded);
            showJFXAlert("Success", "Book added successfully!");
            clearFields();
        } catch (Exception e) {
            showJFXAlert("Error", "Failed to add book. Try again.");
            e.printStackTrace();
        }
    }


    @FXML
    public void handleAdminLogOut(MouseEvent event) {
        loadScene(event, "/com/example/readify/launchScreen.fxml");
    }

    @FXML
    public void onCloseTab(MouseEvent event) {
        loadScene(event, "/com/example/readify/adminDashboard.fxml");
    }

    private void loadScene(MouseEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showJFXAlert(String title, String message) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(title));
        content.setBody(new Text(message));

        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);

        JFXButton okButton = new JFXButton("OK");
        okButton.setStyle("-fx-background-color:#1ABC9C; -fx-text-fill:white; -fx-background-radius:5;");
        okButton.setOnAction(e -> dialog.close());

        content.setActions(okButton);
        dialog.show();
    }
}
