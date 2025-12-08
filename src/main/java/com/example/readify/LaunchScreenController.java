package com.example.readify;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LaunchScreenController {

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
