package classes;

import db.DBHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    Teacher t = new Teacher(99,"Test", "Teacher",Role.TEACHER);

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
    void insertGrade() throws SQLException {
        Assertions.assertTrue(t.insertGrade("7B",Subject.INFORMATIK,2,6,"Test"));
    }
}