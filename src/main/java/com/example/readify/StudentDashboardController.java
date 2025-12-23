package com.example.readify;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class StudentDashboardController {
    @FXML
    private Text StudentName;

    public void setStudentName(String name) {
        StudentName.setText(name);
    }
}
