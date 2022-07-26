package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class SubjectInClass {

    private String class_id;
    private Subject subject;


    public SubjectInClass(String class_id, Subject subject) {
        this.class_id = class_id;
        this.subject = subject;
    }

    public SubjectInClass(ResultSet resultSet) throws SQLException {
        this.class_id = resultSet.getString("class_id");
        this.subject = Subject.valueOf(resultSet.getString("SUBJECT"));
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
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
        return Objects.equals(class_id, that.class_id) && subject == that.subject;
    }


    @Override
    public String toString() {
        return "SubjectWithClass{" +
                "class_id='" + class_id + '\'' +
                ", subject=" + subject +
                '}';
    }
}
