
import classes.*;
import db.DBHelper;


import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Application {

    static boolean loggedIn = false;
    static User user = null;

    public static void main(String[] args) throws SQLException {

        try {
            DBHelper.connectToDb();
        } catch (SQLException e) {
            System.exit(0);
        }

        System.out.println("Email und Passwort eingeben:");
        while (!loggedIn) {

            Scanner scanner = new Scanner(System.in);

            String email = scanner.next();

            String pass = scanner.next();

            User userFromLogin = DBHelper.tryToLogin(email, pass);
            if (userFromLogin != null) {
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

        if (input == 1) {

            Student student = (Student) user;

            List g = student.displayGrades(Subject.INFORMATIK);

            double d = student.calcAverage(g);

            System.out.println(g);
            System.out.println(d);
        } else if (input == 2) {
            if (user.getClass() == Admin.class) {
                Admin admin = (Admin) user;

                boolean b = admin.createUser("S3", "S3", "student3", Role.STUDENT, "7A");
                System.out.println(b);
            } else {
                System.out.println("You're not authorized to do that!");
            }
        } else if (input == 3) {

            DBHelper.insertGrade(new Grade(null, 2, "TEST", Subject.INFORMATIK), (Student) user, Subject.INFORMATIK, "7B");
            DBHelper.insertGrade(new Grade(null, 1, "KLASSENARBEIT", Subject.INFORMATIK), (Student) user, Subject.INFORMATIK, "7B");
            DBHelper.insertGrade(new Grade(null, 3, "KLASSENARBEIT", Subject.DEUTSCH), (Student) user, Subject.DEUTSCH, "7B");
        }

    }
}