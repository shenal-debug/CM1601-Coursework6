package lk.cm1601.malabe_spare_system.filehandler;

import lk.cm1601.malabe_spare_system.model.Dealer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DealerFileHandler {

    public List<Dealer> getAllDealers() {

        List<Dealer> dealerList = new ArrayList<>();

        try {

            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/resources/data/dealers_legacy.txt"));

            String line;

            while ((line = reader.readLine()) != null) {

                String cleanedLine = line.replace("|", ",")
                        .replace(";", ",");

                String[] data = cleanedLine.split("\\s*,\\s*", -1);

                if (data.length >= 4) {

                    String dealerID = data[0].trim();
                    String dealerName = data[1].trim();
                    String phone = data[2].trim();
                    String location = data[3].trim();

                    // Dealer ID and Dealer Name are critical - a record
                    // missing either of these cannot be used, so it is
                    // skipped instead of silently stored with blank data.

                    if (dealerID.isEmpty() || dealerName.isEmpty()) {

                        System.out.println(
                                "Skipping invalid dealer record (missing ID/Name): \"" + line + "\""
                        );

                        continue;

                    }

                    // Phone number is dirty in the legacy file (some rows have
                    // it blank, e.g. "D103; Ranatunga Auto; ; Pittugala").
                    // Instead of storing an empty/invalid phone silently,
                    // fall back to a visible placeholder so it is obvious
                    // in the UI that the data is incomplete.

                    if (phone.isEmpty() || !isValidPhone(phone)) {

                        System.out.println(
                                "Dealer " + dealerID + " has an invalid/missing phone number - using placeholder."
                        );

                        phone = "N/A";

                    }

                    Dealer dealer = new Dealer(

                            dealerID,
                            dealerName,
                            phone,
                            location

                    );

                    dealerList.add(dealer);

                } else {

                    System.out.println(
                            "Skipping malformed dealer record (missing fields): \"" + line + "\""
                    );

                }

            }

            reader.close();

        } catch (IOException e) {

            System.out.println("Unable to load dealers.");

        }

        return dealerList;

    }

    // A valid local phone number is treated as 10 digits (e.g. 0771234567).
    // This is checked manually, character by character.

    private boolean isValidPhone(String phone) {

        if (phone.length() != 10) {

            return false;

        }

        for (int i = 0; i < phone.length(); i++) {

            if (!Character.isDigit(phone.charAt(i))) {

                return false;

            }

        }

        return true;

    }

    public List<Dealer> getRandomDealers() {

        List<Dealer> allDealers = getAllDealers();

        List<Dealer> selectedDealers = new ArrayList<>();

        while (selectedDealers.size() < 4 && allDealers.size() > 0) {

            int randomIndex = (int) (Math.random() * allDealers.size());

            selectedDealers.add(allDealers.get(randomIndex));

            allDealers.remove(randomIndex);

        }

        return selectedDealers;

    }

    public List<Dealer> sortDealersByLocation(List<Dealer> dealerList) {

        for (int i = 0; i < dealerList.size() - 1; i++) {

            for (int j = 0; j < dealerList.size() - i - 1; j++) {

                if (dealerList.get(j).getLocation()
                        .compareToIgnoreCase(dealerList.get(j + 1).getLocation()) > 0) {

                    Dealer temp = dealerList.get(j);
                    dealerList.set(j, dealerList.get(j + 1));
                    dealerList.set(j + 1, temp);

                }

            }

        }

        return dealerList;

    }

}