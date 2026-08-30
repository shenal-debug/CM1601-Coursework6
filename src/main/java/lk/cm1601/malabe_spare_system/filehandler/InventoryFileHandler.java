package lk.cm1601.malabe_spare_system.filehandler;

import lk.cm1601.malabe_spare_system.model.Part;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryFileHandler {

    public void readInventoryFile() {

        try {

            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/resources/data/inventory_legacy.txt"));

            String line;

            while ((line = reader.readLine()) != null) {

                System.out.println(line);

            }

            reader.close();

        } catch (IOException e) {

            System.out.println("File cannot be read.");

        }

    }

}