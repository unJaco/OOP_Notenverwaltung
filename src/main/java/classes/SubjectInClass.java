package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class SubjectInClass {

    private Integer id;
    private String classId;
    private Subject subject;

    //Object combines a teacher, a subject and the class in which he teaches teh subject
    public SubjectInClass() {
    }

    public SubjectInClass(Integer id, String classId, Subject subject) {
        this.id = id;
        this.classId = classId;
        this.subject = subject;
    }

    public SubjectInClass(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("SIC_ID");
        this.classId = resultSet.getString("CLASS_ID");
        this.subject = Subject.valueOf(resultSet.getString("SUBJECT"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectInClass that = (SubjectInClass) o;
        return Objects.equals(classId, that.classId) && subject == that.subject;
    }


    @Override
    public String toString() {
        return classId + " - " + subject;
    }
}
