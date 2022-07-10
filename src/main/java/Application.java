import classes.User;
import db.DBHelper;

import java.sql.SQLException;
import java.util.Scanner;

public class Application {

    static boolean loggedIn = false;
    static User user = null;

    public static void main(String[] args) throws SQLException {

        try {
            DBHelper.connectToDb();
        } catch (SQLException e){
            System.exit(0);
        }

        System.out.println("Email und Passwort eingeben:");
        while (!loggedIn){

            Scanner scanner = new Scanner(System.in);

            String email = scanner.next();

            String pass = scanner.next();

            User userFromLogin = DBHelper.tryToLogin(email, pass);
            if(userFromLogin != null){
                loggedIn = true;
                user = userFromLogin;
            } else {
                System.out.println("Falsche Eingabe.\nBitte erneut eingeben!");
            }

        }

        System.out.println("Success!");
    }
}