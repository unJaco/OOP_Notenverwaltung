package classes;

import db.DBHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin extends User {

    public Admin(int id, String firstname, String lastname, Role role) {
        super(id, firstname, lastname, role);
    }

    public Admin(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("ID"), resultSet.getString("VORNAME"), resultSet.getString("NAME"),Role.valueOf(resultSet.getString("ROLE")));
    }

    public boolean createUser(String firstname, String lastname, String email, Role role) throws SQLException {
        User toCreate = null;
        switch (role){
            case ADMIN -> toCreate = new Admin(1,firstname, lastname, role);

            case STUDENT -> toCreate = new Student(1, firstname, lastname, role);

            case TEACHER -> toCreate = new Teacher(1, firstname, lastname,role, new SubjectOld[0]);
        }

        return DBHelper.insertUser(toCreate, email);

    }

    //public boolean deleteUser(){}

    //public boolean createClassroom(){}

    //public boolean deleteClassroom(){}

    //public boolean insertStudentToClassroom(){}

    //public boolean deleteStudentFromClassroom(){}

}
