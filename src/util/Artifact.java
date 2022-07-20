package util;

import java.util.List;

public class Artifact {
    public final String NAME;
    public final int STARS;
    public final Stat MAIN_STAT;
    public final List<Pair<Stat, Double>> SUBSTATS;

    public Artifact(String name, int stars, Stat mainStat, List<Pair<Stat, Double>> substats) {
        this.NAME = name;
        this.STARS = stars;
        this.MAIN_STAT = mainStat;
        this.SUBSTATS = substats;
    }

    @Override
    public String toString() {
        return NAME + "\n" + STARS + " Stars\n" + MAIN_STAT + "\n" + SUBSTATS;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Artifact other) {
            if (other.NAME.equals(this.NAME) && other.MAIN_STAT.equals(this.MAIN_STAT)) {
                if (other.STARS == this.STARS && other.SUBSTATS.size() == this.SUBSTATS.size()) {
                    for (int i = 0; i < other.SUBSTATS.size(); i++) {
                        if (other.SUBSTATS.get(i).getFirst() != this.SUBSTATS.get(i).getFirst() ||
                            !(other.SUBSTATS.get(i).getLast().equals(this.SUBSTATS.get(i).getLast()))) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
