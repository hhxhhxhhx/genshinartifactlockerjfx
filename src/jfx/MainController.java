package jfx;

import condition.Condition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Scanner;
import util.Pair;
import util.Sleep;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainController {

    @FXML
    private CheckBox fiveStarCheckbox;

    @FXML
    private TextField monitorWidth;

    @FXML
    private TextField monitorHeight;

    private ExactMatchSelectionController EMSC;
    private MultiMatchSelectionController MMSC;


    /**
     * Method executed when the main button is pressed.
     * It first compiles the ExactMatch conditions and also constructs a single AtLeastX
     * condition from all the sub-conditions.
     * It will then read the monitor dimensions and other settings before creating an instance of
     * Scanner, which will then execute the full process.
     * @param ae
     */
    @FXML
    protected void startProcess(ActionEvent ae) {
        List<Condition> conditions = new ArrayList<>();
        if (EMSC != null) {
            conditions.addAll(EMSC.getAllConditions());
        }
        if (MMSC != null) {
            conditions.add(MMSC.getCondition());
        }

        Condition[] conds = conditions.toArray(new Condition[0]);

        final int MONITOR_WIDTH = textFieldToInt(monitorWidth);
        final int MONITOR_HEIGHT = textFieldToInt(monitorHeight);
        final boolean FIVE_STARS_ONLY_CHECKED = fiveStarCheckbox.isSelected();

        System.out.println("Inputs to scanner:");
        System.out.println("Monitor width: " + MONITOR_WIDTH);
        System.out.println("Monitor height: " + MONITOR_HEIGHT);
        System.out.println("Five Stars Only: " + FIVE_STARS_ONLY_CHECKED);
        System.out.println("Conditions: " + Arrays.toString(conds));


        Scanner sc = new Scanner(textFieldToInt(monitorWidth),
                textFieldToInt(monitorHeight), fiveStarCheckbox.isSelected(), conds);

        Sleep.pause();
        Sleep.pause();
        Pair<Integer, Integer> result = sc.execute();
        System.out.println("Scanned " + result.getFirst() + " artifacts.");
        System.out.println("Locked " + result.getLast() + " artifacts.");
    }


    private Scene exactMatchEditorScene = null;

    /**
     * Creates the instance of ExactMatchConditionEditorController and displays it as a pop up.
     * The view is saved and can be reopened at any time without losing data.
     */
    @FXML
    protected void displayExactMatchEditor(ActionEvent ae) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Exact Match Condition Editor");
        stage.initModality(Modality.APPLICATION_MODAL);
        if (exactMatchEditorScene == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ExactMatchSelection.fxml"));
            exactMatchEditorScene = new Scene(loader.load());
            EMSC = loader.getController();
        }
        stage.setScene(exactMatchEditorScene);
        stage.showAndWait();
    }

    private Scene atLeastXEditorScene = null;

    /**
     * Creates the instance of AtLeastXEditorController and displays it as a pop up.
     * The view is saved and can be reopened at any time without losing data.
     */
    @FXML
    protected void displayAtLeastXMatchEditor(ActionEvent ae) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Multi Match Condition Editor");
        stage.initModality(Modality.APPLICATION_MODAL);
        if (atLeastXEditorScene == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiMatchSelection.fxml"));
            atLeastXEditorScene = new Scene(loader.load());
            MMSC = loader.getController();
        }
        stage.setScene(atLeastXEditorScene);
        stage.showAndWait();
    }

    private int textFieldToInt(TextField tf) {
        return Integer.parseInt(tf.getText());
    }
}
