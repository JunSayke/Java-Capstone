package src.data;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MinesweeperRobot {
    private final Robot robot;
    private final Rectangle selectedRegion;
    public static int MOUSE_MOVE_STEPS = 5;
    public static int MOUSE_MOVE_DELAY = 5;
    public static final int MOUSE_INTERRUPT_DISTANCE_THRESHOLD = 1;

    public MinesweeperRobot(Rectangle selectedRegion) throws AWTException {
        this.selectedRegion = selectedRegion;
        this.robot = new Robot();
    }

    public BufferedImage captureBoardImage() {
        return robot.createScreenCapture(selectedRegion);
    }

    public void clickTile(Tile tile, int tileHeight, int tileWidth, int mouseButton) {
        int x = tile.getY() * tileHeight + (int) selectedRegion.getX() + tileHeight / 2;
        int y = tile.getX() * tileWidth + (int) selectedRegion.getY() + tileWidth / 2;
        if (moveMouseSmoothly(x, y)) {
            performMouseClick(mouseButton);
        }
    }

    private boolean moveMouseSmoothly(int x, int y) {
        Point initialPos = MouseInfo.getPointerInfo().getLocation();

        for (int i = 0; i <= MOUSE_MOVE_STEPS; i++) {
            int mov_x = initialPos.x + (x - initialPos.x) * i / MOUSE_MOVE_STEPS;
            int mov_y = initialPos.y + (y - initialPos.y) * i / MOUSE_MOVE_STEPS;
            robot.mouseMove(mov_x, mov_y);
            robot.delay(MOUSE_MOVE_DELAY);

            Point currentPos = MouseInfo.getPointerInfo().getLocation();
            int deltaX = Math.abs(currentPos.x - mov_x);
            int deltaY = Math.abs(currentPos.y - mov_y);
            if (deltaX > MOUSE_INTERRUPT_DISTANCE_THRESHOLD || deltaY > MOUSE_INTERRUPT_DISTANCE_THRESHOLD) {
                return false;
            }
        }
        return true;
    }

    private void performMouseClick(int mouseButton) {
        robot.mousePress(mouseButton);
        robot.mouseRelease(mouseButton);
    }
}
