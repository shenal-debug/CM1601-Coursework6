package lk.cm1601.malabe_spare_system;

import lk.cm1601.malabe_spare_system.model.Part;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PartTest {

    @Test
    void testPartCreation() {

        Part part = new Part(
                "P001",
                "Brake Pad",
                "TVS",
                1250.0,
                10,
                "Brakes",
                "2024-01-01",
                "brake.jpg"
        );

        assertEquals("P001", part.getPartCode());
        assertEquals("Brake Pad", part.getPartName());
        assertEquals("TVS", part.getBrand());
        assertEquals(1250.0, part.getPrice());
        assertEquals(10, part.getQuantity());
        assertEquals("Brakes", part.getCategory());
        assertEquals("2024-01-01", part.getDate());
        assertEquals("brake.jpg", part.getImage());
    }

}