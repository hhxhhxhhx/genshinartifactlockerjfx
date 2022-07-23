package jfx;

import condition.AtLeastX;
import condition.ExactMatch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import util.ObjectWrapper;
import util.Pair;
import util.Stat;

import java.util.ArrayList;
import java.util.List;

public class MultiMatchSelectionController {

    public AtLeastX getCondition() {
        Stat mainStat = null;
        List<Stat> substats = new ArrayList<>();
        List<Pair<Double, Double>> substatRanges = new ArrayList<>();
        List<ObjectWrapper<CheckBox, ComboBox<String>, TextField, TextField>> info = new ArrayList<>();
        info.add(new ObjectWrapper<>(cb1, selector1, min1, max1));
        info.add(new ObjectWrapper<>(cb2, selector2, min2, max2));
        info.add(new ObjectWrapper<>(cb3, selector3, min3, max3));
        info.add(new ObjectWrapper<>(cb4, selector4, min4, max4));
        info.add(new ObjectWrapper<>(cb5, selector5, min5, max5));
        for (ObjectWrapper<CheckBox, ComboBox<String>, TextField, TextField> ow : info) {
            if (ow.SECOND.getValue() == null || ow.SECOND.getValue().equals("Select Stat")) {
                continue;
            }
            boolean isMain = ow.FIRST.isSelected();
            Stat stat = Stat.getStat(ow.SECOND.getValue());
            if (isMain) {
                mainStat = stat;
                continue;
            }
            double min = textFieldToDouble(ow.THIRD, 0);
            double max = textFieldToDouble(ow.FOURTH, 10000);
            substats.add(stat);
            substatRanges.add(new Pair<>(min, max));
        }

        if (mainStat != null) {
            return AtLeastX.createAtLeastXStats((int) textFieldToDouble(numMatchesRequired, 1),
                                                mainStat, substats, substatRanges);
        } else {
            return AtLeastX.createAtLeastXSubstats((int) textFieldToDouble(numMatchesRequired, 1),
                                                substats, substatRanges);
        }
    }

    @FXML
    private void reset1(ActionEvent ae) {
        cb1.setSelected(false);
        selector1.getSelectionModel().clearSelection();
        selector1.setValue("Select Stat");
        min1.setText("");
        max1.setText("");
    }
    @FXML
    private void reset2(ActionEvent ae) {
        cb2.setSelected(false);
        selector2.getSelectionModel().clearSelection();
        selector2.setValue("Select Stat");
        min2.setText("");
        max2.setText("");
    }
    @FXML
    private void reset3(ActionEvent ae) {
        cb3.setSelected(false);
        selector3.getSelectionModel().clearSelection();
        selector3.setValue("Select Stat");
        min3.setText("");
        max3.setText("");
    }
    @FXML
    private void reset4(ActionEvent ae) {
        cb4.setSelected(false);
        selector4.getSelectionModel().clearSelection();
        selector4.setValue("Select Stat");
        min4.setText("");
        max4.setText("");
    }
    @FXML
    private void reset5(ActionEvent ae) {
        cb5.setSelected(false);
        selector5.getSelectionModel().clearSelection();
        selector5.setValue("Select Stat");
        min5.setText("");
        max5.setText("");
    }

    private double textFieldToDouble(TextField tf, double default_) {
        if (tf == null || tf.getText() == null || tf.getText().equals("")) {
            return default_;
        }
        return Double.parseDouble(tf.getText());
    }

    @FXML
    private CheckBox cb1;
    @FXML
    private CheckBox cb2;
    @FXML
    private CheckBox cb3;
    @FXML
    private CheckBox cb4;
    @FXML
    private CheckBox cb5;
    @FXML
    private ComboBox<String> selector1;
    @FXML
    private ComboBox<String> selector2;
    @FXML
    private ComboBox<String> selector3;
    @FXML
    private ComboBox<String> selector4;
    @FXML
    private ComboBox<String> selector5;
    @FXML
    private TextField min1;
    @FXML
    private TextField min2;
    @FXML
    private TextField min3;
    @FXML
    private TextField min4;
    @FXML
    private TextField min5;
    @FXML
    private TextField max1;
    @FXML
    private TextField max2;
    @FXML
    private TextField max3;
    @FXML
    private TextField max4;
    @FXML
    private TextField max5;
    @FXML
    private TextField numMatchesRequired;
}
