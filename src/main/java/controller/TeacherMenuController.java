package controller;

import classes.*;
import javafx.MainApplication;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.security.auth.callback.Callback;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TeacherMenuController implements Initializable {

    static ObservableList<SubjectInClass> subjectInClassesList;

    @FXML
    private TableView<SchoolClass> classView;

    @FXML
    private TableColumn<SchoolClass, Student> nameCol;

    @FXML
    private ChoiceBox<SubjectInClass> classesChoiceBox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Teacher teacher = (Teacher) MainApplication.getUser();
        subjectInClassesList = FXCollections.observableList(teacher.getSubjectsInClasses());

        classesChoiceBox.setItems(subjectInClassesList);

        Callback callback =

        //nameCol.setCellValueFactory(s -> (ObservableValue<Collection<Student>>) s.getValue().getStudents().values());

        classesChoiceBox.setOnAction(actionEvent -> {

            classView.getItems().clear();

            SubjectInClass subjectInClass = classesChoiceBox.getValue();
            Map<String, SchoolClass> schoolClassMap = teacher.getSchoolClassMap();

            classView.getItems().addAll(schoolClassMap.get(subjectInClass.getClassId()));
        });
    }
}
