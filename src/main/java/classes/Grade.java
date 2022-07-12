package classes;

public class Grade {
    private int grade;
    private User user;
    private SubjectOld subject;

    /**
     * @return int return the grade
     */
    public int getGrade() {
        return grade;
    }

    /**
     * @param grade the grade to set
     */
    public void setGrade(int grade) {
        this.grade = grade;
    }

    /**
     * @return user return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return subject return the subject
     */
    public SubjectOld getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(SubjectOld subject) {
        this.subject = subject;
    }

    public Grade(int grade, User user, SubjectOld subject) {
        this.grade = grade;
        this.user = user;
        this.subject = subject;
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
