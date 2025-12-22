package com.example.readify;

import com.example.readify.Database.DBConnection;
import com.example.readify.Models.Member;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Cursor;

import java.net.URL;
import java.time.LocalDate;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ViewMembersController implements Initializable {

    @FXML private TextField searchField;
    @FXML private JFXComboBox<String> categoryCombo;
    @FXML private JFXComboBox<String> sortCombo;
    @FXML private StackPane stackPane;

    private ObservableList<Member> membersList;

    @FXML private TableView<Member> membersTable;
    @FXML private TableColumn<Member, Integer> colId;
    @FXML private TableColumn<Member, String> colName;
    @FXML private TableColumn<Member, String> colEmail;
    @FXML private TableColumn<Member, String> colPhone;
    @FXML private TableColumn<Member, String> colAddress;
    @FXML private TableColumn<Member, String> colMembership;
    @FXML private TableColumn<Member, LocalDate> colJoinDate;
    @FXML private TableColumn<Member, Void> colActions;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Bind table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colMembership.setCellValueFactory(new PropertyValueFactory<>("membership"));
        colJoinDate.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
        membersTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        loadMembersFromDatabase();
        categoryCombo.setItems(FXCollections.observableArrayList("Student", "Teacher"));
        sortCombo.setItems(FXCollections.observableArrayList("A-Z", "Z-A"));

        implementSearchAndFilter();
        addDeleteActionColumn();
    }

    private void loadMembersFromDatabase() {
        membersList = FXCollections.observableArrayList();

        try {
            var conn = DBConnection.getConnection();
            String query = "SELECT * FROM members ORDER BY join_date DESC;";
            var pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Member member = new Member(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("membership"),
                        rs.getDate("join_date") != null ? rs.getDate("join_date").toLocalDate() : null,
                        "" // password not stored/displayed
                );
                membersList.add(member);
            }

            membersTable.setItems(membersList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void implementSearchAndFilter() {
        FilteredList<Member> filteredData = new FilteredList<>(membersList, m -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> updateFilters(filteredData));
        categoryCombo.valueProperty().addListener((obs, oldVal, newVal) -> updateFilters(filteredData));
        sortCombo.valueProperty().addListener((obs, oldVal, newVal) -> updateFilters(filteredData));

        SortedList<Member> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(membersTable.comparatorProperty());
        membersTable.setItems(sortedData);
    }

    private void updateFilters(FilteredList<Member> filteredData) {
        String searchText = searchField.getText() != null ? searchField.getText().toLowerCase() : "";
        String membership = categoryCombo.getValue();
        String sortOrder = sortCombo.getValue();

        filteredData.setPredicate(member -> {
            boolean matchesSearch = member.getName().toLowerCase().contains(searchText) ||
                    (member.getEmail() != null && member.getEmail().toLowerCase().contains(searchText)) ||
                    (member.getPhone() != null && member.getPhone().toLowerCase().contains(searchText));

            boolean matchesMembership = (membership == null || membership.isEmpty()) ||
                    membership.equals(member.getMembership());

            return matchesSearch && matchesMembership;
        });

        // Apply sort
        if (sortOrder != null) {
            if (sortOrder.equals("A-Z")) {
                membersTable.getSortOrder().clear();
                colName.setSortType(TableColumn.SortType.ASCENDING);
                membersTable.getSortOrder().add(colName);
            } else if (sortOrder.equals("Z-A")) {
                membersTable.getSortOrder().clear();
                colName.setSortType(TableColumn.SortType.DESCENDING);
                membersTable.getSortOrder().add(colName);
            }
        }
    }

    private void addDeleteActionColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Label deleteLabel = new Label("Delete");

            {
                deleteLabel.setStyle("-fx-text-fill: blue; -fx-underline: true;");
                deleteLabel.setCursor(Cursor.HAND);

                deleteLabel.setOnMouseClicked(event -> {
                    Member member = getTableView().getItems().get(getIndex());
                    showDeleteConfirmation(member);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteLabel);
                }
            }
        });
    }

    private void showDeleteConfirmation(Member member) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label("Confirm Delete"));
        content.setBody(new Label("Are you sure you want to delete this member?"));

        JFXButton okButton = new JFXButton("OK");
        JFXButton cancelButton = new JFXButton("Cancel");

        okButton.setStyle("-fx-background-color: #D9534F; -fx-text-fill: white;");
        cancelButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");

        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);

        okButton.setOnAction(e -> {
            deleteMember(member);
            dialog.close();
        });

        cancelButton.setOnAction(e -> dialog.close());
        content.setActions(cancelButton, okButton);
        dialog.show();
    }

    private void deleteMember(Member member) {
        try {
            var conn = DBConnection.getConnection();
            String query = "DELETE FROM members WHERE id = ?";
            var pst = conn.prepareStatement(query);
            pst.setInt(1, member.getId());

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                membersList.remove(member);
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    @FXML
    public void onCloseTab(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/readify/AdminDashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
