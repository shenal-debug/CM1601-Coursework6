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

}