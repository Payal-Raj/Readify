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
    private int studentId;
    @FXML
    private Text StudentName;
    private String studentEmail;

    public void setStudentId(int id) {
        this.studentId = id;
    }
    public void setStudentName(String name) {
        StudentName.setText(name);
    }
    public void setStudentEmail(String email) {
        this.studentEmail = email;
    }
    @FXML
    private void AvailableBooks(MouseEvent event) {
        System.out.println("Available Books clicked");
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/readify/StudentBooks.fxml")
            );
            Parent root = loader.load();
            StudentBooksController controller = loader.getController();

            // Pass the student info
            controller.setStudentId(this.studentId);
            controller.setStudentName(this.StudentName.getText());
            controller.setStudentEmail(this.studentEmail);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void IssuedBooks(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/readify/StudentIssuedBooks.fxml"));
            Parent root = loader.load();
            StudentIssuedBooksController controller = loader.getController();
            controller.setMemberId(this.studentId);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



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
