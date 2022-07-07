package classes;

import db.DBHelper;

import java.sql.SQLException;

public class Admin extends User {

    public Admin(int id, String firstname, String lastname, String email, Role role) {
        super(id, firstname, lastname, email, role);
    }

    public boolean createUser(String firstname, String lastname, String email, Role role) throws SQLException {
        User toCreate = null;
        switch (role){
            case ADMIN -> toCreate = new Admin(1,firstname, lastname, email, role);

            case STUDENT -> toCreate = new Student(1, firstname, lastname, email, role);

            case TEACHER -> toCreate = new Teacher(1, firstname, lastname, email,role, new Subject[0]);
        }

        return DBHelper.insertUser(toCreate);

    }

    //public boolean deleteUser(){}

    //public boolean createClassroom(){}

    //public boolean deleteClassroom(){}

    //public boolean insertStudentToClassroom(){}

    //public boolean deleteStudentFromClassroom(){}

}
