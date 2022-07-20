package condition;

import util.Pair;
import util.Stat;

import java.util.List;

public class AtLeastX implements Condition {

    private final int X;

    private final Stat mainStat;
    private final List<Stat> substats;
    private final List<Pair<Double, Double>> substatValues;

    private final boolean includeMainStat;

    private AtLeastX(int x, Stat mainStat, List<Stat> substats,
                    List<Pair<Double, Double>> substatValues, boolean usingMain) {
        this.X = x;
        this.mainStat = mainStat;
        this.substats = substats;
        this.substatValues = substatValues;
        this.includeMainStat = usingMain;
    }

    /**
     * Static method to create an AtLeastX instance that only takes into consideration substats.
     * @param X
     * @param substat
     * @param substatValues
     * @return AtLeastX instance corresponding to the input conditions.
     */
    public static AtLeastX createAtLeastXSubstats(int X, List<Stat> substat,
                                      List<Pair<Double, Double>> substatValues) {
        return new AtLeastX(X, null, substat, substatValues, false);
    }

    /**
     * Static method to create an AtLeastX instance that can include both main stats and substats.
     * @param X
     * @param mainStat
     * @param substat
     * @param substatValues
     * @return AtLeastX instance corresponding to the input conditions.
     */
    public static AtLeastX createAtLeastXStats(int X, Stat mainStat,
                                                    List<Stat> substat,
                                                    List<Pair<Double, Double>> substatValues) {
        return new AtLeastX(X, mainStat, substat, substatValues, true);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Must meet at least ").append(X).append(" of the following: ");
        if (includeMainStat) {
            sb.append("[Main Stat: ").append(mainStat).append("; ");
        }
        for (int i = 0; i < substatValues.size() - 1; i++) {
            sb.append("Sub Stat: ").append(substats.get(i)).append(" in range (").append(substatValues.get(i).getFirst()).append(',').append(substatValues.get(i).getLast()).append("); ");
        }
        sb.append("Sub Stat: ").append(substats.get(substatValues.size() - 1)).append(" in range " +
                "(").append(substatValues.get(substatValues.size() - 1).getFirst()).append(',')
                .append(substatValues.get(substatValues.size() - 1).getLast()).append(")]");
        return sb.toString();
    }

    /**
     * Method to be called to verify if the provided main stat and substats meet the condition
     * set by this instance of AtLeastX.
     * @param mainStat
     * @param substats
     * @return true if the conditions are satisfied, false otherwise.
     */
    @Override
    public boolean satisfiedBy(Stat mainStat, List<Pair<Stat, Double>> substats) {
        int matches = 0;
        if (includeMainStat) {
            if (this.mainStat == mainStat)
                matches++;
        }
        for (int i = 0; i < this.substats.size(); i++) {
            Stat requiredSubstat = this.substats.get(i);
            for (Pair<Stat, Double> testSubstats : substats) {
                if (requiredSubstat == testSubstats.getFirst()) {
                    if (this.substatValues.get(i).getFirst() <= testSubstats.getLast() &&
                            testSubstats.getLast() <= this.substatValues.get(i).getLast()) {
                        matches++;
                    }
                }
            }
        }
        return matches >= this.X;
    }
}
