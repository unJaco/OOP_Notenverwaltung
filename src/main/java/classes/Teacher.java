package src.main.java.classes;
import java.sql.ResultSet;
import java.sql.SQLException;
import db.DBHelper;

public class Teacher extends User {

    private Subject[] subjects;
    
    
    public void onlogin(User user) throws SQLException {
        String sql = "SELECT * FROM sqlGrades AS g INNER JOIN sqlStudent AS s ON g.ID = s.ID INNER JOIN sqlTeacher AS t ON s.BEZKL = t.BEZKL INNER JOIN sqlUser AS u ON u.ID = s.ID";
        ResultSet rs = DBHelper.executeSqlSelectStatement(sql);

        //TODO Ausgabe ändern
        //BEZ könnte falsch sein

        while (rs.next()) {
            System.out.println(rs.getString("FIRSTNAME") + "   " + 
                               rs.getString("NAME") + "   " +
                               rs.getString("SUBJECT") + "   " +
                               rs.getInt("GRADEVALUE")+ "   " +
                               rs.getString("BEZ"));
        }
    }


    /**
     * @return Array return the subjects
     */
    public Subject[] getSubjects() {
        return subjects;
    }

    /**
     * @param subjects the subjects to set
     */
    public void setSubjects(Subject[] subjects) {
        this.subjects = subjects;
    }

    public Teacher(int id, String firstname, String lastname,Role role, Subject[] subjects) {
        super(id, firstname, lastname, role);
    }

    public Teacher(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("ID"), resultSet.getString("VORNAME"), resultSet.getString("NAME"),Role.valueOf(resultSet.getString("ROLE")));
    }

    
}
