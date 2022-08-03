package javafx;

import classes.*;
import db.DBHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainApplication extends Application {

    private static User user = null;
    private static Stage stage;


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
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User newUser) throws SQLException, IOException {
        user = newUser;
        user.switchScene();
        user.onCreation();
    }
}

/*

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

        user.onCreation();
        int input = scanner.nextInt();

        if (input == 1) {

            Student student = (Student) user;

            List<Grade> g = student.displayGrades(Subject.DEUTSCH);

            double d = student.calcAverage(g);

            System.out.println(g);
            System.out.println(d);
        } else if (input == 2) {
            if (user.getClass() == Admin.class) {
                Admin admin = (Admin) user;

                boolean b = admin.addSubjectToTeacher(2, Subject.DEUTSCH, "7B");
                System.out.println(b);


            } else {
                System.out.println("You're not authorized to do that!");
            }
        } else if (input == 3) {

            Teacher teacher = (Teacher) user;

            /*
            teacher.deleteGrade(12);
            teacher.deleteGrade(13);
            teacher.deleteGrade(14);
            */


            /*
            DBHelper.insertGrade(new Grade(null, 5, "MITARBEIT", Subject.INFORMATIK), (Student) user, Subject.INFORMATIK, "7B");
            DBHelper.insertGrade(new Grade(null, 1, "TEST", Subject.INFORMATIK), (Student) user, Subject.INFORMATIK, "7B");
            DBHelper.insertGrade(new Grade(null, 2, "TEST", Subject.INFORMATIK), (Student) user, Subject.INFORMATIK, "7B");
            DBHelper.insertGrade(new Grade(null, 2, "TEST", Subject.INFORMATIK), (Student) user, Subject.INFORMATIK, "7B");
            DBHelper.insertGrade(new Grade(null, 3, "TEST", Subject.INFORMATIK), (Student) user, Subject.INFORMATIK, "7B");
            DBHelper.insertGrade(new Grade(null, 3, "TEST", Subject.DEUTSCH), (Student) user, Subject.DEUTSCH, "7B");
            */



