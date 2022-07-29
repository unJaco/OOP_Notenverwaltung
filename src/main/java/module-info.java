module OOP_Notenverwaltung {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens javafx to javafx.fxml;
    exports javafx;

    opens controller to javafx.controls, javafx.fxml;
    exports controller;


}