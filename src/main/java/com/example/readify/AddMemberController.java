package com.example.readify;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddMemberController implements Initializable {

    @FXML private JFXTextField nameField;
    @FXML private JFXTextField emailField;
    @FXML private JFXTextField phoneField;
    @FXML private JFXTextArea addressField;
    @FXML private JFXComboBox<String> membershipBox;
    @FXML private DatePicker joinDatePicker;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        membershipBox.getItems().addAll(
//                "Student",
//                "Teacher",
//                "General"
//        );
//        joinDatePicker.setValue(LocalDate.now());
    }

//    @FXML
//    private void handleSaveMember() {
//        System.out.println("Save Member clicked");
//    }

}
