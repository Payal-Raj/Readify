package com.example.readify;
import com.example.readify.Database.DBConnection;
import com.example.readify.Manager.MemberManager;
import com.example.readify.Models.Member;
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
    private PasswordField passwordField;

    @FXML
    private StackPane stackPane;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        membershipBox.getItems().addAll(
                "Student",
                "Teacher"
        );
        joinDatePicker.setValue(LocalDate.now());
    }

    public void saveMemberToDatabase(String name, String email, String phone, String address,
                                     String membership, LocalDate joinDate, String password) {
        String sql = "INSERT INTO members (name, email, phone, address, membership, join_date, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, phone);
            pst.setString(4, address);
            pst.setString(5, membership);
            pst.setDate(6, java.sql.Date.valueOf(joinDate));
            pst.setString(7, password);

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
        passwordField.clear();
        membershipBox.getSelectionModel().clearSelection();
        joinDatePicker.setValue(LocalDate.now());
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{10,13}");
    }

    @FXML
    private void handleSaveMember() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        String membership = membershipBox.getValue();
        LocalDate joinDate = joinDatePicker.getValue();
        String password = passwordField.getText().trim();

        if (name.isEmpty()) {
            showJFXAlert("Validation Error", "Full name is required.");
            return;
        }

        if (email.isEmpty()) {
            showJFXAlert("Validation Error", "Email is required.");
            return;
        }

        if (!isValidEmail(email)) {
            showJFXAlert("Validation Error", "Please enter a valid email address.");
            return;
        }

        if (phone.isEmpty()) {
            showJFXAlert("Validation Error", "Phone number is required.");
            return;
        }

        if (!isValidPhone(phone)) {
            showJFXAlert("Validation Error", "Phone number must contain 10–13 digits.");
            return;
        }

        if (address.isEmpty()) {
            showJFXAlert("Validation Error", "Address is required.");
            return;
        }

        if (membership == null) {
            showJFXAlert("Validation Error", "Please select a membership type.");
            return;
        }

        if (joinDate == null) {
            showJFXAlert("Validation Error", "Please select a joining date.");
            return;
        }

        if (joinDate.isAfter(LocalDate.now())) {
            showJFXAlert("Validation Error", "Joining date cannot be in the future.");
            return;
        }

        if (password.isEmpty()) {
            showJFXAlert("Validation Error", "Password is required.");
            return;
        }

        if (password.length() < 6) {
            showJFXAlert("Validation Error", "Password must be at least 6 characters.");
            return;
        }


        Member member = new Member(
                name,
                email,
                phone,
                address,
                membership,
                joinDate,
                password
        );
        if (!MemberManager.addMember(member)) {
            showJFXAlert("Error", "Member already exists.");
            return;
        }

        try {
            saveMemberToDatabase(name, email, phone, address, membership, joinDate, password);
            showJFXAlert("Success", "Member added successfully!");
            clearFields();
        } catch (Exception e) {
            showJFXAlert("Error", "Failed to add member. Please try again.");
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
