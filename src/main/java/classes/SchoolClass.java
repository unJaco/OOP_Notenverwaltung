package classes;


import java.util.ArrayList;
import java.util.Map;

public class SchoolClass {

    private String class_id;

    Map<Integer, Student> studentMap;


    public SchoolClass(String class_id, Map<Integer, Student> studentMap) {
        this.class_id = class_id;
        this.studentMap = studentMap;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public Map<Integer, Student> getStudents() {
        return studentMap;
    }

    public void setStudents(Map<Integer, Student> studentMap) {
        this.studentMap = studentMap;
    }

    public void addStudent(Student student){
        studentMap.put(student.getId(), student);
    }

    public Student getStudent(int uid){
        return studentMap.get(uid);
    }


}
