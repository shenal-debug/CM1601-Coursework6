package lk.cm1601.malabe_spare_system.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.cm1601.malabe_spare_system.filehandler.InventoryFileHandler;
import lk.cm1601.malabe_spare_system.model.CartItem;
import lk.cm1601.malabe_spare_system.model.Part;

public class PosController {

    private static final int BULK_DISCOUNT_MIN_QTY = 3;
    private static final double BULK_DISCOUNT_RATE = 5.0;

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

    @FXML
    private Label lblCartTotal;

    @FXML
    private TableView<CartItem> tableCart;

    @FXML
    private TableColumn<CartItem, String> colCartCode;

    @FXML
    private TableColumn<CartItem, String> colCartName;

    @FXML
    private TableColumn<CartItem, Double> colCartPrice;

    @FXML
    private TableColumn<CartItem, Integer> colCartQty;

    @FXML
    private TableColumn<CartItem, String> colCartDiscount;

    @FXML
    private TableColumn<CartItem, Double> colCartSubtotal;

    private final ObservableList<CartItem> cartList = FXCollections.observableArrayList();

    // Holds the part that was found by the last successful search, so
    // "Add to Cart" knows which part to work with.
    private Part currentPart;

    @FXML
    public void initialize() {

        colCartCode.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        colCartName.setCellValueFactory(new PropertyValueFactory<>("partName"));
        colCartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCartDiscount.setCellValueFactory(new PropertyValueFactory<>("discountLabel"));
        colCartSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tableCart.setItems(cartList);

    }

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

    private CartItem findCartItem(String partCode) {

        for (CartItem item : cartList) {

            if (item.getPartCode().equalsIgnoreCase(partCode)) {

                return item;

            }

        }

        return null;

    }

    private void applyBulkDiscount(CartItem item) {

        if (item.getQuantity() >= BULK_DISCOUNT_MIN_QTY) {

            item.setDiscountPercentage(BULK_DISCOUNT_RATE);

        } else {

            item.setDiscountPercentage(0.0);

        }

    }

    private void recalculateCartTotal() {

        double total = 0;

        for (CartItem item : cartList) {

            total += item.getSubtotal();

        }

        lblCartTotal.setText("Rs. " + String.format("%.2f", total));

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

        CartItem existingItem = findCartItem(currentPart.getPartCode());

        int alreadyInCart = (existingItem != null) ? existingItem.getQuantity() : 0;

        if (quantity + alreadyInCart > currentPart.getQuantity()) {

            lblMessage.setText("Not enough stock available. Only " +
                    currentPart.getQuantity() + " left in stock.");
            return;

        }

        if (existingItem != null) {

            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            applyBulkDiscount(existingItem);

        } else {

            CartItem newItem = new CartItem(
                    currentPart.getPartCode(),
                    currentPart.getPartName(),
                    currentPart.getPrice(),
                    quantity
            );

            applyBulkDiscount(newItem);

            cartList.add(newItem);

        }

        tableCart.refresh();
        recalculateCartTotal();

        lblMessage.setStyle("-fx-text-fill: green;");
        lblMessage.setText(quantity + " x " + currentPart.getPartName() + " added to cart.");

    }

    @FXML
    private void handleRemoveFromCart() {

        CartItem selectedItem = tableCart.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {

            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Please select a cart item to remove.");
            return;

        }

        cartList.remove(selectedItem);

        recalculateCartTotal();

        lblMessage.setStyle("-fx-text-fill: green;");
        lblMessage.setText("Item removed from cart.");

    }

    @FXML
    private void handleClosePos() {

        Stage stage = (Stage) txtSearchCode.getScene().getWindow();
        stage.close();

    }

}