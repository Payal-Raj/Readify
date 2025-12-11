package com.example.readify;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

public class AdminLoginController {

    @FXML
    private TextField AdminUserName;

    @FXML
    private PasswordField AdminPassword;

    private final String ADMIN_USERNAME = "admin123";
    private final String ADMIN_PASSWORD = "admin@123";

    @FXML
    public void handleAdminLogin(ActionEvent event) {

        String username = AdminUserName.getText();
        String password = AdminPassword.getText();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/example/readify/AdminDashboard.fxml"));

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Invalid Credentials");
            alert.setContentText("Username or Password is incorrect!");
            alert.show();
        }
    }

    @FXML
    public void onCloseTab(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/readify/launchScreen.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
