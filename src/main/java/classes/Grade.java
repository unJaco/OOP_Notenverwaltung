package classes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Grade {

    private Integer gradeId;

    private int gradeVal;

    private String gradeBez;

    private Subject subject;

    public Grade(Integer gradeId, int gradeVal, String gradeBez, Subject subject) {
        this.gradeId = gradeId;
        this.gradeVal = gradeVal;
        this.gradeBez = gradeBez;
        this.subject = subject;
    }

    /*
        TODO what if resultset wrong string for subject
     */
    public Grade(ResultSet resultSet) throws SQLException {

        this.gradeId = resultSet.getInt("GRADE_ID");
        this.gradeVal = resultSet.getInt("GRADE_VAL");
        this.gradeBez = resultSet.getString("GRADE_BEZ");
        this.subject = Subject.valueOf(resultSet.getString("SUBJECT"));

    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getGradeVal() {
        return gradeVal;
    }

    public void setGradeVal(int gradeVal) {
        this.gradeVal = gradeVal;
    }

    public String getGradeBez() {
        return gradeBez;
    }

    public void setGradeBez(String gradeBez) {
        this.gradeBez = gradeBez;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "grade_id=" + gradeId +
                ", grade_val=" + gradeVal +
                ", grade_bez='" + gradeBez + '\'' +
                ", subject=" + subject +
                '}';
    }

    /*
     * public boolean deleteGrade(){}
     * 
     * public boolean insertGrade(){}
     * 
     * public boolean changeGrade(){
     * //über delete und change
     * }
     */
}
