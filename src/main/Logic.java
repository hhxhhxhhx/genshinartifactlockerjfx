package main;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import util.*;
import java.util.List;
import java.util.ArrayList;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.regex.Pattern;

public class Logic {

    private final int windowWidth = 1600;
    private final int windowHeight = 900;

    private final int monitorWidth;
    private final int monitorHeight;

    private final int offsetX;
    private final int offsetY;

    private final RobotUtil robot;

    private final Tesseract tesseract;

    public Logic(int monitorWidth, int monitorHeight) {
        this.monitorWidth = monitorWidth;
        this.monitorHeight = monitorHeight;

        this.offsetX = (monitorWidth - windowWidth) / 2;
        this.offsetY = (monitorHeight - windowHeight) / 2 + 11;

        robot = new RobotUtil();

        tesseract = new Tesseract();
        tesseract.setLanguage("eng");
        tesseract.setOcrEngineMode(1);
        tesseract.setDatapath("src/util/");
    }



    /**
     * If an artifact is already clicked on, this method will return the star count of this
     * artifact, by brute forcing and checking 5 pixels. There is no OCR involved.
     *
     * @return int: star count of the clicked artifact
     */
    public int getStars() {

        int fixedXOffset = 1125;
        int fixedYOffset = 308;
        int fixedStarOffset = 27;

        int rgb = 0;

        // check 5th star location
        robot.moveTo(this.offsetX + fixedXOffset + 4 * fixedStarOffset,
                this.offsetY + fixedYOffset);
        rgb = robot.getPixelRGB();
        if (rgb == -13262) {
            Sleep.pause();
            return 5;
        }

        for (int i = 4; i >= 1; i--) {
            robot.addX(-fixedStarOffset);
            rgb = robot.getPixelRGB();
            if (rgb == -13262) {
                Sleep.pause();
                return i;
            }
        }

        throw new RuntimeException("Error in main.Logic.checkStars()! Unable to determine star " +
                "count!");
    }

    /**
     * If an artifact is already clicked on, this method will return whether the selected artifact
     * is locked, by checking a single pixel on the lock symbol. There is no OCR involved.
     *
     * @return boolean: locked status of the clicked artifact
     */
    public boolean isLocked() {

        int fixedXOffset = 1450;
        int fixedYOffset = 358;

        robot.moveTo(offsetX + fixedXOffset, offsetY + fixedYOffset);
        Sleep.pause();

        int rgb = robot.getPixelRGB();

        // Unlocked RGB: -856343
        if (-856343 - 3 <= rgb && rgb <= -856343 + 3) {
            Sleep.pause();
            return false;

        // Locked RGB: -11971738
        } else if (-11971738 - 3 <= rgb && rgb <= -11971738 + 3) {
            Sleep.pause();
            return true;

        // Value doesn't match either one. Error.
        } else {
            System.out.println(rgb);
            throw new RuntimeException("Error in main.Logic.isLocked(). RGB value does not match " +
                    "either locked or unlocked symbols!");
        }
    }

    public void manualMove(int x, int y) {
        robot.moveTo(x, y);
    }

    /**
     * Move the mouse to exact (x, y), and clicks on that position to focus that artifact.
     * Used prior to getStars() to get the correct artifact.
     *
     * @param row: row of existing group of artifacts. (1 <= row <= 5)
     * @param col: column of existing group of artifacts. (1 <= col <= 8)
     */
    public void moveToArtifact(int row, int col) {
        if (row < 1 || row > 5) {
            throw new IndexOutOfBoundsException("Error in main.Logic.moveToArtifact(). Row index " +
                    "out of range! Should be (1 <= row <= 5)!");
        }
        if (col < 1 || col > 8) {
            throw new IndexOutOfBoundsException("Error in main.Logic.moveToArtifact(). Col index " +
                    "out of range! Should be (1 <= col <= 8)!");
        }

        int fixedXOffset = 150;
        int fixedYOffset = 125;
        int xDiff = 121;
        int yDiff = 147;

        robot.moveTo(offsetX + fixedXOffset + xDiff * (col - 1),
                     offsetY + fixedYOffset + yDiff * (row - 1));
        robot.click();
    }

    private int scrollCounter = 0;
    public void scrollDown() {
        moveToArtifact(1, 1);
        if (scrollCounter == 3) {
            for (int i = 0; i < 9; i++) {
                robot.scrollDown(10);
            }
            scrollCounter = 0;
            Sleep.pause();
        } else {
            for (int i = 0; i < 10; i++) {
                robot.scrollDown(10);
            }
            scrollCounter++;
            Sleep.pause();
        }
    }

    public int lockArtifactAt(int row, int col) {
        moveToArtifact(row, col);
        if (!isLocked()) {
            int fixedXOffset = 1450;
            int fixedYOffset = 358;
            robot.moveTo(offsetX + fixedXOffset, offsetY + fixedYOffset);
            robot.click();
            Sleep.pause();
            Sleep.pause();
            Sleep.pause();
            return 1;
        }
        return 0;
    }

    public String getArtifactName() {
        int startX = offsetX + 1090;
        int startY = offsetY + 100;
        int imageWidth = 400;
        int imageHeight = 45;
        Rectangle area = new Rectangle(startX, startY, imageWidth, imageHeight);
        return doOCR(robot.getScreenCapture(area));
    }

    public Piece getArtifactPiece() {
        int startX = offsetX + 1100;
        int startY = offsetY + 150;
        int imageWidth = 200;
        int imageHeight = 30;
        Rectangle area = new Rectangle(startX, startY, imageWidth, imageHeight);
        String str = doOCR(robot.getScreenCapture(area));
        return switch (str) {
            case "Flower of Life" -> Piece.FLOWER;
            case "Plume of Death" -> Piece.FEATHER;
            case "Sands of Eon" -> Piece.TIMEPIECE;
            case "Goblet of Eonothem" -> Piece.GOBLET;
            case "Circlet of Logos" -> Piece.CIRCLET;
            default -> null;
        };
    }

    public Stat getArtifactMainStat() {
        Piece pieceType = this.getArtifactPiece();
        switch (pieceType) {
            case FLOWER:
                return Stat.FLAT_HP;
            case FEATHER:
                return Stat.FLAT_ATK;
        }

        int startX = offsetX + 1110;
        int startY = offsetY + 220;
        int imageWidth = 210;
        int imageHeight = 30;
        Rectangle area = new Rectangle(startX, startY, imageWidth, imageHeight);
        String str = doOCR(robot.getScreenCapture(area));
        return switch (str) {
            case "HP" -> Stat.HP_PERCENT;
            case "ATK" -> Stat.ATK_PERCENT;
            case "DEF" -> Stat.DEF_PERCENT;
            case "Energy Recharge" -> Stat.ENERGY_RECHARGE;
            case "Elemental Mastery" -> Stat.ELEMENTAL_MASTERY;
            case "Hydro DMG Bonus" -> Stat.HYDRO_DMG_BONUS;
            case "Pyro DMG Bonus" -> Stat.PYRO_DMG_BONUS;
            case "Electro DMG Bonus" -> Stat.ELECTRO_DMG_BONUS;
            case "Cryo DMG Bonus" -> Stat.CRYO_DMG_BONUS;
            case "Geo DMG Bonus" -> Stat.GEO_DMG_BONUS;
            case "Anemo DMG Bonus" -> Stat.ANEMO_DMG_BONUS;
            case "Dendro DMG Bonus" -> Stat.DENDRO_DMG_BONUS;
            case "Physical DMG Bonus" -> Stat.PHYS_DMG_BONUS;
            case "CRIT Rate" -> Stat.CRIT_RATE;
            case "CRIT DMG" -> Stat.CRIT_DMG;
            case "Healing Bonus" -> Stat.HEALING_BONUS;
            default -> throw new RuntimeException("Unable to determine main stat in main.Logic" +
                    ".getArtifactMainStat!");
        };
    }

    public List<Pair<Stat, Double>> getArtifactSubstats() {
        List<Pair<Stat, Double>> substats = new ArrayList<>();

        int startX = offsetX + 1130;
        int startY = offsetY + 395;
        int imageWidth = 280;
        int imageHeight = 125;
        Rectangle area = new Rectangle(startX, startY, imageWidth, imageHeight);
        String str = doOCR(robot.getScreenCapture(area));
        while (str.contains("\n\n")) {
            str = str.replaceAll("\n\n", "\n");
        }
        String[] strs = str.split("\n");
        for (String s : strs) {
            if (s.startsWith("Energy")) {
                substats.add(generatePair(s, Stat.ENERGY_RECHARGE));
            } else if (s.startsWith("Elemental Mastery+")) {
                substats.add(generatePair(s, Stat.ELEMENTAL_MASTERY));
            } else if (s.startsWith("HP") && s.contains("%")) {
                substats.add(generatePair(s, Stat.HP_PERCENT));
            } else if (s.startsWith("HP")) {
                substats.add(generatePair(s, Stat.FLAT_HP));
            } else if (s.startsWith("DEF") && s.contains("%")) {
                substats.add(generatePair(s, Stat.DEF_PERCENT));
            } else if (s.startsWith("DEF")) {
                substats.add(generatePair(s, Stat.FLAT_DEF));
            } else if (s.startsWith("ATK") && s.contains("%")) {
                substats.add(generatePair(s, Stat.ATK_PERCENT));
            } else if (s.startsWith("ATK")) {
                substats.add(generatePair(s, Stat.FLAT_ATK));
            } else if (s.startsWith("CRIT Rate")) {
                substats.add(generatePair(s, Stat.CRIT_RATE));
            } else if (s.startsWith("CRIT DMG")) {
                substats.add(generatePair(s, Stat.CRIT_DMG));
            } else {
                break;
            }
        }

        return substats;
    }

    private Pair<Stat, Double> generatePair(String str, Stat stat) {
        boolean percentage = switch (stat) {
            case HP_PERCENT, ATK_PERCENT, DEF_PERCENT, CRIT_RATE, CRIT_DMG, ENERGY_RECHARGE -> true;
            default -> false;
        };
        if (percentage) {
            return new Pair<>(stat, Double.valueOf(str.substring(str.indexOf("+") + 1,
                    str.length() - 1).replace(",", "")));
        } else {
            return new Pair<>(stat, Double.valueOf(str.substring(str.indexOf("+") + 1).replace(",", "")));
        }
    }

    public String doOCR(BufferedImage screenCapture) {
        try {
            return tesseract.doOCR(screenCapture).strip();
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return null;
    }
}
