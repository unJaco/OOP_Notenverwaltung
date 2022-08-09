package classes;

import db.DBHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

class AdminTest {

    Admin a = new Admin(1, "A", "dmin",Role.ADMIN);

    @BeforeAll
    static void setup(){
        try {
            DBHelper.connectToDb();
        } catch (SQLException e) {
            System.out.println("Die Verbindung mit der Datenbank konnte nicht hergestellt werden!");
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Test
    void createClass_Id() {
        Assertions.assertTrue(a.createClass_Id("13/3"));
        Assertions.assertTrue(a.createClass_Id("13/2"));
    }

    @Test
    void tryCreateExistingUser(){
        try{
            a.createUser("A", "A", "admin4",Role.ADMIN,"13/3");
            Assertions.assertTrue(a.createUser("Foo", "Boo", "foo@boo.de",Role.TEACHER,"13/3"));
        }
        catch (SQLException e){
            System.out.println("User besteht bereits.");
            Assertions.assertTrue(true);
        }

    }

    @Test
    void addSubjectToTeacher() throws SQLException {
        Assertions.assertTrue(a.addSubjectToTeacher(3,Subject.MATHE,"7B"));
    }


    @Test
    void deleteUser() {
        Assertions.assertTrue(a.deleteUser(120));
    }
}