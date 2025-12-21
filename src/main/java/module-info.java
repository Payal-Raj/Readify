module com.example.readify {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.jfoenix;
    requires java.sql;

    // Open packages containing FXML controllers
    opens com.example.readify to javafx.fxml;
    opens com.example.readify.Models to javafx.base;
    opens com.example.readify.Start to javafx.fxml;  // for StartApp launcher if it uses FXML

    // Export packages for general access
    exports com.example.readify.Start;    // main launcher
    exports com.example.readify.Database; // DBConnection
}
