package classes;


import db.DBHelper;


import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin extends User {

    public Admin(Integer id, String firstname, String lastname, Role role) {
        super(id, firstname, lastname, role);
    }

    public Admin(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("UID"), resultSet.getString("VORNAME"), resultSet.getString("NAME"),Role.valueOf(resultSet.getString("ROLE")));
    }

    public boolean createUser(String firstname, String lastname, String email, Role role) throws SQLException {
        User toCreate = null;
        switch (role){
            case ADMIN -> toCreate = new Admin(1,firstname, lastname, role);

            case STUDENT -> toCreate = new Student(1, firstname, lastname, role);

            case TEACHER -> toCreate = new Teacher(1, firstname, lastname,role);
        }

        return DBHelper.insertUser(toCreate, email);

    }

    /*
        TODO DB add CLASS_IDS Table
     */

    public boolean createClass_Id(String class_id){

        return false;
    }

    /*
        TODO delete stuff
     */

    public boolean deleteUser(int uid){

        DBHelper.deleteUser(uid);
        return true;
    }

    @Override
    public void onlogin(User user) throws SQLException {
        String sql = "SELECT * FROM sqlGrades AS g INNER JOIN sqlStudent AS s ON g.ID = s.ID INNER JOIN sqlTeacher AS t ON s.BEZKL = t.BEZKL INNER JOIN sqlUser AS u ON u.ID = s.ID";
        ResultSet rs = DBHelper.executeSqlSelectStatement(sql);

        //TODO Ausgabe ändern
        //BEZKL könnte falsch sein

        while (rs.next()) { 
               System.out.println(rs.getString("FIRSTNAME") + "   " + 
                               rs.getString("NAME") + "   " +
                               rs.getString("SUBJECT") + "   " +
                               rs.getInt("GRADE_Val")+ "   " +
                               rs.getString("GRADE_BEZ"));    
        }
        
    }

    //public boolean createClassroom(){}

    //public boolean deleteClassroom(){}

    //public boolean insertStudentToClassroom(){}

    //public boolean deleteStudentFromClassroom(){}

}
