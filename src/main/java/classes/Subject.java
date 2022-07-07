package classes;

public class Subject {

    private String subjName;

    /**
     * @return String return the subjName
     */
    public String getSubjName() {
        return subjName;
    }

    /**
     * @param subjName the subjName to set
     */
    public void setSubjName(String subjName) {
        this.subjName = subjName;
    }

    public Subject(String subjName) {
        this.subjName = subjName;
    }

    
}
