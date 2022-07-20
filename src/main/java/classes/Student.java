package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import db.*;

public class Student extends User {

    @Override
    public void onLogin() throws SQLException {
        String sql = "SELECT * FROM GRADES";
        ResultSet rs = DBHelper.executeSqlSelectStatement(sql);

        //TODO Ausgabe ändern
        //BEZ könnte falsch sein

        while (rs.next()) {
            if (rs.getInt("UID") == this.getId()) {
                    System.out.println(rs.getString("SUBJECT") + "   " + 
                               rs.getInt("GRADE_VAL") + "   " +
                               rs.getString("GRADE_BEZ"));
            }
            
        }
    }

    //when creating User manually there is no Id - so Id is manually set to null therefore the Type needs to be Integer not int
    public Student(Integer id, String firstname, String lastname, Role role) {
        super(id, firstname, lastname, role);
    }

    public Student(ResultSet resultSet) throws SQLException {

        super(resultSet.getInt("UID"), resultSet.getString("VORNAME"), resultSet.getString("NAME"),Role.valueOf(resultSet.getString("ROLE")));



    }

    

}
