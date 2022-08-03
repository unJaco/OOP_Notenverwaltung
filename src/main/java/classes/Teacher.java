package classes;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import db.*;

public class Teacher extends User {

    private ArrayList<SubjectInClass> subjectsInClasses = new ArrayList<>();

    private Map<String, SchoolClass> schoolClassMap = new HashMap<>();

    public Teacher(Integer id, String firstname, String lastname, Role role) {
        super(id, firstname, lastname, role);
    }

    public Teacher(ResultSet resultSet) throws SQLException {

        super(resultSet.getInt("UID"), resultSet.getString("VORNAME"), resultSet.getString("NAME"),Role.valueOf(resultSet.getString("ROLE")));

    }

    public ArrayList<SubjectInClass> getSubjectsInClasses() {
        return subjectsInClasses;
    }

    public void setSubjectsInClasses(ArrayList<SubjectInClass> subjectsInClasses) {
        this.subjectsInClasses = subjectsInClasses;
    }

    private void getClassesAndSubjectsFromTeacher() throws SQLException {

        String sql = "SELECT * FROM TEACHER WHERE (UID = '" + super.getId() + "');";
        ResultSet rs = DBHelper.executeSqlSelectStatement(sql);

        while (rs.next()){
            subjectsInClasses.add(new SubjectInClass(rs));
        }

    }

    public boolean insertGrade(String class_id, Subject subject, int studentId, int gradeVal, String gradeBez) throws SQLException {

        SubjectInClass sic = new SubjectInClass(class_id, subject);


        if(!subjectsInClasses.contains(sic)){
            System.out.println("You dont have that Subject in this Class!");
            return false;
        }

        if(!schoolClassMap.containsKey(class_id)){
            System.out.println("You dont have that Class!");
            return false;
        }

        schoolClassMap.get(class_id).studentMap.get(studentId);

        return DBHelper.insertGrade(class_id, studentId, new Grade(null, gradeVal, gradeBez, subject));
    }


    /*
        TODO delete Grade
     */

    public void deleteGrade(int gradeId, int uid) throws SQLException {
        DBHelper.deleteGrade(gradeId, uid);
    }

    private void getSchoolClass(String classId) throws SQLException {

        SchoolClass schoolClass = new SchoolClass(classId, new HashMap<>());

        String sql = "SELECT * FROM STUDENTS WHERE (CLASS_ID = '" + classId + "');";
        ResultSet rs = DBHelper.executeSqlSelectStatement(sql);

        List<Integer> idList = new ArrayList<>();

        while (rs.next()){
            idList.add(rs.getInt("UID"));
        }
        rs.close();

        for (int id : idList){

            User user = DBHelper.getUser(id);
            if(user != null){
                if(user.getClass() == Student.class){
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

        for (SubjectInClass s: subjectsInClasses) {
            if(!schoolClassMap.containsKey(s.getClassId())){
                getSchoolClass(s.getClassId());
            }
        }

        String sql = "SELECT * FROM GRADES AS g INNER JOIN STUDENTS AS s ON g.UID = s.UID INNER JOIN TEACHER AS t ON s.CLASS_ID = t.CLASS_ID INNER JOIN USER AS u ON u.UID = s.UID";
        ResultSet rs = DBHelper.executeSqlSelectStatement(sql);

        //TODO Ausgabe ändern


    }

    @Override
    public void switchScene() throws IOException {

    }


    /*
    public Subject[] getSubjects() {
        return subjects;
    }

    public void setSubjects(Subject[] subjects) {
        this.subjects = subjects;
    }*/

}


