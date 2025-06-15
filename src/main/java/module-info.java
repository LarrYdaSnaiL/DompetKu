module com.example.uas {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.uas to javafx.fxml;
    exports com.example.uas;
}