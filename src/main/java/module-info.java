module com.cdm.uas_pbo {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    opens com.cdm.uas_pbo.controller to javafx.fxml;

    opens com.cdm.uas_pbo to javafx.graphics;

    opens com.cdm.uas_pbo.model to javafx.base;
    opens com.cdm.uas_pbo.Service to javafx.fxml;
}