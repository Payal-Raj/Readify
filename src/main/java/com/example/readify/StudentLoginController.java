package com.example.readify;
import com.example.readify.Database.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentLoginController {
    @FXML
    private TextField StudentUserName;

    @FXML
    private PasswordField StudentPassword;

    @FXML
    private StackPane rootPane;

    @FXML
    public void handleStudentLogin(ActionEvent event) {

        String username = StudentUserName.getText().trim();
        String password = StudentPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showErrorDialog("Validation Error", "Please enter username and password.");
            return;
        }

        String sql = "SELECT * FROM members WHERE email = ? AND password = ?";
        System.out.println("Username: " + username + ", Password: " + password);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/example/readify/StudentDashboard.fxml"));
                Parent root = loader.load();

                StudentDashboardController dashboardController = loader.getController();
                dashboardController.setStudentName(rs.getString("name")); // Assuming column 'name'
                dashboardController.setStudentEmail(rs.getString("email"));

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Student Dashboard");
                stage.setResizable(false);
                stage.show();


            } else {
                showErrorDialog("Login Failed", "Member not found. Please check credentials.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error", "Database connection failed.");
        }
    }

    @FXML
    public void onCloseTab(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/readify/launchScreen.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to show JFoenix error dialog
    private void showErrorDialog(String title, String message) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(new Text(title));
        dialogLayout.setBody(new Text(message));

        JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.CENTER);

        JFXButton closeButton = new JFXButton("OK");
        closeButton.setStyle("-fx-background-color: #2A4759; -fx-text-fill: white; -fx-font-weight: bold;");
        closeButton.setOnAction(e -> dialog.close());

        dialogLayout.setActions(closeButton);
        dialog.show();
    }
}
