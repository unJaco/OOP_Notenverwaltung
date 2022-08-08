module OOP_Notenverwaltung {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    exports javafx;

    opens controller to javafx.controls, javafx.fxml;
    exports controller;

    opens classes to javafx.base;
    opens javafx to javafx.controls, javafx.fxml;


}