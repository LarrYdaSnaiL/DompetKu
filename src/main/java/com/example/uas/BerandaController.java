package com.example.uas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class BerandaController {

    @FXML
    private ListView<String> listHistori;

    @FXML
    private PieChart pengeluaranPieChart;

    @FXML
    private BarChart<String, Number> pemasukanBarChart;

    @FXML
    public void initialize() {
        // ===== Data histori =====
        ObservableList<String> data = FXCollections.observableArrayList(
                "Belanja,-50000,12 Mei 2025",
                "Makan,-20000,12 Mei 2025",
                "Transport,-30000,12 Mei 2025"
        );
        listHistori.setItems(data);

        listHistori.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    String[] parts = item.split(",");
                    String judul = parts[0];
                    String jumlah = parts[1];
                    String tanggal = parts[2];

                    VBox detailBox = new VBox(2);
                    Label lblJudul = new Label(judul);
                    lblJudul.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
                    Label lblTanggal = new Label(tanggal);
                    lblTanggal.setStyle("-fx-font-size: 11px; -fx-text-fill: gray;");
                    detailBox.getChildren().addAll(lblJudul, lblTanggal);

                    Label lblJumlah = new Label(jumlah.replace("-", "-Rp ") + ".000");
                    lblJumlah.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

                    HBox container = new HBox(detailBox, lblJumlah);
                    container.setSpacing(20);
                    container.setPadding(new Insets(10));
                    container.setAlignment(Pos.CENTER_LEFT);
                    container.setStyle("-fx-background-color: #f2f2f2; -fx-background-radius: 10;");

                    HBox.setHgrow(detailBox, Priority.ALWAYS);
                    lblJumlah.setMaxWidth(Double.MAX_VALUE);
                    lblJumlah.setAlignment(Pos.CENTER_RIGHT);

                    setGraphic(container);
                }
            }
        });

        // ===== Pie Chart data =====
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Belanja", 50),
                new PieChart.Data("Makan", 20),
                new PieChart.Data("Transport", 30)
        );
        pengeluaranPieChart.setData(pieData);

        // ===== Bar Chart data =====
        XYChart.Series<String, Number> barData = new XYChart.Series<>();
        barData.getData().add(new XYChart.Data<>("Pemasukan", 1500000));
        barData.getData().add(new XYChart.Data<>("Pengeluaran", 1000000));

        pemasukanBarChart.getData().add(barData);
    }
}
