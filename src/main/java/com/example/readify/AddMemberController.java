package com.example.readify;
import com.example.readify.Database.DBConnection;
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public void addMember(String name, String email, String phone, String address,
                          String membership, LocalDate joinDate) {
        String sql = "INSERT INTO members (name, email, phone, address, membership, join_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, phone);
            pst.setString(4, address);
            pst.setString(5, membership);
            pst.setDate(6, java.sql.Date.valueOf(joinDate));

            pst.executeUpdate();
            System.out.println("Member added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSaveMember() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String membership = membershipBox.getValue();
        LocalDate joinDate = joinDatePicker.getValue();

        // Validation
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()
                || address.isEmpty() || membership == null || joinDate == null) {
            System.out.println("Please fill all required fields!");
            return;
        }

        // Save to DB
        addMember(name, email, phone, address, membership, joinDate);
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
