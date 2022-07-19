package classes;

import java.lang.reflect.Array;

public class Classroom {

    private int classroomNumber;
    private Array students;

    /**
     * @return int return the classroomNumber
     */
    public int getClassroomNumber() {
        return classroomNumber;
    }

    /**
     * @param classroomNumber the classroomNumber to set
     */
    public void setClassroomNumber(int classroomNumber) {
        this.classroomNumber = classroomNumber;
    }

    /**
     * @return Array return the students
     */
    public Array getStudents() {
        return students;
    }

    /**
     * @param students the students to set
     */
    public void setStudents(Array students) {
        this.students = students;
    }

    public Classroom(int classroomNumber, Array students) {
        this.classroomNumber = classroomNumber;
        this.students = students;
    }

}
