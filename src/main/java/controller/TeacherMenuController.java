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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.IntegerStringConverter;

import java.awt.Label;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class TeacherMenuController implements Initializable {

    ObservableList<SubjectInClass> subjectInClassesList;

    SubjectInClass selectedSubjectInClass = new SubjectInClass();

    Student selectedStudent = new Student();

    Teacher teacher;

    @FXML
    private TableView<Grade> tableView;

    @FXML
    private TableColumn<Grade, String> bezCol;

    @FXML
    private TableColumn<Grade, Integer> valCol;

    @FXML
    private ChoiceBox<SubjectInClass> classesChoiceBox;

    @FXML
    private ChoiceBox<Student> studentChoiceBox;

    @FXML
    private Label avgLabel;
    @FXML
    private Label errorLabel;
    @FXML
    public TextField passwordTextField;
    @FXML
    public TextField emailTextField;
    @FXML
    public ChoiceBox<String> gradeValChoiceBox;
    @FXML
    public TextField gradeBezTextField;
    private TableColumn<Grade, Integer> valColStudent;
    @FXML
    private javafx.scene.control.Label errorLabelStudent;
    @FXML
    private javafx.scene.control.Label avgLabelStudent;
    @FXML
    public javafx.scene.control.Label errLabelChangePassword;

    private final String[] gradeVal = {"1", "2", "3", "4", "5", "6"};

    private final ObservableList<String> gradeValList = FXCollections.observableList(Arrays.asList(gradeVal));




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        teacher = (Teacher) MainApplication.getUser();

        subjectInClassesList = FXCollections.observableList(teacher.getSubjectsInClasses());

        bezCol.setCellValueFactory(new PropertyValueFactory<>("gradeBez"));
        valCol.setCellValueFactory(new PropertyValueFactory<>("gradeVal"));

        tableView.setEditable(true);

        bezCol.setOnEditCommit(event -> {
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

        valCol.setOnEditCommit(event -> {
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


        classesChoiceBox.setItems(subjectInClassesList);
        gradeValChoiceBox.setItems(gradeValList);
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
                System.out.println("LOL");
                setTableViewItems();
            }
        });

        studentChoiceBox.setOnAction(actionEvent -> {
            selectedStudent = studentChoiceBox.getValue();
            setTableViewItems();
        });
    }

    private void updateSelectedStudentGrades() {
        try {
            selectedStudent.onCreation();
            setTableViewItems();
        } catch (SQLException e) {
            errorLabel.setText("Fehler beim laden der Noten");
        }
    }

    @FXML
    protected void onGradeClick() {
        try {
            String gradeBezeichnug = gradeBezTextField.getText();
            int gradeVal = Integer.parseInt(gradeValChoiceBox.getValue());

            if(!gradeBezeichnug.isBlank()  && selectedStudent != null && selectedSubjectInClass != null){
                Grade grade = new Grade(null, gradeVal, gradeBezeichnug, selectedSubjectInClass.getSubject());
                DBHelper.insertGrade(selectedSubjectInClass.getClassId(), selectedStudent.getId(), grade);
                updateSelectedStudentGrades();
            }
        } catch (Exception e) {
            errorLabelStudent.setText("Eintragen fehlgeschalgen");
        }
    }

    @FXML
    protected void onDeleteClick() {
        try {
            Grade selectedGrade = tableView.getSelectionModel().getSelectedItem();
            tableView.getItems().remove(selectedGrade);
            teacher.deleteGrade(selectedGrade.getGradeId(), selectedStudent.getId());
            updateSelectedStudentGrades();
        } catch (SQLException e) {
            errorLabel.setText("Löschen fehlgeschlagen");
        }
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

    private void onDialogClose(WindowEvent windowEvent) {
        /*
            TODO what if in dialog another student is selected
         */
        try {
            selectedStudent.onCreation();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setTableViewItems();
    }

    private void setTableViewItems() {

        tableView.getItems().clear();
        avgLabelStudent.setText("");
        if (selectedStudent != null) {
            SubjectInClass subjectInClass = selectedSubjectInClass;

            List<Grade> grades = selectedStudent.displayGrades(subjectInClass.getSubject());

            tableView.getItems().addAll(grades);
            avgLabelStudent.setText(String.valueOf(selectedStudent.calcAverage(grades)));
        }
    }

    public void onLogOutButtonClick() throws IOException {
        MainApplication.changeScene("login-view.fxml","Bitte geben Sie ihre Login Daten ein!");
    }

    public void onChangePasswordButtonClick() throws SQLException, IOException {
        try {
            MainApplication.changePassword(emailTextField.getText(), passwordTextField.getText());
            MainApplication.changeScene("login-view.fxml", "Bitte geben Sie ihre Login Daten ein!");
        }catch (Exception e){
            errLabelChangePassword.setText("Passwort ändern fehlgeschlagen");
        }
    }
}
