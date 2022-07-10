package classes;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Teacher extends User {

    private SubjectOld[] subjects;

    /**
     * @return Array return the subjects
     */
    public SubjectOld[] getSubjects() {
        return subjects;
    }

    /**
     * @param subjects the subjects to set
     */
    public void setSubjects(SubjectOld[] subjects) {
        this.subjects = subjects;
    }

    public Teacher(int id, String firstname, String lastname,Role role, SubjectOld[] subjects) {
        super(id, firstname, lastname, role);
        this.subjects = subjects;
    }

    public Teacher(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("ID"), resultSet.getString("VORNAME"), resultSet.getString("NAME"),Role.valueOf(resultSet.getString("ROLE")));
        this.subjects = null;
    }
}
