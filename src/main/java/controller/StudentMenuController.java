package controller;

import classes.Grade;
import classes.Student;
import classes.Subject;
import javafx.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
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

}
