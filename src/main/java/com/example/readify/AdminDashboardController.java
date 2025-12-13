package com.example.readify;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AdminDashboardController {

    @FXML
    private VBox sideMenu;

    @FXML
    private VBox mainContent;

    @FXML
    private Label viewMembersLabel;

    @FXML
    private Label viewBooksLabel;

    @FXML
    private Label addLabel;

    private boolean isExpanded = false;

    @FXML
    private void expandMenu() {
        if (!isExpanded) {
            isExpanded = true;
            sideMenu.setPrefWidth(150.0);
            mainContent.setLayoutX(150.0);
            viewMembersLabel.setVisible(true);
            viewBooksLabel.setVisible(true);
            addLabel.setVisible(true);
        }
    }

    @FXML
    private void shrinkMenu() {
        if (isExpanded) {
            isExpanded = false;
            sideMenu.setPrefWidth(46.0);
            mainContent.setLayoutX(46.0);
            viewMembersLabel.setVisible(false);
            viewBooksLabel.setVisible(false);
            addLabel.setVisible(false);
        }
    }


    @FXML
    private void AddMember() {
        mainContent.getChildren().clear();
    }

    @FXML
    private void AddBook() {
        mainContent.getChildren().clear();
    }
}
