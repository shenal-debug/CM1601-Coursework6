package lk.cm1601.malabe_spare_system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.cm1601.malabe_spare_system.filehandler.InventoryFileHandler;
import lk.cm1601.malabe_spare_system.model.Part;

import java.util.List;

public class DashboardController {

    @FXML
    private TextField txtPartCode;

    @FXML
    private TextField txtPartName;

    @FXML
    private TextField txtBrand;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtSearch;

    @FXML
    private ComboBox<String> cmbCategory;

    @FXML
    private TableView<Part> tableParts;

    @FXML
    private TableColumn<Part, String> colCode;

    @FXML
    private TableColumn<Part, String> colName;

    @FXML
    private TableColumn<Part, String> colBrand;

    @FXML
    private TableColumn<Part, Double> colPrice;

    @FXML
    private TableColumn<Part, Integer> colQuantity;

    @FXML
    private TableColumn<Part, String> colCategory;

    @FXML
    private TableColumn<Part, String> colDate;

    @FXML
    private Label lblTotalParts;

    @FXML
    private Label lblTotalStock;

    @FXML
    private Label lblInventoryValue;

    @FXML
    public void initialize() {

        System.out.println("Dashboard Loaded");

        cmbCategory.getItems().addAll(
                "Engine",
                "Electrical",
                "Bodywork",
                "Accessories",
                "Suspension"
        );

        colCode.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("partName"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        tableParts.getSelectionModel().selectedItemProperty().addListener(

                (observable, oldValue, selectedPart) -> {

                    if (selectedPart != null) {

                        txtPartCode.setText(selectedPart.getPartCode());
                        txtPartName.setText(selectedPart.getPartName());
                        txtBrand.setText(selectedPart.getBrand());
                        txtPrice.setText(String.valueOf(selectedPart.getPrice()));
                        txtQuantity.setText(String.valueOf(selectedPart.getQuantity()));
                        cmbCategory.setValue(selectedPart.getCategory());
                        txtDate.setText(selectedPart.getDate());

                    }

                }

        );

        updateInventorySummary();
    }

    private void updateInventorySummary() {

        InventoryFileHandler fileHandler = new InventoryFileHandler();

        List<Part> parts = fileHandler.getAllParts();

        int totalParts = parts.size();
        int totalStock = 0;
        double totalValue = 0;

        for (Part part : parts) {

            totalStock += part.getQuantity();

            totalValue += part.getPrice() * part.getQuantity();

        }

        lblTotalParts.setText(String.valueOf(totalParts));

        lblTotalStock.setText(String.valueOf(totalStock));

        lblInventoryValue.setText(
                "Rs. " + String.format("%.2f", totalValue)
        );

    }

}