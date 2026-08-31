package lk.cm1601.malabe_spare_system;

import lk.cm1601.malabe_spare_system.filehandler.DealerFileHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DealerFileHandlerTest {

    @Test
    void testDealerLoaded() {

        DealerFileHandler handler = new DealerFileHandler();

        assertFalse(handler.getAllDealers().isEmpty());

    }

    @Test
    void testRandomDealerSelection() {

        DealerFileHandler handler = new DealerFileHandler();

        assertEquals(4, handler.getRandomDealers().size());

    }

}