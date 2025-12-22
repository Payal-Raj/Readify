package com.example.readify;

import com.example.readify.Database.DBConnection;
import com.example.readify.Models.Book;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Cursor;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ViewBooksController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private JFXComboBox<String> categoryCombo;
    @FXML
    private JFXComboBox<String> statusCombo;
    @FXML private JFXComboBox<String> sortCombo;
    @FXML
    private StackPane stackPane;

    private ObservableList<Book> booksList;

    @FXML
    private TableView<Book> booksTable;

    @FXML private TableColumn<Book, String> colBookId;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, String> colCategory;
    @FXML private TableColumn<Book, String> colPublisher;
    @FXML private TableColumn<Book, String> colISBN;
    @FXML private TableColumn<Book, Integer> colCopies;
    @FXML private TableColumn<Book, String> colStatus;
    @FXML private TableColumn<Book, LocalDate> colDateAdded;
    @FXML
    private TableColumn<Book, Void> colActions;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Binding
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colCopies.setCellValueFactory(
                new PropertyValueFactory<>("totalCopies")
        );
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDateAdded.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        booksTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        loadBooksFromDatabase();

        categoryCombo.setItems(FXCollections.observableArrayList(
                "Programming", "Science", "Literature", "History", "Islamic Studies"
        ));
        statusCombo.setItems(FXCollections.observableArrayList(
                "Available", "Issued"
        ));
        sortCombo.setItems(FXCollections.observableArrayList(
                "A-Z", "Z-A"
        ));

        implementSearchAndFilter();
        addDeleteActionColumn();

    }

    private void loadBooksFromDatabase() {
        booksList = FXCollections.observableArrayList();

        try {
            var conn = DBConnection.getConnection();
            String query = "SELECT * FROM books";
            var pst = conn.prepareStatement(query);
            var rs = pst.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("publisher"),
                        rs.getString("isbn"),
                        rs.getInt("total_copies"),
                        rs.getString("status"),
                        rs.getDate("date_added").toLocalDate()
                );
                booksList.add(book);
            }

            booksTable.setItems(booksList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void implementSearchAndFilter() {
        FilteredList<Book> filteredData = new FilteredList<>(booksList, b -> true);

        // Add listeners
        searchField.textProperty().addListener((obs, oldVal, newVal) -> updateFilters(filteredData));
        categoryCombo.valueProperty().addListener((obs, oldVal, newVal) -> updateFilters(filteredData));
        statusCombo.valueProperty().addListener((obs, oldVal, newVal) -> updateFilters(filteredData));
        sortCombo.valueProperty().addListener((obs, oldVal, newVal) -> updateFilters(filteredData));

        SortedList<Book> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(booksTable.comparatorProperty());
        booksTable.setItems(sortedData);
    }

    private void updateFilters(FilteredList<Book> filteredData) {
        String searchText = searchField.getText() != null ? searchField.getText().toLowerCase() : "";
        String category = categoryCombo.getValue();
        String status = statusCombo.getValue();
        String sortOrder = sortCombo.getValue();

        filteredData.setPredicate(book -> {
            boolean matchesSearch = book.getTitle().toLowerCase().contains(searchText);
            boolean matchesCategory = (category == null || category.isEmpty()) || book.getCategory().equals(category);
            boolean matchesStatus = (status == null || status.isEmpty()) || book.getStatus().equals(status);
            return matchesSearch && matchesCategory && matchesStatus;
        });

        // Apply sort from Sort ComboBox
        if (sortOrder != null) {
            if (sortOrder.equals("A-Z")) {
                booksTable.getSortOrder().clear();
                colTitle.setSortType(TableColumn.SortType.ASCENDING);
                booksTable.getSortOrder().add(colTitle);
            } else if (sortOrder.equals("Z-A")) {
                booksTable.getSortOrder().clear();
                colTitle.setSortType(TableColumn.SortType.DESCENDING);
                booksTable.getSortOrder().add(colTitle);
            }
        }
    }

    private void addDeleteActionColumn() {

        colActions.setCellFactory(param -> new TableCell<>() {

            private final Label deleteLabel = new Label("Delete");

            {
                deleteLabel.setStyle(
                        "-fx-text-fill: blue;" +
                                "-fx-underline: true;"
                );
                deleteLabel.setCursor(Cursor.HAND);

                // Click event
                deleteLabel.setOnMouseClicked(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    showDeleteConfirmation(book);
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

    private void showDeleteConfirmation(Book book) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label("Confirm Delete"));
        content.setBody(new Label("Are you sure you want to delete this data?"));

        JFXButton okButton = new JFXButton("OK");
        JFXButton cancelButton = new JFXButton("Cancel");

        okButton.setStyle("-fx-background-color: #D9534F; -fx-text-fill: white;");
        cancelButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");

        JFXDialog dialog = new JFXDialog(
                stackPane,
                content,
                JFXDialog.DialogTransition.CENTER
        );

        okButton.setOnAction(e -> {
            deleteBook(book);
            dialog.close();
        });

        cancelButton.setOnAction(e -> dialog.close());

        content.setActions(cancelButton, okButton);
        dialog.show();
    }

    private void deleteBook(Book book) {

        try {
            var conn = DBConnection.getConnection();
            String query = "DELETE FROM books WHERE book_id = ?";
            var pst = conn.prepareStatement(query);
            pst.setString(1, book.getBookId());

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                booksList.remove(book); // removes from table
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
