package lk.cm1601.malabe_spare_system;

import lk.cm1601.malabe_spare_system.model.CartItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartItemTest {

    @Test
    void testSubtotalWithNoDiscount() {

        CartItem item = new CartItem("P001", "Piston", "Engine", 4500.0, 2);

        assertEquals(9000.0, item.getSubtotal(), 0.001);
        assertEquals("-", item.getDiscountLabel());

    }

    @Test
    void testSubtotalWithBulkDiscountApplied() {

        CartItem item = new CartItem("P001", "Piston", "Engine", 4500.0, 3);
        item.setDiscountPercentage(5.0);

        assertEquals("5%", item.getDiscountLabel());
        assertEquals(12825.0, item.getSubtotal(), 0.001);

    }

}