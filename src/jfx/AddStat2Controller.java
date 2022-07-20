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

    /**
     * Self explanatory
     * @return true if the checkbox for main stat is checked, false otherwise.
     */
    public boolean isMainStat() {
        return mainStatCheckBox.isSelected();
    }

    /**
     * Gets the selected value of the ComboBox and gets the proper Enum translation
     * @return corresponding Enum for the selected value.
     */
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

    /**
     * Returns the two numbers in the two text fields. No errors are thrown should the inputs be
     * invalid. The program would not crash, but fail to work when Scanner.execute() is called.
     * It is assumed the user is reasonable and would find the cause of this error.
     * @return a Pair instance with the first value being the lower bound, the second being the
     * upper bound.
     */
    public Pair<Double, Double> getRange() {
        return new Pair<>(textFieldToDouble(lowerBound), textFieldToDouble(upperBound));
    }

    private double textFieldToDouble(TextField tf) {
        return Double.parseDouble(tf.getText());
    }
}
