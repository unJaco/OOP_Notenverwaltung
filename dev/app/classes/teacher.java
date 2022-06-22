package dev.app.classes;
import java.lang.reflect.Array;

public class teacher extends user {

    private Array subjects;

    /**
     * @return Array return the subjects
     */
    public Array getSubjects() {
        return subjects;
    }

    /**
     * @param subjects the subjects to set
     */
    public void setSubjects(Array subjects) {
        this.subjects = subjects;
    }

    public teacher(int id, String firstname, String lastname, Array subjects) {
        super(id, firstname, lastname);
        this.subjects = subjects;
    }
}
