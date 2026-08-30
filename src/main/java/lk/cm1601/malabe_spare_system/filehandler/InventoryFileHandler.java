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

    public List<Part> getAllParts() {

        List<Part> partList = new ArrayList<>();

        try {

            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/resources/data/inventory_legacy.txt"));

            String line;

            while ((line = reader.readLine()) != null) {

                String cleanedLine = line.replace("|", ",").replace(";", ",");

                String[] data = cleanedLine.split("\\s*,\\s*", -1);

                if (data.length >= 7) {

                    try {

                        String partCode = data[0].trim();
                        String partName = data[1].trim();
                        String brand = data[2].trim();

                        double price = Double.parseDouble(
                                data[3]
                                        .replace("Rs.", "")
                                        .replace("Rs", "")
                                        .trim()
                        );

                        int quantity = Integer.parseInt(data[4].trim());

                        String category = data[5].trim();
                        String date = data[6].trim();

                        String image = "";

                        if (data.length > 7) {
                            image = data[7].trim();
                        }

                        Part part = new Part(
                                partCode,
                                partName,
                                brand,
                                price,
                                quantity,
                                category,
                                date,
                                image
                        );

                        partList.add(part);

                    } catch (Exception e) {

                        // Skip invalid records

                    }

                }

            }

            reader.close();

        } catch (IOException e) {

            System.out.println("Unable to load inventory.");

        }

        return partList;

    }

}