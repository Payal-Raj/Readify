module com.example.readify {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.jfoenix;
    requires java.sql;
    requires javafx.base;

    // Open packages for reflection (FXML and PropertyValueFactory)
    opens com.example.readify to javafx.fxml;
    opens com.example.readify.Models to javafx.base;
    opens com.example.readify.Start to javafx.fxml;

    // Export packages if needed by other modules
    exports com.example.readify.Start;    // main launcher
    exports com.example.readify.Database; // DBConnection
}
