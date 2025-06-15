package com.example.uas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/uas/splash.fxml"));
        Scene scene = new Scene(loader.load(), 400, 600);
        stage.setScene(scene);
        stage.setTitle("DompetKU");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
