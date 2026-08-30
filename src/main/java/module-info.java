module lk.cm1601.malabe_spare_system {
    requires javafx.controls;
    requires javafx.fxml;


    opens lk.cm1601.malabe_spare_system to javafx.fxml;
    exports lk.cm1601.malabe_spare_system;
}