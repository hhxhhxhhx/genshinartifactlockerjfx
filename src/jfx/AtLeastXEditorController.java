package jfx;

import condition.ExactMatch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.Pair;
import util.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AtLeastXEditorController {

    @FXML
    private TextField requiredNumOfMatch;

    @FXML
    private VBox conditionsVB;
    private final List<Pair<Integer, HBox>> conditionsHBoxes = new ArrayList<>();

    private MainController mainController;

    /**
     * In an ideal world, this would take in the methods in MainController that need to be called.
     * This compromises the security of the program by making extra methods being visible, but
     * the workaround is a bit more complicated and more than excessive for this program.
     * @param mc
     */
    public void attachMainController(MainController mc) {
        this.mainController = mc;
    }

    /**
     * Captures the value in the textfield in the view and returns an integer representation. An
     * invalid input is ignored and replaced with 1. The user is allowed to enter an input
     * greater than the number of conditions they specified. In this case, nothing could be
     * matched. There is no deterrent to this, and the user is expected to be reasonable and be
     * able to fix the error.
     * @return how many matches need to be made under AtLeastX condition.
     */
    public int getX() {
        String rNOM = requiredNumOfMatch.getText();
        int val = 1;
        try {
            val = Integer.parseInt(rNOM);
        } catch (NumberFormatException nfe) {
            // ignore
        }
        return val;
    }

    /**
     * Private methods to add a potential condition. The parameters should be self explanatory.
     * @param stat
     * @param isMain
     * @param min
     * @param max
     */
    private void addCondition(Stat stat, boolean isMain, double min, double max) {
        StringBuilder sb = new StringBuilder();
        if (isMain)
            sb.append("Main Stat: ");
        else
            sb.append("Substat: ");
        sb.append(stat);
        if (isMain) {
            ExactMatch em = ExactMatch.mainStatMatch(stat);
            alertMainControllerToAddNewAtLeastXCondition(em, sb.toString());
        } else {
            sb.append(" in range [");
            sb.append(min);
            sb.append(",");
            sb.append(max);
            sb.append("]");
            ExactMatch em = ExactMatch.substatMatch(stat, min, max);
            alertMainControllerToAddNewAtLeastXCondition(em, sb.toString());
        }
    }

    /**
     * Private methods to add a potential condition. The parameters should be self explanatory.
     * @param condition
     * @param displayText
     */
    private void alertMainControllerToAddNewAtLeastXCondition(ExactMatch condition,
                                                                String displayText) {
        int conditionID = mainController.addAtLeastXCondition(condition);
        addNewConditionToDisplay(displayText, conditionID);
    }

    /**
     * Private methods to add a potential condition. The parameters should be self explanatory.
     * @param displayText
     * @param conditionID
     */
    private void addNewConditionToDisplay(String displayText, int conditionID) {
        HBox hb = new HBox();
        hb.setSpacing(15);
        hb.setAlignment(Pos.CENTER);
        Label lb = new Label(displayText + "; " + conditionID);
        lb.setMinWidth(300);
        Button removeButton = new Button("X");
        removeButton.setOnAction(e -> removeExistingCondition(conditionID));
        removeButton.setId(String.valueOf(conditionID));
        hb.getChildren().addAll(lb, removeButton);
        conditionsHBoxes.add(new Pair<>(conditionID, hb));
        conditionsVB.getChildren().add(hb);
    }

    /**
     * Method to show the stat selection view. IOException should never be thrown.
     * @param ae
     * @throws IOException
     */
    @FXML
    protected void openAddConditionPopup(ActionEvent ae) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Add New Requirement");
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddStat2.fxml"));
        Scene addStats2 = new Scene(loader.load());
        AddStat2Controller controller = loader.getController();
        stage.setScene(addStats2);
        stage.setOnCloseRequest(e -> grabStatSelectionDetailsAndAddCondition(controller));
        stage.showAndWait();
    }

    /**
     * Method to be invoked when the popup stat selection view is closed. It will call
     * addCondition() to add the Condition to be potentially matched.
     * @param controller
     */
    private void grabStatSelectionDetailsAndAddCondition(AddStat2Controller controller) {
        Stat stat = controller.getStat();
        boolean isMainStat = controller.isMainStat();
        Pair<Double, Double> range = controller.getRange();
        addCondition(stat, isMainStat, range.getFirst(), range.getLast());
    }

    /**
     * Method to be invoked when an "X" button is clicked on. It will find the correct Condition
     * by ID and will remove it from all necessary places.
     * @param conditionID
     */
    @FXML
    protected void removeExistingCondition(int conditionID) {
        if (mainController != null) {
            boolean removeSuccess = mainController.removeAtLeastXByID(conditionID);
            if (!removeSuccess) {
                throw new RuntimeException("Unable to remove existing condition by ID. There are " +
                        "no matches to this ID!");
            }
        }
        HBox hb = null;
        for (Pair<Integer, HBox> pair : conditionsHBoxes) {
            if(pair.getFirst().equals(conditionID)) {
                hb = pair.getLast();
                break;
            }
        }
        conditionsVB.getChildren().removeAll(hb);
    }
}
