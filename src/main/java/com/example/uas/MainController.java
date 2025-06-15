package com.example.uas;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;

public class MainController {

    @FXML
    private Button btnMasuk;

    @FXML
    private Button btnDaftar;

    @FXML
    private void handleMasuk() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/uas/masuk.fxml"));
            Stage stage = (Stage) btnMasuk.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal membuka halaman masuk.fxml");
        }
    }

    @FXML
    private void handleDaftar() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/uas/daftar.fxml"));
            Stage stage = (Stage) btnDaftar.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal membuka halaman daftar.fxml");
        }
    }
}
