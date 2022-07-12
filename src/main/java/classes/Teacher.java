package src.main.java.classes;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Teacher extends User {




    public Teacher(Integer id, String firstname, String lastname,Role role) {
        super(id, firstname, lastname, role);
    }

    public Teacher(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("ID"), resultSet.getString("VORNAME"), resultSet.getString("NAME"),Role.valueOf(resultSet.getString("ROLE")));
    }
}
