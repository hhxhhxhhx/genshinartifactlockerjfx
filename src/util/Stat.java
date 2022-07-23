package util;

public enum Stat {
    FLAT_ATK,
    FLAT_DEF,
    FLAT_HP,
    ATK_PERCENT,
    DEF_PERCENT,
    HP_PERCENT,

    ELEMENTAL_MASTERY,
    ENERGY_RECHARGE,

    PHYS_DMG_BONUS,
    PYRO_DMG_BONUS,
    CRYO_DMG_BONUS,
    HYDRO_DMG_BONUS,
    ELECTRO_DMG_BONUS,
    ANEMO_DMG_BONUS,
    GEO_DMG_BONUS,
    DENDRO_DMG_BONUS,

    CRIT_RATE,
    CRIT_DMG,
    HEALING_BONUS;

    public static Stat getStat(String stat) {
        return switch (stat) {
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
            default -> throw new IllegalStateException("Unexpected value: " + stat);
        };
    }
}
