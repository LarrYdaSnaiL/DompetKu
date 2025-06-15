package com.example.uas;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class SplashController {

    @FXML
    private ImageView logoImage;

    @FXML
    public void initialize() {
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            try {
                URL fxml = getClass().getResource("/com/example/uas/main.fxml");
                if (fxml == null) throw new IOException("main.fxml not found");

                Parent mainRoot = FXMLLoader.load(fxml);
                Stage stage = (Stage) logoImage.getScene().getWindow();
                stage.setScene(new Scene(mainRoot, 1024, 640));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        delay.play();
    }
}
