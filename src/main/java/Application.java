import db.DBHelper;

import java.sql.SQLException;
import java.util.Scanner;

public class Application {

    static boolean loggedIn = false;

    public static void main(String[] args) throws SQLException {

        try {
            DBHelper.connectToDb();
        } catch (SQLException e){
            System.out.println("Das jz blöd");
            System.exit(0);
        }

        System.out.println("Email und Passwort eingeben:");
        while (!loggedIn){

            Scanner scanner = new Scanner(System.in);

            String email = scanner.next();

            String pass = scanner.next();

            boolean success = DBHelper.tryToLogin(email, pass);
            if(success){
                loggedIn = success;
            } else {
                System.out.println("Falsche Eingabe.\nBitte erneut eingeben!");
            }

        }

        System.out.println("Successfully LoggedIn");


    }
}