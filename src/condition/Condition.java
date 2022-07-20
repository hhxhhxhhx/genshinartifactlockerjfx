package condition;

import util.Pair;
import util.Stat;

import java.util.List;

public interface Condition {
    boolean satisfiedBy(Stat mainStat, List<Pair<Stat, Double>> substats);
    String toString();
}
