package com.example.readify;

import com.example.readify.Database.DBConnection;
import com.example.readify.Models.Book;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class StudentBooksController {

    // ================= UI =================
    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> colBookId;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, String> colCategory;
    @FXML private TableColumn<Book, Integer> colCopies;
    @FXML private TableColumn<Book, String> colStatus;
    @FXML private TableColumn<Book, String> colActions;
    @FXML private TextField searchField;

    // ================= DATA =================
    private final ObservableList<Book> masterList = FXCollections.observableArrayList();
    private FilteredList<Book> filteredList;

    private boolean initialized = false;

    // ================= INITIALIZE =================
    @FXML
    public void initialize() {
        if (initialized) return;
        initialized = true;

        setupColumns();
        setupSearch();   // search first
        loadBooks();     // then data
        addIssueButton();
    }

    // ================= TABLE SETUP =================
    private void setupColumns() {
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colCopies.setCellValueFactory(new PropertyValueFactory<>("totalCopies"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    // ================= SEARCH =================
    private void setupSearch() {
        filteredList = new FilteredList<>(masterList, b -> true);
        SortedList<Book> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(booksTable.comparatorProperty());
        booksTable.setItems(sortedList);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String text = newVal.toLowerCase().trim();
            filteredList.setPredicate(book -> {
                if (text.isEmpty()) return true;
                return book.getTitle().toLowerCase().contains(text)
                        || book.getAuthor().toLowerCase().contains(text)
                        || book.getCategory().toLowerCase().contains(text);
            });
        });
    }

    // ================= LOAD BOOKS =================
    private void loadBooks() {
        masterList.clear();
        String query = "SELECT * FROM books";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                masterList.add(new Book(
                        rs.getString("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("publisher"),
                        rs.getString("isbn"),
                        rs.getInt("total_copies"),
                        rs.getString("status"),
                        rs.getDate("date_added").toLocalDate()
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= ISSUE BUTTON =================
    private void addIssueButton() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final JFXButton issueBtn = new JFXButton("Issue");

            {
                issueBtn.setStyle("-fx-background-color: #2A4759; -fx-text-fill: white;");
                issueBtn.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handleIssueBook(book);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Book book = getTableView().getItems().get(getIndex());
                    if (book.getTotalCopies() <= 0) {
                        issueBtn.setDisable(true);
                        issueBtn.setText("Unavailable");
                    } else {
                        issueBtn.setDisable(false);
                        issueBtn.setText("Issue");
                    }
                    setGraphic(issueBtn);
                }
            }
        });
    }

    // ================= ISSUE LOGIC =================
    private void handleIssueBook(Book book) {
        int studentId = StudentSession.getInstance().getStudentId();

        if (studentId <= 0) {
            showAlert("Session Error", "Invalid student session. Please login again.");
            return;
        }

        if (book.getTotalCopies() <= 0) {
            showAlert("Unavailable", "No copies available!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            String insertSql =
                    "INSERT INTO issued_books (book_id, member_id, issued_date, due_date) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertSql);
            ps.setString(1, book.getBookId());
            ps.setInt(2, studentId);
            ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            ps.setDate(4, java.sql.Date.valueOf(LocalDate.now().plusDays(1)));
            ps.executeUpdate();

            String updateSql =
                    "UPDATE books SET total_copies = total_copies - 1 WHERE book_id = ?";
            PreparedStatement psUpdate = conn.prepareStatement(updateSql);
            psUpdate.setString(1, book.getBookId());
            psUpdate.executeUpdate();

            showAlert("Success", "Book issued successfully!");
            loadBooks();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to issue book!");
        }
    }

    // ================= ALERT =================
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ================= LOGOUT =================
    @FXML
    public void handleStudentLogout(MouseEvent event) {
        try {
            StudentSession.getInstance().clearSession();

            Parent root = FXMLLoader.load(getClass().getResource("/com/example/readify/launchScreen.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= CLOSE TAB =================
    @FXML
    public void onCloseTab(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/readify/StudentDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
