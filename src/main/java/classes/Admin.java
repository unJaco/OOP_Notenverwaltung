package classes;


import db.DBHelper;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin extends User {

    public Admin(Integer id, String firstname, String lastname, Role role) {
        super(id, firstname, lastname, role);
    }

    public Admin(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("UID"), resultSet.getString("VORNAME"), resultSet.getString("NAME"),Role.valueOf(resultSet.getString("ROLE")));
    }

    public boolean createUser(String firstname, String lastname, String email, Role role, String class_id) throws SQLException {

        User toCreate = null;
        switch (role){
            case ADMIN -> toCreate = new Admin(1,firstname, lastname, role);

            case STUDENT -> toCreate = new Student(1, firstname, lastname, role);

            case TEACHER -> toCreate = new Teacher(1, firstname, lastname,role);
        }

        return DBHelper.insertUser(toCreate, email, class_id);

    }

    public boolean addSubjectToTeacher(int teacherId, Subject subject, String class_id) throws SQLException {

        return DBHelper.addTeacherWithSubjectToClass(teacherId, class_id, subject);
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
    public void onCreation() throws SQLException {
        String sql = "SELECT * FROM GRADES AS g INNER JOIN GRADES AS s ON g.UID = s.UID INNER JOIN TEACHER AS t ON s.CLASS_ID = t.CLASS_ID INNER JOIN USER AS u ON u.UID = s.UID";
        ResultSet rs = DBHelper.executeSqlSelectStatement(sql);

        //TODO Ausgabe ändern
        //BEZKL könnte falsch sein

        while (rs.next()) { 
               System.out.println(rs.getString("VORNAME") + "   " +
                               rs.getString("NAME") + "   " +
                               rs.getString("SUBJECT") + "   " +
                               rs.getInt("GRADE_Val")+ "   " +
                               rs.getString("GRADE_BEZ"));    
        }
        
    }

    @Override
    public void switchScene() throws IOException {

    }

    //public boolean createClassroom(){}

    //public boolean deleteClassroom(){}

    //public boolean insertStudentToClassroom(){}

    //public boolean deleteStudentFromClassroom(){}

}
