package controller;

import classes.*;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class AdminMenuController implements Initializable {


    private Admin admin;

    private SubjectInClass selectedSubjectInClass = new SubjectInClass();

    private Student selectedStudent = new Student();

    private Teacher selectedTeacher = new Teacher();

    private final String[] gradeVal = {"1", "2", "3", "4", "5", "6"};

    private final ObservableList<String> gradeValList = FXCollections.observableList(Arrays.asList(gradeVal));

    private final ObservableList<Subject> subjectList = FXCollections.observableList(Arrays.stream(Subject.values()).toList());

    private final ObservableList<Role> roleList = FXCollections.observableList(Arrays.stream(Role.values()).toList());

    @FXML
    private ChoiceBox<SubjectInClass> classesChoiceBox;
    @FXML
    private ChoiceBox<Student> studentChoiceBox;
    @FXML
    private TableView<Grade> tableViewStudent;
    @FXML
    private TableColumn<Grade, String> bezColStudent;
    @FXML
    private TableColumn<Grade, Integer> valColStudent;
    @FXML
    private Label errorLabelStudent;
    @FXML
    private Label avgLabelStudent;
    @FXML
    public ChoiceBox<String> gradeValChoiceBox;
    @FXML
    public TextField gradeBezTextField;
    @FXML
    private ChoiceBox<Teacher> teacherChoiceBox;
    @FXML
    private TableView<SubjectInClass> tableViewTeacher;
    @FXML
    private TableColumn<SubjectInClass, String> classColTeacher;
    @FXML
    private TableColumn<SubjectInClass, Subject> subjectColTeacher;
    @FXML
    private Label errLabelTeacher;
    @FXML
    private TextField classTextField;
    @FXML
    private ChoiceBox<Subject> subjectChoiceBox;
    @FXML
    private TextField vornameTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField insertClassTextField;
    @FXML
    private ChoiceBox<Role> roleChoiceBox;
    @FXML
    private Label errLabelUser;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField emailTextField2;
    @FXML
    public Label errLabelChangePassword;
    @FXML
    public Label errLabelLogout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        admin = (Admin) MainApplication.getUser();

        setUpStudentTab();
        setUpTeacherTab();
        setUpUserTab();

    }
    //sets up the tap of the student
    //mostly fills the Table with the choosen values
    private void setUpStudentTab() {

        gradeValChoiceBox.setItems(gradeValList);

        classesChoiceBox.getItems().clear();

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
            } catch (Exception e) {
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
            } catch (Exception e) {
                errorLabelStudent.setText("Update fehlgeschlagen!");
            }
        });
        classesChoiceBox.setOnAction(actionEvent -> {

            //get the selected SubjectInClass (subject In Combination With a Class)
            SubjectInClass newSelectedSubjectInClass = classesChoiceBox.getValue();
            if (newSelectedSubjectInClass != null) {
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
        if (selectedStudent != null) {
            List<Grade> grades = selectedStudent.displayGrades(selectedSubjectInClass.getSubject());
            tableViewStudent.getItems().addAll(grades);
            avgLabelStudent.setText(String.valueOf(selectedStudent.calcAverage(grades)));
        }
    }

    private void updateSelectedStudentGrades() {
        try {
            selectedStudent.onCreation();
            setStudentTableViewItems();
        } catch (Exception e) {
            errorLabelStudent.setText("Fehler beim laden der Noten");
        }
    }

    //sets up the teacher tab
    private void setUpTeacherTab() {

        teacherChoiceBox.setItems(FXCollections.observableList(admin.getTeachersList()));

        classColTeacher.setCellValueFactory(new PropertyValueFactory<>("classId"));
        subjectColTeacher.setCellValueFactory(new PropertyValueFactory<>("subject"));
        teacherChoiceBox.setOnAction(actionEvent -> {
            selectedTeacher = teacherChoiceBox.getValue();
            setTeacherTableViewItems();
        });
        subjectChoiceBox.setItems(subjectList);
    }

    private void setTeacherTableViewItems() {

        tableViewTeacher.getItems().clear();

        if (selectedTeacher != null) {
            List<SubjectInClass> subjectInClasses = selectedTeacher.getSubjectsInClasses();
            tableViewTeacher.getItems().addAll(subjectInClasses);
        }

    }

    //sets up User Tab
    private void setUpUserTab() {
        roleChoiceBox.setItems(roleList);
        insertClassTextField.setVisible(false);
        roleChoiceBox.setOnAction(actionEvent -> insertClassTextField.setVisible(Objects.equals(roleChoiceBox.getValue(), Role.STUDENT)));
    }

    //onClick Handler for Grade Delete Button
    @FXML
    protected void onGradeDeleteClick() {
        try {
            Grade selectedGrade = tableViewStudent.getSelectionModel().getSelectedItem();
            tableViewStudent.getItems().remove(selectedGrade);
            selectedTeacher.deleteGrade(selectedGrade.getGradeId(), selectedStudent.getId());
            updateSelectedStudentGrades();
        } catch (Exception e) {
            errorLabelStudent.setText("Löschen fehlgeschlagen");
        }
    }

    //onClick Handler for delete SubjectInClass Button
    @FXML
    protected void deleteSIC() {
        try {
            SubjectInClass subjectInClass = tableViewTeacher.getSelectionModel().getSelectedItem();
            DBHelper.deleteTeacherEntry(subjectInClass.getId(), selectedTeacher.getId());
            tableViewTeacher.getItems().remove(subjectInClass);
        } catch (Exception e) {
            errLabelTeacher.setText("Löschen fehlgeschlagen");
        }
    }

    //onClick Handler for insert of Grade Button
    @FXML
    protected void onGradeClick() {
        try {

            String gradeBezeichnug = gradeBezTextField.getText();
            int gradeVal = Integer.parseInt(gradeValChoiceBox.getValue());

            if (!gradeBezeichnug.isBlank() && selectedStudent != null && selectedSubjectInClass != null) {
                Grade grade = new Grade(null, gradeVal, gradeBezeichnug, selectedSubjectInClass.getSubject());
                DBHelper.insertGrade(selectedSubjectInClass.getClassId(), selectedStudent.getId(), grade);
                updateSelectedStudentGrades();
            }
        } catch (Exception e) {
            errorLabelStudent.setText("Eintragen fehlgeschalgen");
        }
    }

    //onClick Handler for insert of SubjectInClass Button
    @FXML
    protected void addSIC() {
        try {
            admin.addSubjectToTeacher(selectedTeacher.getId(), subjectChoiceBox.getValue(), classTextField.getText());
            selectedTeacher.onCreation();
            admin.onCreation();
            setTeacherTableViewItems();
            setUpStudentTab();
        } catch (Exception e) {
            errLabelTeacher.setText("Hinzufügen fehlgeschlagen");
        }
    }

    //onClick Handler for insert User Button
    @FXML
    protected void insertUserButton() {

        Role role = roleChoiceBox.getValue();
        String vorname = vornameTextField.getText();
        String nachname = nameTextField.getText();
        String email = emailTextField.getText();
        String classId = null;

        if (!vorname.isBlank() && !nachname.isBlank() && !email.isBlank() && role != null) {
            try {
                if (role == Role.STUDENT) {
                    classId = insertClassTextField.getText();
                }
                admin.createUser(vorname, nachname, email, role, classId);

                try {
                    admin.onCreation();
                } catch (Exception e) {
                    errLabelUser.setText("Laden des neuen Users fehlgeschlagen. Probieren Sie das System neu zu starten");
                }
                if (role == Role.STUDENT) {
                    setUpStudentTab();
                } else if (role == Role.TEACHER) {
                    setUpTeacherTab();
                }

            } catch (SQLException e) {
                if (e.getErrorCode() == 19) {
                    errLabelUser.setText("Email existiert bereits");
                } else {
                    errLabelUser.setText("Erstellen fehlgeschlagen");
                }
            }
        } else {
            errLabelUser.setText("Alle Felder müssen ausgefüllt sein");
        }


        vornameTextField.clear();
        nameTextField.clear();
        emailTextField.clear();
        insertClassTextField.clear();

    }

    //onClick Handler for delete Student Button
    @FXML
    protected void deleteStudent() {
        try {
            admin.deleteUser(selectedStudent.getId());
            selectedStudent = null;
            admin.onCreation();
            setUpStudentTab();
        } catch (Exception e) {
            System.out.println(e);
            errorLabelStudent.setText("Löschen fehlgeschlagen");
        }
    }

    //onClick Handler for delete Teacher Button
    @FXML
    protected void deleteTeacher() {
        try {
            admin.deleteUser(selectedTeacher.getId());
            selectedTeacher = null;
            admin.onCreation();
            setUpTeacherTab();
        } catch (Exception e) {
            errLabelTeacher.setText("Löschen fehlgeschlagen");
        }
    }

    //onClick Handler for logOut Button
    public void onLogOutButtonClick() throws IOException {
        try {
            MainApplication.changeScene("login-view.fxml", "Bitte geben Sie ihre Login Daten ein!");
        } catch (Exception e) {
            errLabelLogout.setText("Ausloggen fehlgeschlagen");
        }
    }

    // onClick Handler for changePassword Button
    public void onChangePasswordButtonClick() throws IOException, Exception {
        try {
            MainApplication.changePassword(emailTextField2.getText(), passwordTextField.getText());
            MainApplication.changeScene("login-view.fxml", "Bitte geben Sie ihre Login Daten ein!");
        } catch (Exception e) {
            errLabelChangePassword.setText("Passwort ändern fehlgeschlagen");
        }

    }
}
