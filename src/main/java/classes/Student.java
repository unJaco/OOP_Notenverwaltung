package classes;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import db.*;
import javafx.MainApplication;

public class Student extends User {

    ArrayList<Grade> grades;

    DecimalFormat df = new DecimalFormat("#.##");

    public Student() {
    }

    //when creating User manually there is no Id - so Id is manually set to null therefore the Type needs to be Integer not int
    public Student(Integer id, String firstname, String lastname, Role role) {
        super(id, firstname, lastname, role);
    }

    public Student(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("UID"), resultSet.getString("VORNAME"), resultSet.getString("NAME"), Role.valueOf(resultSet.getString("ROLE")));
    }

    @Override
    public void onCreation() throws SQLException {
        String sql = "SELECT * FROM GRADES WHERE UID = '" + this.getId() + "';";
        ResultSet rs = DBHelper.executeSqlSelectStatement(sql);

        this.grades = new ArrayList<>();

        if (rs.isClosed()) {
            return;
        }
        while (rs.next()) {
            grades.add(new Grade(rs));
        }
    }

    //displays the Grades on the scene
    public List<Grade> displayGrades(Subject subject) {
        return grades.stream().filter(grade -> grade.getSubject() == subject).toList();
    }

    //calculates the average of the notes
    public double calcAverage(List<Grade> grades) {
        return (double) grades.stream().mapToInt(Grade::getGradeVal).sum() / grades.size();
    }

    public List<Grade> showAllGrades() {
        return grades;
    }

    @Override
    public void switchScene() throws IOException {
        MainApplication.changeScene("menu-view-student.fxml", "Deine Noten");
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
