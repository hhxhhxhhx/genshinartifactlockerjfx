package jfx;

import condition.ExactMatch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.Pair;
import util.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExactMatchConditionEditorController {

    @FXML
    private Button addCondition;

    @FXML
    private VBox conditionsVB;
    private List<Pair<Integer, HBox>> conditionsHBoxes = new ArrayList<>();

    private MainController mainController;

    public void attachMainController(MainController mc) {
        this.mainController = mc;
    }

    public void addCondition(Stat stat, boolean isMain, double min, boolean useMin, double max, boolean useMax) {
        StringBuilder sb = new StringBuilder();
        if (isMain)
            sb.append("Main Stat: ");
        else
            sb.append("Substat: ");
        sb.append(stat);
        if (isMain) {
            ExactMatch em = ExactMatch.mainStatMatch(stat);
            alertMainControllerToAddNewExactMatchCondition(em, sb.toString());
        } else if (!useMin && !useMax) {
            ExactMatch em = ExactMatch.substatMatch(stat, -1000, 100000);
            alertMainControllerToAddNewExactMatchCondition(em, sb.toString());
        } else {
            sb.append(" in range [");
            if (useMin)
                sb.append(min);
            sb.append(",");
            if (useMax)
                sb.append(max);
            sb.append("]");
            ExactMatch em = ExactMatch.substatMatch(stat, (useMin ? min : -1000), (useMax? max :
                    100000));
            alertMainControllerToAddNewExactMatchCondition(em, sb.toString());
        }
    }

    private void alertMainControllerToAddNewExactMatchCondition(ExactMatch condition,
                                                                String displayText) {
        int conditionID = mainController.addExactMatchCondition(condition);
        addNewConditionToDisplay(displayText, conditionID);
    }

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

    @FXML
    protected void openAddConditionPopup(ActionEvent ae) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddStat1.fxml"));
        Scene addStats1 = new Scene(loader.load());
        AddStat1Controller controller = loader.getController();
        controller.attachEditorController(this);
        stage.setScene(addStats1);
        stage.setOnCloseRequest(e -> grabStatSelectionDetailsAndAddCondition(controller));
        stage.showAndWait();
    }

    private void grabStatSelectionDetailsAndAddCondition(AddStat1Controller controller) {
        Stat stat = controller.getStat();
        boolean isMainStat = controller.isMainStat();
        Pair<Double, Double> range = controller.getRange();
        addCondition(stat, isMainStat, range.getFirst(), true, range.getLast(), true);
    }

    protected void removeExistingCondition(int conditionID) {
        if (mainController != null) {
            boolean removeSuccess = mainController.removeExactMatchByID(conditionID);
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
        // At this point, hb should not ever be null;
        conditionsVB.getChildren().removeAll(hb);
    }
}
