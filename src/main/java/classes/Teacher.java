package classes;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Teacher extends User {




    public Teacher(Integer id, String firstname, String lastname,Role role) {
        super(id, firstname, lastname, role);
    }

    public Teacher(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("UID"), resultSet.getString("VORNAME"), resultSet.getString("NAME"),Role.valueOf(resultSet.getString("ROLE")));
    }
}
