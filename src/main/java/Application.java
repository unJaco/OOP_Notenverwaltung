
import classes.*;
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
        System.out.println("Actions... \n1 für Noten eintragen");

        Scanner scanner = new Scanner(System.in);


        user.onLogin();
        int input = scanner.nextInt();


        if(input == 1){

            Student student = (Student) user;

            //user.onLogin();


            //DBHelper.insertGrade(new Grade(null, 6, "Klassenarbeit", Subject.ENGLISCH), student, Subject.ENGLISCH, "7B");

        } else if(input == 2){
            if(user.getClass() == Admin.class){
                Admin admin = (Admin) user;

                boolean b = admin.createUser("S3", "S3", "student3", Role.STUDENT);
                System.out.println(b);
            } else {

                System.out.println("You're not authorized to do that!");
            }
        }

    }
}