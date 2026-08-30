package lk.cm1601.malabe_spare_system.filehandler;

import lk.cm1601.malabe_spare_system.model.Part;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
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

    public Part searchPartByCode(String partCode) {

        List<Part> parts = getAllParts();

        for (Part part : parts) {

            if (part.getPartCode().equalsIgnoreCase(partCode)) {

                return part;

            }

        }

        return null;

    }

    public List<Part> searchParts(String keyword) {

        List<Part> allParts = getAllParts();
        List<Part> results = new ArrayList<>();

        keyword = keyword.toLowerCase();

        for (Part part : allParts) {

            if (part.getPartCode().toLowerCase().contains(keyword)
                    || part.getPartName().toLowerCase().contains(keyword)
                    || part.getBrand().toLowerCase().contains(keyword)
                    || part.getCategory().toLowerCase().contains(keyword)) {

                results.add(part);

            }

        }

        return results;

    }

    public boolean updatePart(Part updatedPart) {

        List<Part> partList = getAllParts();

        boolean updated = false;

        for (int i = 0; i < partList.size(); i++) {

            if (partList.get(i).getPartCode()
                    .equalsIgnoreCase(updatedPart.getPartCode())) {

                partList.set(i, updatedPart);
                updated = true;
                break;

            }

        }

        if (!updated) {

            return false;

        }

        try {

            FileWriter writer = new FileWriter(
                    "src/main/resources/data/inventory_legacy.txt");

            for (Part part : partList) {

                writer.write(
                        part.getPartCode() + "," +
                                part.getPartName() + "," +
                                part.getBrand() + "," +
                                part.getPrice() + "," +
                                part.getQuantity() + "," +
                                part.getCategory() + "," +
                                part.getDate() + "," +
                                part.getImage() +
                                System.lineSeparator()
                );

            }

            writer.close();

        } catch (IOException e) {

            return false;

        }

        return true;

    }

}