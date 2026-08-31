package lk.cm1601.malabe_spare_system.model;

public class CartItem {

    private String partCode;
    private String partName;
    private double price;
    private int quantity;
    private double discountPercentage;

    public CartItem(String partCode, String partName, double price, int quantity) {

        this.partCode = partCode;
        this.partName = partName;
        this.price = price;
        this.quantity = quantity;
        this.discountPercentage = 0.0;

    }

    public String getPartCode() {
        return partCode;
    }

    public String getPartName() {
        return partName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscountLabel() {

        if (discountPercentage > 0) {

            return (int) discountPercentage + "%";

        }

        return "-";

    }

    public double getSubtotal() {

        double rawTotal = price * quantity;

        return rawTotal - (rawTotal * discountPercentage / 100.0);

    }

}