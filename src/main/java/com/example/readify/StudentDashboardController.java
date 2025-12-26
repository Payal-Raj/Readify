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

    @FXML
    public void initialize() {
        // Set student name from session
        StudentName.setText(StudentSession.getInstance().getStudentName());
    }

    @FXML
    private void AvailableBooks(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/readify/StudentBooks.fxml"));
            Parent root = loader.load();
            // Controller will fetch session automatically
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
            controller.setMemberId(StudentSession.getInstance().getStudentId());

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/readify/StudentProfile.fxml"));
            Parent root = loader.load();
            StudentProfileController controller = loader.getController();
            controller.setStudentEmail(StudentSession.getInstance().getStudentEmail());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
