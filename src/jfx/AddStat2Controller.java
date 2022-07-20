package jfx;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import util.Pair;
import util.Stat;

public class AddStat2Controller {
    @FXML
    private ComboBox<String> selector;

    @FXML
    private CheckBox mainStatCheckBox;

    @FXML
    private TextField lowerBound;

    @FXML
    private TextField upperBound;

    private AtLeastXEditorController ALXEC;

    @FXML
    private void initialize() {
        //selector.lookup(".list-view").setStyle("-fx-font-size: 20pt;");
        //selector.setStyle("-fx-font-size: 20pt;");
    }

    public void attachEditorController(AtLeastXEditorController alxec) {
        this.ALXEC = alxec;
    }

    public boolean isMainStat() {
        return mainStatCheckBox.isSelected();
    }

    public Stat getStat() {
        return switch (selector.getValue()) {
            case "Crit Rate" -> Stat.CRIT_RATE;
            case "Crit Damage" -> Stat.CRIT_DMG;
            case "ATK%" -> Stat.ATK_PERCENT;
            case "ATK" -> Stat.FLAT_ATK;
            case "Elemental Mastery" -> Stat.ELEMENTAL_MASTERY;
            case "Energy Recharge" -> Stat.ENERGY_RECHARGE;
            case "Physical DMG Bonus" -> Stat.PHYS_DMG_BONUS;
            case "Pyro DMG Bonus" -> Stat.PYRO_DMG_BONUS;
            case "Hydro DMG Bonus" -> Stat.HYDRO_DMG_BONUS;
            case "Cryo DMG Bonus" -> Stat.CRYO_DMG_BONUS;
            case "Electro DMG Bonus" -> Stat.ELECTRO_DMG_BONUS;
            case "Anemo DMG Bonus" -> Stat.ANEMO_DMG_BONUS;
            case "Geo DMG Bonus" -> Stat.GEO_DMG_BONUS;
            case "Dendro DMG Bonus" -> Stat.DENDRO_DMG_BONUS;
            case "HP%" -> Stat.HP_PERCENT;
            case "HP" -> Stat.FLAT_HP;
            case "DEF%" -> Stat.DEF_PERCENT;
            case "DEF" -> Stat.FLAT_DEF;
            case "Healing Bonus" -> Stat.HEALING_BONUS;
            default -> throw new IllegalStateException("Unexpected value: " + selector.getValue());
        };
    }

    public Pair<Double, Double> getRange() {
        return new Pair<>(textFieldToDouble(lowerBound), textFieldToDouble(upperBound));
    }

    private double textFieldToDouble(TextField tf) {
        return Double.parseDouble(tf.getText());
    }
}
