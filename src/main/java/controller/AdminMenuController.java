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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {


    private Admin admin;

    private SubjectInClass selectedSubjectInClass = new SubjectInClass();

    private Student selectedStudent = new Student();

    private Teacher selectedTeacher = new Teacher();

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        admin = (Admin) MainApplication.getUser();

        setUpStudentTab();
        setUpTeacherTab();
        setUpUserTab();

    }

    private void setUpStudentTab() {

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
        } catch (SQLException e) {
            errorLabelStudent.setText("Fehler beim laden der Noten");
        }
    }

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

        List<SubjectInClass> subjectInClasses = selectedTeacher.getSubjectsInClasses();
        tableViewTeacher.getItems().addAll(subjectInClasses);

    }

    private void setUpUserTab() {
        roleChoiceBox.setItems(roleList);
        insertClassTextField.setVisible(false);
        roleChoiceBox.setOnAction(actionEvent -> insertClassTextField.setVisible(Objects.equals(roleChoiceBox.getValue(), Role.STUDENT)));
    }

    @FXML
    protected void onGradeDeleteClick() {
        try {
            Grade selectedGrade = tableViewStudent.getSelectionModel().getSelectedItem();
            tableViewStudent.getItems().remove(selectedGrade);
            selectedTeacher.deleteGrade(selectedGrade.getGradeId(), selectedStudent.getId());
            updateSelectedStudentGrades();
        } catch (SQLException e) {
            errorLabelStudent.setText("Löschen fehlgeschlagen");
        }
    }

    @FXML
    protected void deleteSIC() {
        try {
            SubjectInClass subjectInClass = tableViewTeacher.getSelectionModel().getSelectedItem();
            DBHelper.deleteTeacherEntry(subjectInClass.getId(), selectedTeacher.getId());
            tableViewTeacher.getItems().remove(subjectInClass);
        } catch (SQLException e) {
            errLabelTeacher.setText("Löschen fehlgeschlagen");
        }
    }

    @FXML
    protected void onGradeClick(){
        try {
            openDialog();
        } catch (IOException e) {
            errorLabelStudent.setText("Öffnen fehlgeschlagen");
        }
    }

    @FXML
    protected void addSIC() {
        try {
            admin.addSubjectToTeacher(selectedTeacher.getId(), subjectChoiceBox.getValue(),classTextField.getText());
            selectedTeacher.onCreation();
            admin.onCreation();
            setTeacherTableViewItems();
            setUpStudentTab();
        } catch (SQLException e) {
            errLabelTeacher.setText("Hinzufügen fehlgeschlagen");
        }
    }

    @FXML
    protected void insertUserButton() {

        Role role = roleChoiceBox.getValue();
        String vorname = vornameTextField.getText();
        String nachname = nameTextField.getText();
        String email = emailTextField.getText();
        String classId = null;

        if (!vorname.isBlank() && !nachname.isBlank() && !email.isBlank() && role != null) {
            try {
                if(role == Role.STUDENT){
                    classId = insertClassTextField.getText();
                }
                admin.createUser(vorname,nachname, email, role, classId);
            } catch (SQLException e) {
                errLabelUser.setText("Erstellen fehlgeschlagen");
            }
        } else {
            errLabelUser.setText("Alle Felder müssen ausgefüllt sein");
        }

        vornameTextField.clear();
        nameTextField.clear();
        emailTextField.clear();
        insertClassTextField.clear();

    }

    @FXML
    protected void deleteStudent(){
        try {
            admin.deleteUser(selectedStudent.getId());
        } catch (SQLException e) {
            errorLabelStudent.setText("Löschen fehlgeschlagen");
        }
    }

    @FXML
    protected void deleteTeacher(){
        try {
            admin.deleteUser(selectedTeacher.getId());
        } catch (SQLException e) {
            errLabelTeacher.setText("Löschen fehlgeschlagen");
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

    public void onChangePasswordButtonClick() throws IOException, SQLException {

        MainApplication.changePassword(emailTextField2.getText(),passwordTextField.getText());
        MainApplication.changeScene("login-view.fxml", "Bitte geben Sie ihre Login Daten ein!");


    }
}
