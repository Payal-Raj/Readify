package com.example.readify;

import com.example.readify.Database.DBConnection;
import com.example.readify.Models.IssuedBooks;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class ViewIssuedBooksController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private JFXComboBox<String> sortCombo;

    @FXML
    private TableView<IssuedBooks> issuedBooksTable;

    @FXML
    private TableColumn<IssuedBooks, String> colBookId;
    @FXML
    private TableColumn<IssuedBooks, Integer> colMemberId;
    @FXML
    private TableColumn<IssuedBooks, String> colBookName;
    @FXML
    private TableColumn<IssuedBooks, String> colMemberName;
    @FXML
    private TableColumn<IssuedBooks, LocalDate> colIssuedDate;
    @FXML
    private TableColumn<IssuedBooks, LocalDate> colDueDate;
    @FXML
    private TableColumn<IssuedBooks, Integer> colPenalty;

    private ObservableList<IssuedBooks> issuedBooksList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Bind columns
        colBookId.setCellValueFactory(cellData -> cellData.getValue().bookIdProperty());
        colMemberId.setCellValueFactory(cellData -> cellData.getValue().memberIdProperty().asObject());
        colBookName.setCellValueFactory(cellData -> cellData.getValue().bookNameProperty());
        colMemberName.setCellValueFactory(cellData -> cellData.getValue().memberNameProperty());
        colIssuedDate.setCellValueFactory(cellData -> cellData.getValue().issuedDateProperty());
        colDueDate.setCellValueFactory(cellData -> cellData.getValue().dueDateProperty());
        colPenalty.setCellValueFactory(cellData -> cellData.getValue().penaltyProperty().asObject());

        issuedBooksTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        loadIssuedBooksFromDatabase();

        // Initialize sort combo
        sortCombo.setItems(FXCollections.observableArrayList(
                "Oldest First", "Newest First"
        ));

        implementSearchAndSort();
    }

    private void loadIssuedBooksFromDatabase() {
        issuedBooksList = FXCollections.observableArrayList();

        try {
            var conn = DBConnection.getConnection();
            String query = "SELECT ib.book_id, ib.member_id, b.title AS book_name, m.name AS member_name, ib.issued_date, ib.due_date " +
                    "FROM issued_books ib " +
                    "JOIN books b ON ib.book_id = b.book_id " +
                    "JOIN members m ON ib.member_id = m.id " +
                    "ORDER BY ib.issued_date DESC";

            var pst = conn.prepareStatement(query);
            var rs = pst.executeQuery();

            while (rs.next()) {
                LocalDate issuedDate = rs.getDate("issued_date").toLocalDate();
                LocalDate dueDate = rs.getDate("due_date").toLocalDate();

                // Calculate penalty
                long daysOverdue = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
                int penalty = daysOverdue > 0 ? (int) (daysOverdue * 200) : 0;

                IssuedBooks issuedBook = new IssuedBooks(
                        rs.getString("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("book_name"),
                        rs.getString("member_name"),
                        issuedDate,
                        dueDate,
                        penalty
                );
                issuedBooksList.add(issuedBook);
            }

            issuedBooksTable.setItems(issuedBooksList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void implementSearchAndSort() {
        FilteredList<IssuedBooks> filteredData = new FilteredList<>(issuedBooksList, b -> true);

        // Search listener (search on book name or member name)
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(issuedBook -> {
                String searchText = newVal.toLowerCase();
                return issuedBook.getBookName().toLowerCase().contains(searchText)
                        || issuedBook.getMemberName().toLowerCase().contains(searchText);
            });
        });
        sortCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            SortedList<IssuedBooks> sortedData = new SortedList<>(filteredData);
            if (newVal != null) {
                if (newVal.equals("Oldest First")) {
                    sortedData.setComparator((b1, b2) -> b1.getIssuedDate().compareTo(b2.getIssuedDate()));
                } else if (newVal.equals("Newest First")) {
                    sortedData.setComparator((b1, b2) -> b2.getIssuedDate().compareTo(b1.getIssuedDate()));
                }
            }
            issuedBooksTable.setItems(sortedData);
        });
        SortedList<IssuedBooks> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(issuedBooksTable.comparatorProperty());
        issuedBooksTable.setItems(sortedData);
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
