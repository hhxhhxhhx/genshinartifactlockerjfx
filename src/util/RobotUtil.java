package util;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

public class RobotUtil {

    private final Robot robot;
    private Position position;

    public RobotUtil() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Failed to initialize mouse!");
        }
        position = Position.nullPos();
    }

    public void moveTo(int x, int y) {
        robot.mouseMove(x, y);
        position = new Position(x, y);
    }

    public void addX(int diff) {
        moveTo(position.X + diff, position.Y);
    }

    public void scrollDown(int amt) {
        robot.mouseWheel(amt);
    }

    public void click() {
        int leftButtonAction = InputEvent.BUTTON1_DOWN_MASK;
        robot.mousePress(leftButtonAction);
        robot.mouseRelease(leftButtonAction);
        Sleep.pause();
    }

    public int getPixelRGB() {
        return robot.getPixelColor(position.X, position.Y).getRGB();
    }

    public BufferedImage getScreenCapture(Rectangle area) {
        return robot.createScreenCapture(area);
    }
}
