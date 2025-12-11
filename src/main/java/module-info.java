module com.example.readify {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.jfoenix;


    opens com.example.readify to javafx.fxml;
    exports com.example.readify;
    exports com.example.readify.Start;
    opens com.example.readify.Start to javafx.fxml;
}