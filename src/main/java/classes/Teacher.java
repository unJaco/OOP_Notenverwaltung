package src.main.java.classes;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Teacher extends User {





    // subjets needs entire tree because javax.security.auth.Subject exists
    public Teacher(Integer id, String firstname, String lastname, Role role, src.main.java.classes.Subject[] subjects) {
        super(id, firstname, lastname, role);
        this.subjects = subjects;
    }

    public Teacher(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("UID"), resultSet.getString("VORNAME"), resultSet.getString("NAME"),Role.valueOf(resultSet.getString("ROLE")));
    }
}
