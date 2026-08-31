package lk.cm1601.malabe_spare_system;

import lk.cm1601.malabe_spare_system.filehandler.InventoryFileHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryFileHandlerTest {

    @Test
    void testInventoryLoaded() {

        InventoryFileHandler handler = new InventoryFileHandler();

        assertFalse(handler.getAllParts().isEmpty());

    }

    @Test
    void testSearchPart() {

        InventoryFileHandler handler = new InventoryFileHandler();

        assertNotNull(handler.searchPartByCode("P001"));

    }

}