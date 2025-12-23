package com.example.readify;

import com.example.readify.Database.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentProfileController {

    @FXML
    private TextField nameField, emailField, phoneField, memberTypeField;

    @FXML
    private PasswordField currentPasswordField, newPasswordField;

    private String studentEmail; // to identify the student in DB

    // Call this method from dashboard when opening profile
    public void setStudentEmail(String email) {
        this.studentEmail = email;
        loadStudentInfo();
    }

    // Load student info from DB
    private void loadStudentInfo() {
        String sql = "SELECT * FROM members WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, studentEmail);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phone"));
                memberTypeField.setText(rs.getString("membership"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleStudentLogout(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/readify/launchScreen.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Handle password change
    @FXML
    private void handleChangePassword() {
        String current = currentPasswordField.getText().trim();
        String newPass = newPasswordField.getText().trim();

        if (current.isEmpty() || newPass.isEmpty()) {
            showAlert("Error", "Please fill both password fields.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Verify current password
            String checkSql = "SELECT password FROM members WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, studentEmail);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getString("password").equals(current)) {
                // Update password
                String updateSql = "UPDATE members SET password = ? WHERE email = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, newPass);
                updateStmt.setString(2, studentEmail);
                int updated = updateStmt.executeUpdate();
                if (updated > 0) {
                    showAlert("Success", "Password changed successfully!");
                    currentPasswordField.clear();
                    newPasswordField.clear();
                }
            } else {
                showAlert("Error", "Current password is incorrect!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Something went wrong.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void onCloseTab(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/readify/StudentDashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
