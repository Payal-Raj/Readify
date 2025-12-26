package com.example.readify;

import com.example.readify.Database.DBConnection;
import com.example.readify.Models.IssuedBooks;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StudentIssuedBooksController implements Initializable {

    @FXML private TextField searchField;
    @FXML private StackPane stackPane;
    @FXML private TableView<IssuedBooks> issuedBooksTable;

    @FXML private TableColumn<IssuedBooks, String> colBookId;
    @FXML private TableColumn<IssuedBooks, String> colBookName;
    @FXML private TableColumn<IssuedBooks, LocalDate> colIssuedDate;
    @FXML private TableColumn<IssuedBooks, LocalDate> colDueDate;
    @FXML private TableColumn<IssuedBooks, Void> colActions;

    private ObservableList<IssuedBooks> issuedBooksList;

    private int memberId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colBookId.setCellValueFactory(data -> data.getValue().bookIdProperty());
        colBookName.setCellValueFactory(data -> data.getValue().bookNameProperty());
        colIssuedDate.setCellValueFactory(data -> data.getValue().issuedDateProperty());
        colDueDate.setCellValueFactory(data -> data.getValue().dueDateProperty());

        addActionColumn();
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
        loadIssuedBooks();
        implementSearch();
    }

    private void loadIssuedBooks() {
        issuedBooksList = FXCollections.observableArrayList();

        try {
            var conn = DBConnection.getConnection();
            String query = """
            SELECT ib.book_id, b.title AS book_name, ib.issued_date, ib.due_date
            FROM issued_books ib
            JOIN books b ON ib.book_id = b.book_id
            WHERE ib.member_id = ?
        """;
            var pst = conn.prepareStatement(query);
            pst.setInt(1, memberId);
            var rs = pst.executeQuery();

            while (rs.next()) {
                IssuedBooks book = new IssuedBooks(
                        rs.getString("book_id"),
                        rs.getString("book_name"),
                        rs.getDate("issued_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate()
                );
                issuedBooksList.add(book);
            }

            issuedBooksTable.setItems(issuedBooksList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void implementSearch() {
        FilteredList<IssuedBooks> filteredData = new FilteredList<>(issuedBooksList, b -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String searchText = newVal.toLowerCase();
            filteredData.setPredicate(book ->
                    book.getBookName().toLowerCase().contains(searchText)
            );

        });

        SortedList<IssuedBooks> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(issuedBooksTable.comparatorProperty());
        issuedBooksTable.setItems(sortedData);
    }

    private void addActionColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Label returnLabel = new Label("Return");
            private final Label renewLabel = new Label("Renew");

            {
                returnLabel.setStyle("-fx-text-fill: blue; -fx-underline: true;");
                renewLabel.setStyle("-fx-text-fill: blue; -fx-underline: true;");
                returnLabel.setCursor(Cursor.HAND);
                renewLabel.setCursor(Cursor.HAND);

                returnLabel.setOnMouseClicked(event -> {
                    IssuedBooks book = getTableView().getItems().get(getIndex());
                    handleReturn(book);
                });

                renewLabel.setOnMouseClicked(event -> {
                    IssuedBooks book = getTableView().getItems().get(getIndex());
                    handleRenew(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10, returnLabel, renewLabel);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void handleReturn(IssuedBooks book) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Return Book");
        alert.setHeaderText("Are you sure you want to return this book?");
        alert.setContentText(book.getBookName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                var conn = DBConnection.getConnection();
                var pst = conn.prepareStatement("DELETE FROM issued_books WHERE book_id = ? AND member_id = ?");
                pst.setString(1, book.getBookId());
                pst.setInt(2, memberId);
                int rows = pst.executeUpdate();
                if (rows > 0) issuedBooksList.remove(book);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    private void handleReturn(IssuedBooks book) {
//        try {
//            var conn = DBConnection.getConnection();
//            var pst = conn.prepareStatement("DELETE FROM issued_books WHERE book_id = ? AND member_id = ?");
//            pst.setString(1, book.bookIdProperty().get());
//            pst.setInt(2, memberId);
//            int rows = pst.executeUpdate();
//            if (rows > 0) issuedBooksList.remove(book);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void handleRenew(IssuedBooks book) {
//        try {
//            var conn = DBConnection.getConnection();
//            LocalDate newDue = book.dueDateProperty().get().plusWeeks(2);
//            var pst = conn.prepareStatement("UPDATE issued_books SET due_date = ? WHERE book_id = ? AND member_id = ?");
//            pst.setDate(1, Date.valueOf(newDue));
//            pst.setString(2, book.bookIdProperty().get());
//            pst.setInt(3, memberId);
//            pst.executeUpdate();
//            book.dueDateProperty().set(newDue);
//            issuedBooksTable.refresh();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void handleRenew(IssuedBooks book) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Renew Book");
        alert.setHeaderText("Do you want to renew this book for 1 day more?");
        alert.setContentText(book.getBookName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                var conn = DBConnection.getConnection();
                LocalDate newDue = book.getDueDate().plusDays(1);
                var pst = conn.prepareStatement("UPDATE issued_books SET due_date = ? WHERE book_id = ? AND member_id = ?");
                pst.setDate(1, Date.valueOf(newDue));
                pst.setString(2, book.getBookId());
                pst.setInt(3, memberId);
                pst.executeUpdate();
                book.setDueDate(newDue);
                issuedBooksTable.refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    public void handleLogOut(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/readify/launchScreen.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void onCloseTab(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/readify/StudentDashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
