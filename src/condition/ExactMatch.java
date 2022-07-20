package condition;

import util.Pair;
import util.Stat;

import java.util.List;

public class ExactMatch implements Condition {

    private final Stat mainStat;
    private final Stat substat;
    private final double minSubstat;
    private final double maxSubstat;

    private final boolean checksMain;

    private ExactMatch(Stat mainStat, Stat substat, double minSubstat, double maxSubstat,
                       boolean checksMain) {
        this.mainStat = mainStat;
        this.substat = substat;
        this.minSubstat = minSubstat;
        this.maxSubstat = maxSubstat;
        this.checksMain = checksMain;
    }
    public static ExactMatch mainStatMatch(Stat mainStat) {
        return new ExactMatch(mainStat, null, 0, 0, true);
    }
    public static ExactMatch substatMatch(Stat substat, double minValue, double maxValue) {
        return new ExactMatch(null, substat, minValue, maxValue, false);
    }

    public Stat getStat() {
        if (checksMain) {
            return mainStat;
        } else {
            return substat;
        }
    }

    /**
     * Can result in undefined behavior, but it is to be expected that this would only be called
     * in well defined situations.
     * @return substat value ranges
     */
    public Pair<Double, Double> getRange() {
        return new Pair<>(minSubstat, maxSubstat);
    }

    public boolean isMainStat() {
        return checksMain;
    }

    @Override
    public String toString() {
        if (checksMain) {
            return "Exact Match of Main Stat " + mainStat;
        } else {
            return "Exact Match of Substat " + substat + " in range (" + minSubstat + "," + maxSubstat + ")";
        }
    }

    @Override
    public boolean satisfiedBy(Stat mainStat, List<Pair<Stat, Double>> substats) {
        if (checksMain) {
            return this.mainStat == mainStat;
        } else {
            for (Pair<Stat, Double> substat : substats) {
                if (substat.getFirst() == this.substat) {
                    if (minSubstat <= substat.getLast() && substat.getLast() <= maxSubstat) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
