package main;

import condition.Condition;
import util.Artifact;
import util.Pair;
import util.Sleep;
import util.Stat;

import java.util.List;

public class Scanner {
    private Artifact firstOfRow;
    private Artifact last;
    private final Logic logic;
    private final Condition[] conditions;
    private final boolean fiveStarsOnly;

    public Scanner(int monitorWidth, int monitorHeight,
                   boolean fiveStarsOnly, Condition... conditions) {
        logic = new Logic(monitorWidth, monitorHeight);
        this.fiveStarsOnly = fiveStarsOnly;
        this.conditions = conditions;
    }

    /**
     * Method to be executed to start the scanning. In the equivalent of this program that only
     * uses CLI for user input, this would be called after all the user inputs are confirmed and
     * the scanning and locking to begin.
     * @return Pair(artifacts processed, artifacts modified)
     */
    public Pair<Integer, Integer> execute() {
        int uniqueArtifactsLookedAt = 0;
        int artifactsModified = 0;

        while (true) { // can scroll down, no need to check if matches last, only need to check
            // if matches first of previous row
            Artifact current = craftArtifactAtPos(1, 1);
            if (current != null && current.STARS != 5 && fiveStarsOnly) {
                return new Pair<>(uniqueArtifactsLookedAt, artifactsModified);
            }
            if (current != null && current.equals(firstOfRow)) {
                break;
            }
            if (satisfiesLockCondition(current)) {
                artifactsModified += logic.lockArtifactAt(1, 1);
            }
            firstOfRow = current;
            for (int i = 2; i <= 8; i++) {
                current = craftArtifactAtPos(1, i);
                if (current != null && current.STARS != 5 && fiveStarsOnly) {
                    return new Pair<>(uniqueArtifactsLookedAt, artifactsModified);
                }
                if (satisfiesLockCondition(current)) {
                    artifactsModified += logic.lockArtifactAt(1, i);
                }
                uniqueArtifactsLookedAt++;
            }
            Sleep.pause();
            logic.scrollDown();
        }
        // can no longer scroll down
        for (int r = 2; r <= 4; r++) {
            for (int c = 1; c <= 8; c++) {
                // no longer need to check firstOfRow, only need to check if matches last on row 5
                Artifact current = craftArtifactAtPos(r, c);
                if (current != null && current.STARS != 5 && fiveStarsOnly) {
                    return new Pair<>(uniqueArtifactsLookedAt, artifactsModified);
                }
                if (current == null) {
                    // Got EXP Fodder on second to last row on edge case.
                    return new Pair<>(uniqueArtifactsLookedAt, artifactsModified);
                }
                if (satisfiesLockCondition(current)) {
                    artifactsModified += logic.lockArtifactAt(r, c);
                }
                last = current;
                uniqueArtifactsLookedAt++;
            }
        }

        // on last row
        for (int c = 1; c <= 8; c++) {
            Artifact current = craftArtifactAtPos(5, c);
            if (current != null && current.STARS != 5 && fiveStarsOnly) {
                return new Pair<>(uniqueArtifactsLookedAt, artifactsModified);
            }
            if (current == null) {
                return new Pair<>(uniqueArtifactsLookedAt, artifactsModified);
            }
            if (current.equals(last)) {
                return new Pair<>(uniqueArtifactsLookedAt, artifactsModified);
            }
            if (satisfiesLockCondition(current)) {
                artifactsModified += logic.lockArtifactAt(5, c);
            }
            last = current;
            uniqueArtifactsLookedAt++;
        }
        return new Pair<>(uniqueArtifactsLookedAt, artifactsModified);
    }

    /**
     * Self explanatory private method.
     * @param art
     * @return
     */
    private boolean satisfiesLockCondition(Artifact art) {
        for (Condition condition : conditions) {
            if (!condition.satisfiedBy(art.MAIN_STAT, art.SUBSTATS)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Constructs an Artifact object for the artifact at position (row, col) in the current screen.
     * @param row
     * @param col
     * @return Artifact instance with the values of the artifact scanned.
     */
    private Artifact craftArtifactAtPos(int row, int col) {
        logic.moveToArtifact(row, col);
        String name = logic.getArtifactName();
        if (name.equals("Sanctifying Essence") || name.equals("Sanctifying Unction")) {
            return null;
        }
        Stat mainStat = logic.getArtifactMainStat();
        List<Pair<Stat, Double>> substats = logic.getArtifactSubstats();
        int stars = logic.getStars();
        return new Artifact(name, stars, mainStat, substats);
    }
}
