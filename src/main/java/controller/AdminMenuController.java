package controller;

import classes.*;
import db.DBHelper;
import javafx.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {


    private Admin admin;

    ObservableList<SubjectInClass> subjectInClassesList;

    SubjectInClass selectedSubjectInClass = new SubjectInClass();

    Student selectedStudent = new Student();

    Teacher selectedTeacher = new Teacher();

    ObservableList<Subject> subjectList = FXCollections.observableList(Arrays.stream(Subject.values()).toList());
    @FXML
    public ChoiceBox<SubjectInClass> classesChoiceBox;
    @FXML
    public ChoiceBox<Student> studentChoiceBox;
    @FXML
    public TableView<Grade> tableViewStudent;
    @FXML
    public TableColumn<Grade, String> bezColStudent;
    @FXML
    public TableColumn<Grade, Integer> valColStudent;
    @FXML
    public Label errorLabelStudent;
    @FXML
    public Label avgLabelStudent;

    @FXML
    public ChoiceBox<Teacher> teacherChoiceBox;
    @FXML
    public TableView<SubjectInClass> tableViewTeacher;
    @FXML
    public TableColumn<SubjectInClass, String> classColTeacher;
    @FXML
    public TableColumn<SubjectInClass, Subject> subjectColTeacher;
    @FXML
    public Label errLabelTeacher;
    @FXML
    public TextField classTextField;
    @FXML
    public ChoiceBox<Subject> subjectChoiceBox;
    @FXML
    public TextField passwordTextField;
    @FXML
    public TextField emailTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        admin = (Admin) MainApplication.getUser();

        setUpStudentTab();
        setUpTeacherTab();


    }
    private void setUpStudentTab(){

        classesChoiceBox.setItems(FXCollections.observableList(admin.getAllSubjectsInAllClasses()));

        bezColStudent.setCellValueFactory(new PropertyValueFactory<>("gradeBez"));
        valColStudent.setCellValueFactory(new PropertyValueFactory<>("gradeVal"));

        tableViewStudent.setEditable(true);

        bezColStudent.setOnEditCommit(event -> {
            Grade gradeToUpdate = event.getRowValue();
            String newBezVal = event.getNewValue();
            gradeToUpdate.setGradeBez(newBezVal);
            try {
                DBHelper.updateGradeBez(gradeToUpdate);
                updateSelectedStudentGrades();
            } catch (SQLException e) {
                errorLabelStudent.setText("Update fehlgeschlagen!");
            }

        });

        valColStudent.setOnEditCommit(event -> {
            Grade gradeToUpdate = event.getRowValue();
            int newGradeVal = event.getNewValue();
            gradeToUpdate.setGradeVal(newGradeVal);
            try {
                DBHelper.updateGradeVal(gradeToUpdate);
                updateSelectedStudentGrades();
            } catch (SQLException e) {
                errorLabelStudent.setText("Update fehlgeschlagen!");
            }
        });

        classesChoiceBox.setOnAction(actionEvent -> {

            //get the selected SubjectInClass (subject In Combination With a Class)
            SubjectInClass newSelectedSubjectInClass = classesChoiceBox.getValue();

            //get the classId / identifier
            String classId = newSelectedSubjectInClass.getClassId();

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
                setStudentTableViewItems();
            }
        });

        studentChoiceBox.setOnAction(actionEvent -> {
            selectedStudent = studentChoiceBox.getValue();
            setStudentTableViewItems();
        });
    }

    private void setStudentTableViewItems() {

        tableViewStudent.getItems().clear();
        avgLabelStudent.setText("");
        if(selectedStudent!= null){
            List<Grade> grades = selectedStudent.displayGrades(selectedSubjectInClass.getSubject());
            tableViewStudent.getItems().addAll(grades);
            avgLabelStudent.setText(String.valueOf(selectedStudent.calcAverage(grades)));
        }

    }

    private void updateSelectedStudentGrades() {
        try {
            selectedStudent.onCreation();
            setStudentTableViewItems();
        } catch (SQLException e) {
            errorLabelStudent.setText("Fehler beim laden der Noten");
        }
    }

    private void setUpTeacherTab(){

        teacherChoiceBox.setItems(FXCollections.observableList(admin.getTeachersList()));

        classColTeacher.setCellValueFactory(new PropertyValueFactory<>("classId"));
        subjectColTeacher.setCellValueFactory(new PropertyValueFactory<>("subject"));

        teacherChoiceBox.setOnAction(actionEvent -> {

            selectedTeacher = teacherChoiceBox.getValue();
            setTeacherTableViewItems();

        });

        subjectChoiceBox.setItems(subjectList);
    }

    private void setTeacherTableViewItems(){

        tableViewTeacher.getItems().clear();

        List<SubjectInClass> subjectInClasses = selectedTeacher.getSubjectsInClasses();
        tableViewTeacher.getItems().addAll(subjectInClasses);

    }

    @FXML
    protected void onGradeDeleteClick() {
        try{
            Grade selectedGrade = tableViewStudent.getSelectionModel().getSelectedItem();
            tableViewStudent.getItems().remove(selectedGrade);
            DBHelper.deleteGrade(selectedGrade.getGradeId(), selectedStudent.getId());
            updateSelectedStudentGrades();
        } catch (SQLException e){
            errorLabelStudent.setText("Löschen fehlgeschlagen");
        }
    }
    @FXML
    protected void onSubjectInClassDeleteClick() {
        try {
            SubjectInClass subjectInClass = tableViewTeacher.getSelectionModel().getSelectedItem();
            DBHelper.deleteTeacherEntry(subjectInClass.getId(), selectedTeacher.getId());
            tableViewTeacher.getItems().remove(subjectInClass);

        } catch (SQLException e) {
            errLabelTeacher.setText("Löschen fehlgeschlagen");
        }
    }
    @FXML
    protected void onGradeClick() {
        try {
            openDialog();
        } catch (IOException e) {
            errorLabelStudent.setText("Öffnen fehlgeschlagen");
        }
    }

    @FXML
    protected void addSIC(){
        try {
            DBHelper.addTeacherWithSubjectToClass(selectedTeacher.getId(), classTextField.getText(), subjectChoiceBox.getValue());
            selectedTeacher.onCreation();
            admin.onCreation();
            setTeacherTableViewItems();
        } catch (SQLException e) {
            errLabelTeacher.setText("Hinzufügen fehlgeschlagen");
        }
    }

    @FXML
    protected void deleteSIC(){


    }

    private void openDialog() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("input-grade-view.fxml"));
        Parent parent = fxmlLoader.load();
        GradeDialogController controller = fxmlLoader.getController();
        controller.initData(studentChoiceBox.getValue(), classesChoiceBox.getValue());
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setTitle("Geben Sie eine Note ein");
        stage.show();

        stage.getScene().getWindow().addEventFilter(WindowEvent.ANY, this::onDialogClose);

    }

    private void onDialogClose(WindowEvent windowEvent){
        /*
            TODO what if in dialog another student is selected
         */
        try {
            selectedStudent.onCreation();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setStudentTableViewItems();
    }

    public void onLogOutButtonClick() throws IOException {
        MainApplication.changeScene("login-view.fxml", "Bitte geben Sie ihre Login Daten ein!");
    }

    public void onAllGradeClick(ActionEvent event) {
    }

    public void onDeleteClick(ActionEvent event) {
    }

    public void onChangePasswordButtonClick() throws IOException, SQLException {

            MainApplication.changePassword(emailTextField.getText(),passwordTextField.getText());
            MainApplication.changeScene("login-view.fxml", "Bitte geben Sie ihre Login Daten ein!");


    }
}
