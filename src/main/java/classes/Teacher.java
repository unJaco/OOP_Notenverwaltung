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
        String sql = "SELECT * FROM GRADES AS g INNER JOIN STUDENTS AS s ON g.UID = s.UID INNER JOIN TEACHER AS t ON s.CLASS_ID = t.CLASS_ID INNER JOIN USER AS u ON u.UID = s.UID";
        ResultSet rs = DBHelper.executeSqlSelectStatement(sql);

        //TODO Ausgabe ändern

        
        while (rs.next()) {
            if (Arrays.asList(subjects).contains(rs.getString("SUBJECT")) && Arrays.asList(classes).contains(rs.getString("CLASS_ID"))) {
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
