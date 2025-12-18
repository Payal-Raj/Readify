package com.example.readify;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddMemberController implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressField;
    @FXML private ComboBox<String> membershipBox;
    @FXML private DatePicker joinDatePicker;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        membershipBox.getItems().addAll(
                "Student",
                "Teacher",
                "General"
        );
        joinDatePicker.setValue(LocalDate.now());
    }

    @FXML
    private void handleSaveMember() {
        System.out.println("Member Saved");
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
}
