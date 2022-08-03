package controller;

import classes.User;
import db.DBHelper;
import javafx.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private Label loginLabel;

    @FXML
    private TextField mailField;

    @FXML
    private TextField passField;

    @FXML
    protected void onLoginButtonClick() {

        User user;
        String labelText;

        try{
           user  = DBHelper.tryToLogin(mailField.getText(), passField.getText());
           if(user != null){
               MainApplication.setUser(user);
               return;
           }
           labelText = "Anmeldedaten nicht korrekt!";
        } catch (SQLException | IOException e){
            labelText = "Es gibt ein Fehler im System. Probieren Sie die Application neu zu starten";
        }

        loginLabel.setText(labelText);
        passField.clear();
        mailField.clear();
    }
}
