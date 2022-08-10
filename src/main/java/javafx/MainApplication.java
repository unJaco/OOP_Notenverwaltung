package javafx;

import classes.*;
import db.DBHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.SQLException;

//this is the Main Applications
//to start the Applications run this file
public class MainApplication extends Application {

    private static User user = null;
    private static Stage stage;

    //this is the main finction of the Application
    public static void main(String[] args) {
        try {
            DBHelper.connectToDb();
        } catch (SQLException e) {
            System.out.println("Die Verbindung mit der Datenbank konnte nicht hergestellt werden!");
            e.printStackTrace();
            System.exit(0);
        }
        launch();
    }

    //sets up the start page of the Application (Login View)
    @Override
    public void start(Stage stage) throws Exception {

        MainApplication.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Bitte geben Sie ihre Login Daten ein!");
        stage.setScene(scene);
        stage.show();
    }

    public static void changeScene(String fxml, String newTitle) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle(newTitle);
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, MainApplication::onWindowClose);
    }

    public static void onWindowClose(WindowEvent windowEvent) {
        System.exit(0);
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User newUser) throws SQLException, IOException {
        user = newUser;
        user.onCreation();
        user.switchScene();
    }

    public static void changePassword(String email, String password) throws SQLException {
        DBHelper.changeCredentials(email, password);
    }
}



