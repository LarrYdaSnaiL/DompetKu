package com.example.uas;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;

public class PengaturanController {

    @FXML
    private TextField txtNama;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnSimpan;

    @FXML
    private Button btnUploadFoto;

    @FXML
    private ImageView profileImageView;

    @FXML
    public void initialize() {
        btnSimpan.setOnAction(event -> {
            String nama = txtNama.getText();
            String email = txtEmail.getText();
            String password = txtPassword.getText();
            System.out.println("Data disimpan:");
            System.out.println("Nama: " + nama);
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);
        });

        btnUploadFoto.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Pilih Foto");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                Image image = new Image(file.toURI().toString());
                profileImageView.setImage(image);
            }
        });
    }
}
