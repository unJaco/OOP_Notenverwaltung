package classes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import db.*;

public class Teacher extends User {

    private String[] classes;

    private Subject[] subjects;
    

    @Override
    public void onLogin() throws SQLException {
        String sql = "SELECT * FROM sqlGrades AS g INNER JOIN sqlStudent AS s ON g.ID = s.ID INNER JOIN sqlTeacher AS t ON s.BEZKL = t.BEZKL INNER JOIN sqlUser AS u ON u.ID = s.ID";
        ResultSet rs = DBHelper.executeSqlSelectStatement(sql);

        //TODO Ausgabe ändern
        //BEZKL könnte falsch sein

        while (rs.next()) {
            if (Arrays.asList(subjects).contains(rs.getString("SUBJECT")) && Arrays.asList(classes).contains(rs.getString("BEZKL"))) {
               System.out.println(rs.getString("FIRSTNAME") + "   " + 
                               rs.getString("NAME") + "   " +
                               rs.getString("SUBJECT") + "   " +
                               rs.getInt("GRADE_Val")+ "   " +
                               rs.getString("GRADE_BEZ")); 
            }
            
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


    /*
        maybe add subject ???
     */
    public Teacher(Integer id, String firstname, String lastname, Role role) {
        super(id, firstname, lastname, role);
    }

    public Teacher(ResultSet resultSet) throws SQLException {

        super(resultSet.getInt("UID"), resultSet.getString("VORNAME"), resultSet.getString("NAME"),Role.valueOf(resultSet.getString("ROLE")));

    }

    
}
