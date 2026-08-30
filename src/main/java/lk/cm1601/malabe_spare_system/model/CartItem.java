package lk.cm1601.malabe_spare_system.model;

public class CartItem {

    private Part part;
    private int quantity;
    private double subtotal;

    public CartItem(Part part, int quantity, double subtotal) {
        this.part = part;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}