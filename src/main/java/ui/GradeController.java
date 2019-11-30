package ui;

import domain.Grade;
import domain.Homework;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import repository.RepositoryException;
import serviceManager.ServiceManager;
import validation.ValidationException;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GradeController implements Initializable {
    public TableView<Grade> gradeTable;
    public Button clearButton;
    public Button addButton;
    public TableColumn gradeTableHomework;
    public TableColumn gradeTableStudent;
    public TableColumn gradeTableGivenGrade;
    public TableColumn gradeTableProfessor;
    public TableColumn gradeTableHandOverDate;
    public TableColumn gradeTableFeedback;
    public TableColumn gradeTableUpdate;
    public TableColumn gradeTableDelete;
    public ComboBox HomeworkId;
    public ComboBox searchHomeworkId;
    public TextField searchStudentName;
    public TextField studentName;
    public TextField searchGivenGrade;
    public TextField givenGrade;
    public TextField searchProfessorName;
    public TextField professorName;
    public DatePicker searchHandOverDate;
    public DatePicker handOverDate;

    private ObservableList<Grade> grades;
    private MenuController menuController;
    private ServiceManager service;

    public void init(MenuController menuController) {
        this.menuController = menuController;
    }

    public void setService(ServiceManager service) {
        this.service = service;
        postInit();
        setGrades(service.findAllGrade());
    }

    private void setGrades(Iterable<Grade> grades) {
        this.grades.setAll(StreamSupport
                .stream(grades.spliterator(), false)
                .collect(Collectors.toList()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TextFields.bind

        grades = FXCollections.observableArrayList();
        gradeTable.setItems(grades);
        gradeTable.setEditable(true);
    }

    private void IntegerConverter(TableColumn<Homework, Integer> homeworkTableDeadlineWeek) {
        homeworkTableDeadlineWeek.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return object.toString();
            }

            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.valueOf(string);
                } catch (NumberFormatException ignored) {
                    return 0;
                }
            }
        }));
    }

    private void addButtonToTable(TableColumn<Homework, Void> tc, String text, BiConsumer<Integer, Homework> function) {
        tc.setCellFactory(new Callback<TableColumn<Homework, Void>, TableCell<Homework, Void>>() {
            @Override
            public TableCell<Homework, Void> call(final TableColumn<Homework, Void> param) {
                return new TableCell<Homework, Void>() {
                    private final Button btn = new Button(text);

                    {
                        //TODO change with CSS
                        btn.setPadding(new Insets(4));

                        btn.setMaxSize(100, 40);
                        btn.setOnAction((ActionEvent event) -> {
                            Homework homework = getTableView().getItems().get(getIndex());
                            try {
                                function.accept(getIndex(), homework);
                            } catch (ValidationException | RepositoryException e) {
                                showError("Eroare", e.getMessage());
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        });
    }

    //TODO change function definitions
    private Integer IntegerInput(TextField tf, Boolean showWarning) {
        try {
            return Integer.parseInt(tf.getText());
        } catch (NumberFormatException e) {
            if (showWarning)
                tf.setStyle("-fx-control-inner-background: #ff442e");
            return null;
        }
    }

    private Integer IntegerInput(Spinner s, Boolean showWarning) {
        try {
            return Integer.parseInt(s.getValue().toString());
        } catch (NumberFormatException e) {
            if (showWarning)
                s.setStyle("-fx-control-inner-background: #ff442e");
            return null;
        }
    }

    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Eroare");

        Text text = new Text("  " + message);
        alert.getDialogPane().setContent(text);
        alert.getDialogPane().setPadding(new Insets(0, 5, 0, 10));
        alert.getDialogPane().setMinWidth(200);
        text.setWrappingWidth(200);

        alert.showAndWait();
        System.out.println(message);
    }

    public void addHomework(ActionEvent actionEvent) {

    }

    public void searchHomework(Event actionEvent) {

    }

    public void removeBackground(KeyEvent inputMethodEvent) {
        TextField tf = (TextField) inputMethodEvent.getSource();
        tf.setStyle("-fx-control-inner-background: white");
    }

    public void postInit() {

    }

    //TODO cleaner solution
    public void updateField(TableColumn.CellEditEvent<Homework, Object> homeworkStringCellEditEvent) {
        Grade grade = gradeTable.getSelectionModel().getSelectedItem();
        var newValue = homeworkStringCellEditEvent.getNewValue();
        var oldValue = homeworkStringCellEditEvent.getOldValue();
        switch (homeworkStringCellEditEvent.getTablePosition().getColumn()) {
            case 1:
                //homework.setDescription(newValue.toString());
                break;
            case 2:
                //homework.setStartWeek((Integer) newValue);
                break;
            case 3:
                //homework.setDeadlineWeek((Integer) newValue);
                break;
        }
    }

    public void clearFields(ActionEvent actionEvent) {

        searchHomework(null);
    }
}