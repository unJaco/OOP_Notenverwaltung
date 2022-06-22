package dev.app.classes;

public class grade {
    private int grade;
    private user user;
    private subject subject;

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
    public user getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(user user) {
        this.user = user;
    }

    /**
     * @return subject return the subject
     */
    public subject getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(subject subject) {
        this.subject = subject;
    }

    public grade(int grade, user user, subject subject) {
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
