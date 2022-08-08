package controller;

import classes.Grade;
import classes.Student;
import classes.SubjectInClass;
import classes.Teacher;
import db.DBHelper;
import javafx.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GradeDialogController implements Initializable {

    static SubjectInClass selectedSubjectInClass = new SubjectInClass();

    static Student selectedStudent = new Student();

    @FXML
    private ChoiceBox<SubjectInClass> classesChoiceBox;

    @FXML
    private ChoiceBox<Student> studentChoiceBox;
    @FXML
    private TextField bezField;

    @FXML
    private TextField valField;

    @FXML
    private Label insertLabel;

    @FXML
    private Button insertButton;

    public GradeDialogController(){}

    @FXML
    protected void onInsertButtonClick() {

        if (bezField.getText().isBlank() || valField.getText().isBlank()){
            insertLabel.setText("Jedes Feld muss ausgefüllt sein");
            return;
        }
        try {
            int i = Integer.parseInt(valField.getText());

            if(i < 1 || i > 6){
                insertLabel.setText("Note muss zwischen 1 und 6 liegen");
                return;
            }
        } catch (NumberFormatException e){
            insertLabel.setText("Note muss eine Zahl sein");
            return;
        }

        int gradeVal = Integer.parseInt(valField.getText());

        Grade gradeToInsert = new Grade(null, gradeVal, bezField.getText(),selectedSubjectInClass.getSubject());
        try{
            DBHelper.insertGrade(selectedSubjectInClass.getClassId(), selectedStudent.getId(), gradeToInsert);
            Stage stage = (Stage) insertButton.getScene().getWindow();
            stage.close();

        } catch (SQLException e){
            insertLabel.setText("Es gab ein Fehler beim eintragen der Note");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Teacher teacher = (Teacher) MainApplication.getUser();

        ObservableList<SubjectInClass> subjectInClassesList = FXCollections.observableList(teacher.getSubjectsInClasses());

        classesChoiceBox.setItems(subjectInClassesList);

        classesChoiceBox.setOnAction(actionEvent -> {

            //get the selected SubjectInClass (subject In Combination With a Class)
            SubjectInClass newSelectedSubjectInClass = classesChoiceBox.getValue();

            //get the classId / identifier
            String classId = newSelectedSubjectInClass.getClassId();

            //get the selected SchoolClass by Id
            var schoolClass = teacher.getSchoolClassMap().get(classId);


            if (!newSelectedSubjectInClass.getClassId().equals(selectedSubjectInClass.getClassId())) {

                selectedSubjectInClass = newSelectedSubjectInClass;

                //create observableList for choice-box with the student from the selected SchoolClass
                var observableList = FXCollections.observableList(schoolClass.getStudents().values().stream().toList());

                studentChoiceBox.getSelectionModel().clearSelection();
                studentChoiceBox.setItems(observableList);

            } else {

                selectedSubjectInClass = newSelectedSubjectInClass;
            }
        });

        studentChoiceBox.setOnAction(actionEvent -> {
            selectedStudent = studentChoiceBox.getValue();
        });
    }

    public void initData(Student student, SubjectInClass subjectInClass){
        studentChoiceBox.getSelectionModel().select(student);
        classesChoiceBox.getSelectionModel().select(subjectInClass);
    }

}
