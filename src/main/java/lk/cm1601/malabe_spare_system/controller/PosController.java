package lk.cm1601.malabe_spare_system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.cm1601.malabe_spare_system.filehandler.InventoryFileHandler;
import lk.cm1601.malabe_spare_system.model.Part;

public class PosController {

    @FXML
    private TextField txtSearchCode;

    @FXML
    private TextField txtQuantity;

    @FXML
    private Label lblPartName;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblStock;

    @FXML
    private Label lblMessage;

    // Holds the part that was found by the last successful search, so
    // "Add to Cart" knows which part to work with.
    private Part currentPart;

    @FXML
    private void handleSearchPart() {

        String code = txtSearchCode.getText().trim();

        if (code.isEmpty()) {

            lblMessage.setText("Please enter a Part Code to search.");
            currentPart = null;
            clearPartDetails();
            return;

        }

        InventoryFileHandler fileHandler = new InventoryFileHandler();

        Part part = fileHandler.searchPartByCode(code);

        if (part == null) {

            lblMessage.setText("No part found with code: " + code);
            currentPart = null;
            clearPartDetails();
            return;

        }

        currentPart = part;

        lblPartName.setText(part.getPartName());
        lblPrice.setText("Rs. " + String.format("%.2f", part.getPrice()));
        lblStock.setText(String.valueOf(part.getQuantity()));

        lblMessage.setText("");

    }

    private void clearPartDetails() {

        lblPartName.setText("-");
        lblPrice.setText("-");
        lblStock.setText("-");

    }

    @FXML
    private void handleAddToCart() {

        if (currentPart == null) {

            lblMessage.setText("Please search for a valid part first.");
            return;

        }

        String qtyText = txtQuantity.getText().trim();

        if (qtyText.isEmpty()) {

            lblMessage.setText("Please enter a quantity.");
            return;

        }

        int quantity;

        try {

            quantity = Integer.parseInt(qtyText);

        } catch (NumberFormatException e) {

            lblMessage.setText("Quantity must be a valid whole number.");
            return;

        }

        if (quantity <= 0) {

            lblMessage.setText("Quantity must be greater than zero.");
            return;

        }

        if (quantity > currentPart.getQuantity()) {

            lblMessage.setText("Not enough stock available. Only " +
                    currentPart.getQuantity() + " left in stock.");
            return;

        }

        // Cart table + real cart storage is added in the next phase.
        // For now this confirms the search + quantity validation works end-to-end.

        lblMessage.setStyle("-fx-text-fill: green;");
        lblMessage.setText(quantity + " x " + currentPart.getPartName() + " validated - ready to add to cart.");

    }

    @FXML
    private void handleClosePos() {

        Stage stage = (Stage) txtSearchCode.getScene().getWindow();
        stage.close();

    }

}