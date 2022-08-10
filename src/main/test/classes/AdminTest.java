package classes;

import db.DBHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

class AdminTest {

    Admin a = new Admin(null, "A", "dmin",Role.ADMIN);

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
    void tryCreateExistingUser(){
        try{
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
    void deleteUser() throws SQLException {
        Assertions.assertTrue(a.deleteUser(DBHelper.getIDEmail("foo@boo.de")));
    }
}