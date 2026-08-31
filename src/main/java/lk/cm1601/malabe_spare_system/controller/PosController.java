package lk.cm1601.malabe_spare_system.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.cm1601.malabe_spare_system.filehandler.AuditLogHandler;
import lk.cm1601.malabe_spare_system.filehandler.InventoryFileHandler;
import lk.cm1601.malabe_spare_system.model.CartItem;
import lk.cm1601.malabe_spare_system.model.Part;

public class PosController {

    private static final int BULK_DISCOUNT_MIN_QTY = 3;
    private static final double BULK_DISCOUNT_RATE = 5.0;

    private static final double SYNERGY_DISCOUNT_RATE = 10.0;
    private static final String SYNERGY_CATEGORY_1 = "Engine";
    private static final String SYNERGY_CATEGORY_2 = "Electrical";

    @FXML
    private TableView<Part> tableInventory;

    @FXML
    private TableColumn<Part, String> colCode;

    @FXML
    private TableColumn<Part, String> colName;

    @FXML
    private TableColumn<Part, Double> colPrice;

    @FXML
    private TableColumn<Part, Integer> colQuantity;

    @FXML
    private TableColumn<Part, String> colCategory;

    @FXML
    private TextField txtQuantity;

    @FXML
    private Label lblMessage;

    @FXML
    private Label lblCartTotal;

    @FXML
    private Label lblSynergyStatus;

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
    private TableColumn<CartItem, String> colCartCategory;

    @FXML
    private TableColumn<CartItem, String> colCartDiscount;

    @FXML
    private TableColumn<CartItem, Double> colCartSubtotal;

    private final ObservableList<CartItem> cartList = FXCollections.observableArrayList();

    private boolean synergyDiscountApplied = false;

    @FXML
    public void initialize() {

        colCode.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("partName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        InventoryFileHandler fileHandler = new InventoryFileHandler();

        ObservableList<Part> partList =
                FXCollections.observableArrayList(fileHandler.getAllParts());

        tableInventory.setItems(partList);

        colCartCode.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        colCartName.setCellValueFactory(new PropertyValueFactory<>("partName"));
        colCartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCartCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colCartDiscount.setCellValueFactory(new PropertyValueFactory<>("discountLabel"));
        colCartSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tableCart.setItems(cartList);

        lblSynergyStatus.setText("No synergy discount yet (add an Engine part and an Electrical part for 10% off).");

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

    private double recalculateCartTotal() {

        double itemTotal = 0;

        boolean hasEngine = false;
        boolean hasElectrical = false;

        for (CartItem item : cartList) {

            itemTotal += item.getSubtotal();

            if (item.getCategory().equalsIgnoreCase(SYNERGY_CATEGORY_1)) {

                hasEngine = true;

            }

            if (item.getCategory().equalsIgnoreCase(SYNERGY_CATEGORY_2)) {

                hasElectrical = true;

            }

        }

        synergyDiscountApplied = hasEngine && hasElectrical;

        double finalTotal = itemTotal;

        if (synergyDiscountApplied) {

            finalTotal = itemTotal * (1 - SYNERGY_DISCOUNT_RATE / 100.0);

            lblSynergyStatus.setStyle("-fx-text-fill: green;");
            lblSynergyStatus.setText("10% Synergy Discount Applied (cart has Engine + Electrical parts).");

        } else {

            lblSynergyStatus.setStyle("-fx-text-fill: gray;");
            lblSynergyStatus.setText("No synergy discount yet (add an Engine part and an Electrical part for 10% off).");

        }

        lblCartTotal.setText("Rs. " + String.format("%.2f", finalTotal));

        return finalTotal;

    }

    @FXML
    private void handleAddToCart() {

        Part selectedPart = tableInventory.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {

            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Please select a part from the table.");
            return;

        }

        String qtyText = txtQuantity.getText().trim();

        if (qtyText.isEmpty()) {

            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Please enter a quantity.");
            return;

        }

        int quantity;

        try {

            quantity = Integer.parseInt(qtyText);

        } catch (NumberFormatException e) {

            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Quantity must be a valid whole number.");
            return;

        }

        if (quantity <= 0) {

            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Quantity must be greater than zero.");
            return;

        }

        CartItem existingItem = findCartItem(selectedPart.getPartCode());

        int alreadyInCart = (existingItem != null) ? existingItem.getQuantity() : 0;

        if (quantity + alreadyInCart > selectedPart.getQuantity()) {

            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Not enough stock available. Only " +
                    selectedPart.getQuantity() + " left in stock.");
            return;

        }

        if (existingItem != null) {

            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            applyBulkDiscount(existingItem);

        } else {

            CartItem newItem = new CartItem(
                    selectedPart.getPartCode(),
                    selectedPart.getPartName(),
                    selectedPart.getCategory(),
                    selectedPart.getPrice(),
                    quantity
            );

            applyBulkDiscount(newItem);

            cartList.add(newItem);

        }

        tableCart.refresh();
        recalculateCartTotal();

        lblMessage.setStyle("-fx-text-fill: green;");
        lblMessage.setText(quantity + " x " + selectedPart.getPartName() + " added to cart.");

        txtQuantity.clear();

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

    // Finalizes the sale: deducts each cart item's quantity from the
    // inventory file, writes an audit log line per item plus a summary
    // line, shows the receipt total, then resets the screen for the
    // next sale.

    @FXML
    private void handleCheckout() {

        if (cartList.isEmpty()) {

            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Your cart is empty.");
            return;

        }

        double finalTotal = recalculateCartTotal();

        InventoryFileHandler fileHandler = new InventoryFileHandler();
        AuditLogHandler auditLogHandler = new AuditLogHandler();

        for (CartItem item : cartList) {

            Part currentPart = fileHandler.searchPartByCode(item.getPartCode());

            if (currentPart == null) {

                continue;

            }

            int remainingStock = currentPart.getQuantity() - item.getQuantity();

            Part updatedPart = new Part(

                    currentPart.getPartCode(),
                    currentPart.getPartName(),
                    currentPart.getBrand(),
                    currentPart.getPrice(),
                    remainingStock,
                    currentPart.getCategory(),
                    currentPart.getDate(),
                    currentPart.getImage()

            );

            fileHandler.updatePart(updatedPart);

            auditLogHandler.writeLog(
                    "CHECKOUT",
                    item.getPartCode() + " - Qty: " + item.getQuantity() +
                            " - Subtotal: Rs. " + String.format("%.2f", item.getSubtotal())
            );

        }

        String synergyNote = synergyDiscountApplied
                ? " (10% Synergy Discount Applied)"
                : "";

        auditLogHandler.writeLog(
                "CHECKOUT SUMMARY",
                "Items: " + cartList.size() +
                        " - Total: Rs. " + String.format("%.2f", finalTotal) + synergyNote
        );

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Checkout");
        alert.setHeaderText("Purchase Successful");
        alert.setContentText("Total Amount: Rs. " + String.format("%.2f", finalTotal) + synergyNote);
        alert.showAndWait();

        tableInventory.setItems(
                FXCollections.observableArrayList(fileHandler.getAllParts())
        );

        cartList.clear();

        lblCartTotal.setText("Rs. 0.00");
        lblSynergyStatus.setStyle("-fx-text-fill: gray;");
        lblSynergyStatus.setText("No synergy discount yet (add an Engine part and an Electrical part for 10% off).");

        lblMessage.setStyle("-fx-text-fill: green;");
        lblMessage.setText("Checkout complete. Inventory updated.");

        txtQuantity.clear();
        tableInventory.getSelectionModel().clearSelection();
        tableCart.getSelectionModel().clearSelection();

        synergyDiscountApplied = false;

    }

    @FXML
    private void handleClosePos() {

        Stage stage = (Stage) tableInventory.getScene().getWindow();
        stage.close();

    }

}