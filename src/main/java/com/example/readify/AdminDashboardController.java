package com.example.readify;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;


public class AdminDashboardController {

    @FXML
    private VBox sideMenu;

    @FXML
    private VBox mainContent;

    @FXML
    private Label ViewIssuedBooksLabel;

    @FXML
    private Label viewMembersLabel;

    @FXML
    private Label viewBooksLabel;

    @FXML
    private Label addLabel;

    @FXML
    private VBox card1;

    @FXML
    private VBox card2;

    private boolean isExpanded = false;

    private void setCardMargins(double left, double right) {
        Insets newInsets = new Insets(0, right, 0, left);

        HBox.setMargin(card1, newInsets);
        HBox.setMargin(card2, newInsets);
    }

    @FXML
    private void expandMenu() {
        if (!isExpanded) {
            isExpanded = true;
            sideMenu.setPrefWidth(150.0);
            mainContent.setLayoutX(150.0);
            viewMembersLabel.setVisible(true);
            ViewIssuedBooksLabel.setVisible(true);
            viewBooksLabel.setVisible(true);
            addLabel.setVisible(true);

            setCardMargins(10, 10);
        }
    }

    @FXML
    private void shrinkMenu() {
        if (isExpanded) {
            isExpanded = false;
            sideMenu.setPrefWidth(46.0);
            mainContent.setLayoutX(46.0);
            viewMembersLabel.setVisible(false);
            ViewIssuedBooksLabel.setVisible(false);
            viewBooksLabel.setVisible(false);
            addLabel.setVisible(false);

            setCardMargins(60, 20);
        }
    }

    @FXML
    public void handleAdminLogOut(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/readify/launchScreen.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUI(String fxml) {
        try {
            mainContent.getChildren().clear();
            Parent ui = FXMLLoader.load(getClass().getResource(fxml));
            mainContent.getChildren().add(ui);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void AddMember() {
        mainContent.getChildren().clear();
        loadUI("/com/example/readify/AddMember.fxml");
    }

    @FXML
    private void AddBook() {
        mainContent.getChildren().clear();
        loadUI("/com/example/readify/AddBook.fxml");
    }

    @FXML
    public void ViewIssuedBooks(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/readify/ViewIssuedBooks.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void viewBook(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/readify/ViewBooks.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void viewMember(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/readify/ViewMembers.FXML"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
