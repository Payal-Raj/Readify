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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.scene.text.Text;

public class AddMemberController implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressField;
    @FXML private ComboBox<String> membershipBox;
    @FXML private DatePicker joinDatePicker;
    @FXML
    private StackPane stackPane;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        membershipBox.getItems().addAll(
                "Student",
                "Teacher",
                "General"
        );
        joinDatePicker.setValue(LocalDate.now());
    }

    public void addMember(String name, String email, String phone, String address,
                          String membership, LocalDate joinDate) {
        String sql = "INSERT INTO members (name, email, phone, address, membership, join_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, phone);
            pst.setString(4, address);
            pst.setString(5, membership);
            pst.setDate(6, java.sql.Date.valueOf(joinDate));

            pst.executeUpdate();
            System.out.println("Member added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        membershipBox.getSelectionModel().clearSelection();
        joinDatePicker.setValue(LocalDate.now());
    }


    @FXML
    private void handleSaveMember() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String membership = membershipBox.getValue();
        LocalDate joinDate = joinDatePicker.getValue();

        // Validation
        if (name.isEmpty()) {
            showJFXAlert("Validation Error", "Please enter the full name.");
            return;
        }
        if (email.isEmpty()) {
            showJFXAlert("Validation Error", "Please enter the email.");
            return;
        }
        if (phone.isEmpty()) {
            showJFXAlert("Validation Error", "Please enter the phone number.");
            return;
        }
        if (address.isEmpty()) {
            showJFXAlert("Validation Error", "Please enter the address.");
            return;
        }
        if (membership == null) {
            showJFXAlert("Validation Error", "Please select a membership type.");
            return;
        }
        if (joinDate == null) {
            showJFXAlert("Validation Error", "Please select the join date.");
            return;
        }

        // Save to DB
        try {
            addMember(name, email, phone, address, membership, joinDate);
            showJFXAlert("Success", "Member added successfully!");
            clearFields();
        } catch (Exception e) {
            showJFXAlert("Error", "Failed to add member. Try again.");
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
        // Create dialog content
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(title));
        content.setBody(new Text(message));

        // Create dialog
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);

        // Add OK button
        JFXButton okButton = new JFXButton("OK");
        okButton.setStyle("-fx-background-color:#1ABC9C; -fx-text-fill:white; -fx-background-radius:5;");
        okButton.setOnAction(event -> dialog.close()); // close dialog on click

        content.setActions(okButton);

        dialog.show();
    }

}
