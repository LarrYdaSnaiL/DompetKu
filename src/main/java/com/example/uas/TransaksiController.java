package com.example.uas;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class TransaksiController {

    @FXML
    private TextField searchField;

    @FXML
    private VBox transaksiList;

    @FXML
    private Button btnTambahPemasukan, btnTambahPengeluaran;

    @FXML
    public void initialize() {
        // Contoh transaksi awal
        tambahTransaksi("Belanja", "12 Mei 2025", "-Rp 50.000");
        tambahTransaksi("Transportasi", "16 Mei 2025", "-Rp 20.000");
        tambahTransaksi("Pemasukan", "17 Mei 2025", "+Rp 100.000");

        btnTambahPemasukan.setOnAction(e -> tambahTransaksi("Pemasukan Baru", "20 Mei 2025", "+Rp 75.000"));
        btnTambahPengeluaran.setOnAction(e -> tambahTransaksi("Pengeluaran Baru", "20 Mei 2025", "-Rp 40.000"));

        searchField.textProperty().addListener((obs, oldText, newText) -> filterTransaksi(newText));
    }

    private void tambahTransaksi(String kategori, String tanggal, String jumlah) {
        HBox card = new HBox(10);
        card.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-background-radius: 10;");
        card.setPrefWidth(600);

        VBox info = new VBox(5);
        Label labelKategori = new Label(kategori);
        labelKategori.setStyle("-fx-font-weight: bold;");
        Label labelTanggal = new Label(tanggal);
        labelTanggal.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");
        info.getChildren().addAll(labelKategori, labelTanggal);

        Label labelJumlah = new Label(jumlah);
        labelJumlah.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnEdit = new Button("Edit");
        btnEdit.setStyle("-fx-background-color: #ffcc00;");
        Button btnHapus = new Button("Hapus");
        btnHapus.setStyle("-fx-background-color: #cc6600; -fx-text-fill: white;");
        btnHapus.setOnAction(e -> transaksiList.getChildren().remove(card));

        card.getChildren().addAll(info, spacer, labelJumlah, btnEdit, btnHapus);
        transaksiList.getChildren().add(card);
    }

    private void filterTransaksi(String keyword) {
        for (javafx.scene.Node node : transaksiList.getChildren()) {
            if (node instanceof HBox hbox) {
                VBox info = (VBox) hbox.getChildren().get(0);
                Label labelKategori = (Label) info.getChildren().get(0);
                boolean visible = labelKategori.getText().toLowerCase().contains(keyword.toLowerCase());
                hbox.setVisible(visible);
                hbox.setManaged(visible);
            }
        }
    }
}
