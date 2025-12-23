package com.example.readify;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class StudentDashboardController {
    @FXML
    private Text StudentName;

    public void setStudentName(String name) {
        StudentName.setText(name);
    }

    @FXML
    private void handleMyProfile(MouseEvent event) {
        System.out.println("My Profile clicked");
        // TODO: open profile page
    }

    @FXML
    private void AvailableBooks(MouseEvent event) {
        System.out.println("Available Books clicked");
        // TODO: open available books page
    }

    @FXML
    private void IssuedBooks(MouseEvent event) {
        System.out.println("Issued Books clicked");
        // TODO: open issued books page
    }
}
