package controller;

import classes.Grade;
import classes.Student;
import classes.Subject;
import javafx.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class StudentMenuController implements Initializable {


    ObservableList<Subject> subjectList = FXCollections.observableList(Arrays.stream(Subject.values()).toList());
    @FXML
    private ChoiceBox<Subject> subjectChoiceBox;

    @FXML
    private TableView<Grade> tableView;

    @FXML
    private TableColumn<Grade, String> bezCol;

    @FXML
    private TableColumn<Grade, Integer> valCol;

    @FXML
    private Label avgLabel;

    @FXML
    public TextField passwordTextField;
    @FXML
    public TextField emailTextField;
    @FXML
    public Label errLabelChangePassword;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Please notice that many functions will the same as in AdminMenuController and not be commented here
    // Please go to AdminMenuController for more detailed comments

    @Override
    public void initialize(URL url, ResourceBundle resources) {

        subjectChoiceBox.setItems(subjectList);

        Student student = (Student) MainApplication.getUser();

        bezCol.setCellValueFactory(new PropertyValueFactory<>("gradeBez"));
        valCol.setCellValueFactory(new PropertyValueFactory<>("gradeVal"));

        subjectChoiceBox.setOnAction(actionEvent -> {

            tableView.getItems().clear();
            Subject selectedSubject = subjectChoiceBox.getValue();
            List<Grade> grades = student.displayGrades(selectedSubject);
            tableView.getItems().addAll(grades);
            avgLabel.setText(String.valueOf(student.calcAverage(grades)));

        });


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
