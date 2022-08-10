package classes;

import db.DBHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {


    Teacher t2 = new Teacher(null,"Test2", "Teacher2",Role.TEACHER);
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
    void insertGrade() throws SQLException {
        Teacher t1 = (Teacher)DBHelper.getUser(DBHelper.getIDEmail("teacher"));
        Assertions.assertFalse(t1.insertGrade("7B",Subject.INFORMATIK,DBHelper.getIDEmail("student"),6,"Test"));
    }

    @Test
    void insertGradeInWrongClass() throws SQLException {
        Assertions.assertFalse(t2.insertGrade("7B",Subject.DEUTSCH,1,6,"Test"));
    }
}