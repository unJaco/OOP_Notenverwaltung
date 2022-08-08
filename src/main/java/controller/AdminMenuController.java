package controller;

import classes.*;
import db.DBHelper;
import javafx.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {


    private Admin admin;

    ObservableList<SubjectInClass> subjectInClassesList;

    SubjectInClass selectedSubjectInClass = new SubjectInClass();

    Student selectedStudent = new Student();

    @FXML
    public ChoiceBox<SubjectInClass> classesChoiceBox;
    @FXML
    public ChoiceBox<Student> studentChoiceBox;
    @FXML
    public TableView<Grade> tableViewTeacher;
    @FXML
    public TableColumn<Grade, String> bezColTeacher;
    @FXML
    public TableColumn<Grade, Integer> valColTeacher;
    @FXML
    public Label errorLabel;
    @FXML
    public Label avgLabelTeacher;
    @FXML
    public ChoiceBox<SubjectInClass> subjectChoiceBox;
    @FXML
    public TableView<Grade> tableViewStudent;
    @FXML
    public TableColumn<Grade, String> bezColStudent;
    @FXML
    public TableColumn<Grade, Integer> valColStudent;
    @FXML
    public Label avgLabelStudent;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        admin = (Admin) MainApplication.getUser();

        classesChoiceBox.setItems(FXCollections.observableList(admin.getAllSubjectsInAllClasses()));

        bezColTeacher.setCellValueFactory(new PropertyValueFactory<>("gradeBez"));
        valColTeacher.setCellValueFactory(new PropertyValueFactory<>("gradeVal"));

        bezColTeacher.setCellFactory(TextFieldTableCell.forTableColumn());
        valColTeacher.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        tableViewTeacher.setEditable(true);

        bezColTeacher.setOnEditCommit(event -> {
            Grade gradeToUpdate = event.getRowValue();
            String newBezVal = event.getNewValue();
            gradeToUpdate.setGradeBez(newBezVal);
            try {
                DBHelper.updateGradeBez(gradeToUpdate);
                updateSelectedStudentGrades();
            } catch (SQLException e) {
                errorLabel.setText("Update fehlgeschlagen!");
            }

        });

        valColTeacher.setOnEditCommit(event -> {
            Grade gradeToUpdate = event.getRowValue();
            int newGradeVal = event.getNewValue();
            gradeToUpdate.setGradeVal(newGradeVal);
            try {
                DBHelper.updateGradeVal(gradeToUpdate);
                updateSelectedStudentGrades();
            } catch (SQLException e) {
                errorLabel.setText("Update fehlgeschlagen!");
            }
        });

        classesChoiceBox.setOnAction(actionEvent -> {

            //get the selected SubjectInClass (subject In Combination With a Class)
            SubjectInClass newSelectedSubjectInClass = classesChoiceBox.getValue();

            //get the classId / identifier
            String classId = newSelectedSubjectInClass.getClassId();

            //get the selected SchoolClass by Id


            if (!newSelectedSubjectInClass.getClassId().equals(selectedSubjectInClass.getClassId())) {

                selectedSubjectInClass = newSelectedSubjectInClass;

                //create observableList for choice-box with the student from the selected SchoolClass

                Teacher teacher = admin.getTeachersList().stream().filter(teacher1 -> teacher1.getSubjectsInClasses().contains(classesChoiceBox.getValue())).toList().get(0);
                var schoolClass = teacher.getSchoolClassMap().get(classId);

                var observableList = FXCollections.observableList(schoolClass.getStudents().values().stream().toList());

                studentChoiceBox.getSelectionModel().clearSelection();
                studentChoiceBox.setItems(observableList);

            } else {

                selectedSubjectInClass = newSelectedSubjectInClass;
                setTableViewItems();
            }
        });

        studentChoiceBox.setOnAction(actionEvent -> {
            selectedStudent = studentChoiceBox.getValue();
            setTableViewItems();
        });

    }

    private void setTableViewItems() {

        tableViewTeacher.getItems().clear();
        SubjectInClass subjectInClass = selectedSubjectInClass;

        List<Grade> grades = selectedStudent.displayGrades(subjectInClass.getSubject());
        tableViewTeacher.getItems().addAll(grades);
        avgLabelTeacher.setText(String.valueOf(selectedStudent.calcAverage(grades)));
    }

    private void updateSelectedStudentGrades() {
        try {
            selectedStudent.onCreation();
            setTableViewItems();
        } catch (SQLException e) {
            errorLabel.setText("Fehler beim laden der Noten");
        }
    }

    public void onDeleteClick(ActionEvent actionEvent) {
    }

    public void onGradeClick(ActionEvent actionEvent) {
    }

    public void onAllGradeClick(ActionEvent actionEvent) {
    }
}
