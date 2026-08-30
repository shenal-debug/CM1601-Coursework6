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

                    Dealer dealer = new Dealer(

                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim()

                    );

                    dealerList.add(dealer);

                }

            }

            reader.close();

        } catch (IOException e) {

            System.out.println("Unable to load dealers.");

        }

        return dealerList;

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