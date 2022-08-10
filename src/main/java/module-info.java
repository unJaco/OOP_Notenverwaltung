module OOP_Notenverwaltung {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.desktop;


    exports javafx;

    opens controller to javafx.controls, javafx.fxml;
    exports controller;

    opens classes to javafx.base;
    opens javafx to javafx.controls, javafx.fxml;


}
