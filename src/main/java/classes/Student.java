package src.main.java.classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import src.main.java.db.DBHelper;

public class Student extends User {

    @Override
    public void onlogin(User user) throws SQLException {
        String sql = "SELECT * FROM sqlGrades";
        ResultSet rs = DBHelper.executeSqlSelectStatement(sql);

        //TODO Ausgabe ändern
        //BEZ könnte falsch sein

        while (rs.next()) {
            if (rs.getInt("ID") == user.getId()) {
                    System.out.println(rs.getString("SUBJECT") + "   " + 
                               rs.getInt("GRADEVALUE") + "   " +
                               rs.getString("BEZ"));
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
