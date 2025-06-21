package com.cdm.uas_pbo;

import com.cdm.uas_pbo.Service.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 350);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png")));
        stage.getIcons().add(icon);
        stage.setTitle("DompetKu.");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        Database.closeConnection();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}