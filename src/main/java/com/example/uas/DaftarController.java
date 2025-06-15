package com.example.uas;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class DaftarController {

    @FXML
    private Button btnDaftar;

    @FXML
    private void handleDaftar() {
        System.out.println("Pendaftaran berhasil (simulasi)");

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/uas/fxml/main.fxml"));
            Stage stage = (Stage) btnDaftar.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal kembali ke halaman main.fxml");
        }
    }
}
