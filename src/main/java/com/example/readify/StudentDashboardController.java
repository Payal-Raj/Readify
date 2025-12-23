package com.example.readify;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StudentDashboardController {
    @FXML
    private Text StudentName;

    private String studentEmail;

    public void setStudentName(String name) {
        StudentName.setText(name);
    }
    public void setStudentEmail(String email) {
        this.studentEmail = email;
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

//    @FXML
//    private void handleMyProfile(MouseEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/readify/StudentProfile.fxml"));
//            Parent root = loader.load();
//
//            StudentProfileController profileController = loader.getController();
//            profileController.setStudentEmail(this.studentEmail); // Pass the email of logged-in student
//
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root));
//            stage.setTitle("My Profile");
//            stage.setResizable(false);
//            stage.show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @FXML
    public void handleMyProfile(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/readify/StudentProfile.fxml")
            );
            Parent root = loader.load();
            StudentProfileController controller = loader.getController();
            controller.setStudentEmail(this.studentEmail);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
