package classes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Grade {

    private Integer grade_id;

    private int grade_val;

    private String grade_bez;

    private Subject subject;

    public Grade(Integer grade_id, int grade_val, String grade_bez, Subject subject) {
        this.grade_id = grade_id;
        this.grade_val = grade_val;
        this.grade_bez = grade_bez;
        this.subject = subject;
    }

    /*
        TODO what if resultset wrong string for subject
     */
    public Grade(ResultSet resultSet) throws SQLException {

        this.grade_id = resultSet.getInt("GRADE_ID");
        this.grade_val = resultSet.getInt("GRADE_VAL");
        this.grade_bez = resultSet.getString("GRADE_BEZ");
        this.subject = Subject.valueOf(resultSet.getString("SUBJECT"));

    }

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public int getGrade_val() {
        return grade_val;
    }

    public void setGrade_val(int grade_val) {
        this.grade_val = grade_val;
    }

    public String getGrade_bez() {
        return grade_bez;
    }

    public void setGrade_bez(String grade_bez) {
        this.grade_bez = grade_bez;
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
                "grade_id=" + grade_id +
                ", grade_val=" + grade_val +
                ", grade_bez='" + grade_bez + '\'' +
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
