package classes;
import java.lang.reflect.Array;

public class Teacher extends User {

    private Subject[] subjects;

    /**
     * @return Array return the subjects
     */
    public Subject[] getSubjects() {
        return subjects;
    }

    /**
     * @param subjects the subjects to set
     */
    public void setSubjects(Subject[] subjects) {
        this.subjects = subjects;
    }

    public Teacher(int id, String firstname, String lastname, String email,Role role, Subject[] subjects) {
        super(id, firstname, lastname, email, role);
        this.subjects = subjects;
    }
}
