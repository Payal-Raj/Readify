package com.example.readify;

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

public class AdminLoginController {

    @FXML
    private TextField AdminUserName;

    @FXML
    private PasswordField AdminPassword;

    @FXML
    private StackPane rootPane; // Required for JFoenix dialogs

    private final String ADMIN_USERNAME = "admin123";
    private final String ADMIN_PASSWORD = "admin@123";

    @FXML
    public void handleAdminLogin(ActionEvent event) {

        String username = AdminUserName.getText();
        String password = AdminPassword.getText();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/readify/AdminDashboard.fxml"));
                Parent adminDashboardRoot = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(adminDashboardRoot, 800, 600));
                stage.setTitle("Admin Dashboard");
                stage.setResizable(false);

// Show the stage
                stage.show();


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            showErrorDialog("Login Failed", "Username or Password is incorrect!");
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
