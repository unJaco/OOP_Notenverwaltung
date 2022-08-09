package classes;


import db.DBHelper;
import javafx.MainApplication;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Admin extends User {

    private ArrayList<Student> studentsList;
    private ArrayList<Teacher> teachersList;

    private ArrayList<SubjectInClass> allSubjectsInAllClasses;
    public Admin(Integer id, String firstname, String lastname, Role role) {
        super(id, firstname, lastname, role);
    }

    public Admin(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("UID"), resultSet.getString("VORNAME"), resultSet.getString("NAME"),Role.valueOf(resultSet.getString("ROLE")));
    }

    public ArrayList<Student> getStudentsList() {
        return studentsList;
    }

    public void setStudentsList(ArrayList<Student> studentsList) {
        this.studentsList = studentsList;
    }

    public ArrayList<Teacher> getTeachersList() {
        return teachersList;
    }

    public void setTeachersList(ArrayList<Teacher> teachersList) {
        this.teachersList = teachersList;
    }

    public ArrayList<SubjectInClass> getAllSubjectsInAllClasses() {
        return allSubjectsInAllClasses;
    }

    public void setAllSubjectsInAllClasses(ArrayList<SubjectInClass> allSubjectsInAllClasses) {
        this.allSubjectsInAllClasses = allSubjectsInAllClasses;
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

    public boolean deleteUser(int uid) throws SQLException {
        DBHelper.deleteUser(uid);
        return true;
    }

    private void getStudents(){
        String sql = "SELECT * FROM USER WHERE ROLE = 'STUDENT'";
        try {
            ResultSet rs = DBHelper.executeSqlSelectStatement(sql);
            while (rs.next()){
                Student student = new Student(rs);
                student.onCreation();
                studentsList.add(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getTeacher(){
        String sql = "SELECT * FROM USER WHERE ROLE = 'TEACHER'";
        try {
            ResultSet rs = DBHelper.executeSqlSelectStatement(sql);
            while (rs.next()){
                Teacher teacher = new Teacher(rs);
                teacher.onCreation();
                teachersList.add(teacher);
            }
            for (Teacher teacher : teachersList){
                allSubjectsInAllClasses.addAll(teacher.getSubjectsInClasses());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreation() throws SQLException {

        this.studentsList = new ArrayList<>();
        this.teachersList = new ArrayList<>();
        this.allSubjectsInAllClasses = new ArrayList<>();

        getStudents();
        getTeacher();

    }

    @Override
    public void switchScene() throws IOException {
        MainApplication.changeScene("menu-view-admin.fxml", "Admin Ansicht");
    }

}
