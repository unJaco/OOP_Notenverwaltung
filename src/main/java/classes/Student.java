package src.main.java.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Student extends User {


    //when creating User manually there is no Id - so Id is manually set to null therefore the Type needs to be Integer not int
    public Student(Integer id, String firstname, String lastname, Role role) {
        super(id, firstname, lastname, role);
    }

    public Student(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("ID"), resultSet.getString("VORNAME"), resultSet.getString("NAME"),Role.valueOf(resultSet.getString("ROLE")));

    }

}
