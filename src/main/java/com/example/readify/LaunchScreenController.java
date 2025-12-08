package com.example.readify;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LaunchScreenController {

    @FXML
    private Button LaunchAdmin;

    @FXML
    private Button LaunchStudent;

    @FXML
    public void initialize() {

    }

    public void handleAdminLaunch() {
        System.out.println("Admin button clicked!");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/readify/AdminLogin.fxml"));
            Parent adminLoginRoot = loader.load();

            Stage stage = (Stage) LaunchAdmin.getScene().getWindow();

            stage.setScene(new Scene(adminLoginRoot, 800, 600));
            stage.setTitle("Admin Login");
            stage.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleStudentLaunch() {
        System.out.println("Student button clicked!");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/readify/StudentLogin.fxml"));
            Parent studentLoginRoot = loader.load();

            Stage stage = (Stage) LaunchStudent.getScene().getWindow();

            stage.setScene(new Scene(studentLoginRoot, 800, 600));
            stage.setTitle("Student Login");
            stage.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onHover(javafx.scene.input.MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #D1E2EB; " +
                        "-fx-text-fill: #2A4759; " +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    public void onExit(javafx.scene.input.MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FFFFFF; " +
                        "-fx-text-fill: #2A4759; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-background-radius: 10px;"
        );
    }
}
