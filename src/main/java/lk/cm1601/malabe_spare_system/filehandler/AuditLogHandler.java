package lk.cm1601.malabe_spare_system.filehandler;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AuditLogHandler {

    public void writeLog(String action, String details) {

        try {

            FileWriter writer = new FileWriter(
                    "src/main/resources/data/audit_log.txt", true);

            writer.write(
                    LocalDateTime.now() +
                            " | " +
                            action +
                            " | " +
                            details +
                            System.lineSeparator()
            );

            writer.close();

        } catch (IOException e) {

            System.out.println("Unable to write audit log.");

        }

    }

}