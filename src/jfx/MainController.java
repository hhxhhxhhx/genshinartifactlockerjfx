package jfx;

import condition.AtLeastX;
import condition.Condition;
import condition.ExactMatch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Scanner;
import util.Pair;
import util.Sleep;
import util.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainController {

    @FXML
    private Button startScan;

    @FXML
    private Button showExactMatch;

    @FXML
    private Button showMultiMatch;

    @FXML
    private CheckBox fiveStarCheckbox;

    @FXML
    private TextField monitorWidth;

    @FXML
    private TextField monitorHeight;

    private int currentExactMatchID = 0;
    private List<Pair<Integer, ExactMatch>> exactMatchList = new ArrayList<>();
    private int currentAtLeastXID = 0;
    private List<Pair<Integer, ExactMatch>> atLeastXList = new ArrayList<>();
    private AtLeastXEditorController ALXEC;

    public int addExactMatchCondition(ExactMatch exactMatchCondition) {
        exactMatchList.add(new Pair<>(currentExactMatchID, exactMatchCondition));
        return currentExactMatchID++;
    }

    public int addAtLeastXCondition(ExactMatch atLeastXCondition) {
        atLeastXList.add(new Pair<>(currentAtLeastXID, atLeastXCondition));
        return currentAtLeastXID++;
    }

    public boolean removeExactMatchByID(int id) {
        for (int i = 0; i < exactMatchList.size(); i++) {
            if (exactMatchList.get(i).getFirst().equals(id)) {
                exactMatchList.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean removeAtLeastXByID(int id) {
        for (int i = 0; i < atLeastXList.size(); i++) {
            if (atLeastXList.get(i).getFirst().equals(id)) {
                atLeastXList.remove(i);
                return true;
            }
        }
        return false;
    }

    @FXML
    protected void startProcess(ActionEvent ae) {
        List<Condition> conditions = new ArrayList<>();
        for (Pair<Integer, ExactMatch> pair : exactMatchList) {
            conditions.add(pair.getLast());
        }
        if (atLeastXList.size() > 0) {
            int xVal = atLeastXList.size();
            if (ALXEC != null) {
                xVal = ALXEC.getX();
            }
            List<ExactMatch> atLeastXConditions = new ArrayList<>();
            atLeastXList.forEach(e -> atLeastXConditions.add(e.getLast()));

            boolean containsMainStatRequirement = false;
            int mainStatIndex = -1;
            for (int i = 0; i < atLeastXConditions.size(); i++) {
                ExactMatch em = atLeastXConditions.get(i);
                if (em.isMainStat()) {
                    containsMainStatRequirement = true;
                    mainStatIndex = i;
                    break;
                }
            }
            if (!containsMainStatRequirement) {
                List<Stat> substats = new ArrayList<>();
                List<Pair<Double, Double>> ranges = new ArrayList<>();
                atLeastXConditions.forEach(e -> substats.add(e.getStat()));
                atLeastXConditions.forEach(e -> ranges.add(e.getRange()));

                AtLeastX aLX = AtLeastX.createAtLeastXSubstats(xVal, substats, ranges);

                conditions.add(aLX);
            } else {
                List<Stat> substats = new ArrayList<>();
                List<Pair<Double, Double>> ranges = new ArrayList<>();
                Stat mainStat = null;
                for (int i = 0; i < atLeastXConditions.size(); i++) {
                    ExactMatch em = atLeastXConditions.get(i);
                    if (i != mainStatIndex) {
                        substats.add(em.getStat());
                        ranges.add(em.getRange());
                    } else {
                        mainStat = em.getStat();
                    }
                }

                AtLeastX aLX = AtLeastX.createAtLeastXStats(xVal, mainStat, substats, ranges);

                conditions.add(aLX);
            }
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
    @FXML
    protected void displayExactMatchEditor(ActionEvent ae) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Exact Match Condition Editor");
        stage.initModality(Modality.APPLICATION_MODAL);
        if (exactMatchEditorScene == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ExactMatchEditor.fxml"));
            exactMatchEditorScene = new Scene(loader.load());
            ExactMatchConditionEditorController controller = loader.getController();
            controller.attachMainController(this);
        }
        stage.setScene(exactMatchEditorScene);
        stage.showAndWait();
    }

    private Scene atLeastXEditorScene = null;
    @FXML
    protected void displayAtLeastXMatchEditor(ActionEvent ae) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Multi Match Condition Editor");
        stage.initModality(Modality.APPLICATION_MODAL);
        if (atLeastXEditorScene == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AtLeastXEditor.fxml"));
            atLeastXEditorScene = new Scene(loader.load());
            AtLeastXEditorController controller = loader.getController();
            controller.attachMainController(this);
            ALXEC = controller;
        }
        stage.setScene(atLeastXEditorScene);
        stage.showAndWait();
    }

    private int textFieldToInt(TextField tf) {
        return Integer.parseInt(tf.getText());
    }
}
