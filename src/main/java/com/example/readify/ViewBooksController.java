package com.example.readify;

import com.example.readify.Models.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ViewBooksController implements Initializable {

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
        loadDummyData();
    }
    private void loadDummyData() {
        ObservableList<Book> bookList = FXCollections.observableArrayList();

        bookList.add(new Book(
                "B001",
                "Java Programming",
                "Herbert Schildt",
                "Programming",
                "McGraw Hill",
                "978-0071808552",
                5,
                "Available",
                LocalDate.now()
        ));

        bookList.add(new Book(
                "B002",
                "Data Structures",
                "Mark Allen Weiss",
                "CS",
                "Pearson",
                "978-0133008429",
                3,
                "Issued",
                LocalDate.now()
        ));

        booksTable.setItems(bookList);
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
