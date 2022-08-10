package classes;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import db.*;
import javafx.MainApplication;

public class Teacher extends User {

    private ArrayList<SubjectInClass> subjectsInClasses = new ArrayList<>();

    private Map<String, SchoolClass> schoolClassMap = new HashMap<>();

    public Teacher() {
    }

    public Teacher(Integer id, String firstname, String lastname, Role role) {
        super(id, firstname, lastname, role);
    }

    public Teacher(ResultSet resultSet) throws SQLException {

        super(resultSet.getInt("UID"), resultSet.getString("VORNAME"), resultSet.getString("NAME"), Role.valueOf(resultSet.getString("ROLE")));

    }

    public ArrayList<SubjectInClass> getSubjectsInClasses() {
        return subjectsInClasses;
    }

    public void setSubjectsInClasses(ArrayList<SubjectInClass> subjectsInClasses) {
        this.subjectsInClasses = subjectsInClasses;
    }

    public Map<String, SchoolClass> getSchoolClassMap() {
        return schoolClassMap;
    }

    public void setSchoolClassMap(Map<String, SchoolClass> schoolClassMap) {
        this.schoolClassMap = schoolClassMap;
    }

    private void getClassesAndSubjectsFromTeacher() throws SQLException {

        subjectsInClasses = new ArrayList<>();

        String sql = "SELECT * FROM TEACHER WHERE (UID = '" + super.getId() + "');";
        ResultSet rs = DBHelper.executeSqlSelectStatement(sql);

        while (rs.next()) {
            subjectsInClasses.add(new SubjectInClass(rs));
        }

    }

    //inserts a grade
    public boolean insertGrade(String class_id, Subject subject, int studentId, int gradeVal, String gradeBez) throws SQLException {

        SubjectInClass sic = new SubjectInClass(null, class_id, subject);


        if (!subjectsInClasses.contains(sic)) {
            System.out.println("You dont have that Subject in this Class!");
            return false;
        }

        if (!schoolClassMap.containsKey(class_id)) {
            System.out.println("You dont have that Class!");
            return false;
        }

        schoolClassMap.get(class_id).studentMap.get(studentId);

        return DBHelper.insertGrade(class_id, studentId, new Grade(null, gradeVal, gradeBez, subject));
    }

    public void deleteGrade(int gradeId, int uid) throws SQLException {
        DBHelper.deleteGrade(gradeId, uid);
    }

    //fills schoolClassMap with pupils
    private void getSchoolClass(String classId) throws SQLException {

        SchoolClass schoolClass = new SchoolClass(classId, new HashMap<>());

        String sql = "SELECT * FROM STUDENTS WHERE (CLASS_ID = '" + classId + "');";
        ResultSet rs = DBHelper.executeSqlSelectStatement(sql);

        List<Integer> idList = new ArrayList<>();

        while (rs.next()) {
            idList.add(rs.getInt("UID"));
        }
        rs.close();

        for (int id : idList) {

            User user = DBHelper.getUser(id);
            if (user != null) {
                if (user.getClass() == Student.class) {
                    user.onCreation();
                    schoolClass.addStudent((Student) user);
                }
            }
        }

        schoolClassMap.put(classId, schoolClass);
    }


    @Override
    public void onCreation() throws SQLException {

        getClassesAndSubjectsFromTeacher();

        for (SubjectInClass s : subjectsInClasses) {
            if (!schoolClassMap.containsKey(s.getClassId())) {
                getSchoolClass(s.getClassId());
            }
        }

    }

    @Override
    public void switchScene() throws IOException {
        MainApplication.changeScene("menu-view-teacher.fxml", "Ihre Klassen");
    }


}


