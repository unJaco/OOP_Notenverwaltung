package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class SubjectInClass {

    private String classId;
    private Subject subject;


    public SubjectInClass(String classId, Subject subject) {
        this.classId = classId;
        this.subject = subject;
    }

    public SubjectInClass(ResultSet resultSet) throws SQLException {
        this.classId = resultSet.getString("class_id");
        this.subject = Subject.valueOf(resultSet.getString("SUBJECT"));
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
