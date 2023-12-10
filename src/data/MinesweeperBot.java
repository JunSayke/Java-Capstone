package src.data;

import java.awt.*;
import java.awt.event.InputEvent;

public class MinesweeperBot {
    private final Robot robot;
    private final MinesweeperSolver minesweeperSolver;
    private final MinesweeperConfig minesweeperConfig;
    private Rectangle selectedRegion;
    private static final String MINESWEEPER_BOARD_SAVE_PATH = "src\\data\\temp\\MinesweeperBoard.png";
    private static final int MOUSE_MOVE_STEPS = 50;
    private static final int MOUSE_MOVE_DELAY = 2;
    private static final int LEFT_CLICK = InputEvent.BUTTON1_DOWN_MASK;
    private static final int RIGHT_CLICK = InputEvent.BUTTON3_DOWN_MASK;

    public MinesweeperBot(Rectangle selectedRegion, MinesweeperSolver minesweeperSolver, MinesweeperConfig minesweeperConfig) throws AWTException {
        this.selectedRegion = selectedRegion;
        this.minesweeperSolver = minesweeperSolver;
        this.minesweeperConfig = minesweeperConfig;
        robot = new Robot();
    }

    public void calculateProbabilities() {
        captureBoardImage();
        validateBoardState();
        minesweeperSolver.solveBoard(minesweeperConfig.board, minesweeperConfig.totalMines - minesweeperConfig.knownMines);
        minesweeperSolver.displayBoard(minesweeperConfig.board);
        minesweeperSolver.displayProbability(minesweeperConfig.board);
    }

    private void captureBoardImage() {
        BoardAnalyzer boardAnalyzer = new BoardAnalyzer(robot.createScreenCapture(selectedRegion), minesweeperConfig);
        boardAnalyzer.setSaveTiles(true).setTileOffset(0).setImageTolerance(20).analyzeBoard();
        boardAnalyzer.saveImage(MINESWEEPER_BOARD_SAVE_PATH);
    }

    public void automateClicks() {
        for (Tile[] rows : minesweeperConfig.board) {
            for (Tile col : rows) {
                double prob = col.getProbability();
                if ((prob == 0 || prob == 1) && col.getState() == Block.CLOSED) {
                    int x = col.getY() * minesweeperConfig.tileSide + (int) selectedRegion.getX() + minesweeperConfig.tileSide / 2;
                    int y = col.getX() * minesweeperConfig.tileSide + (int) selectedRegion.getY() + minesweeperConfig.tileSide / 2;

                    moveMouseSmoothly(x, y);

                    int mouseButton = (prob == 0) ? LEFT_CLICK : RIGHT_CLICK;
                    robot.mousePress(mouseButton);
                    robot.mouseRelease(mouseButton);
                }
            }
        }
    }

    private void moveMouseSmoothly(int x, int y) {
        Point initialPos = MouseInfo.getPointerInfo().getLocation();

        for (int i = 0; i <= MOUSE_MOVE_STEPS; i++) {
            int mov_x = initialPos.x + (x - initialPos.x) * i / MOUSE_MOVE_STEPS;
            int mov_y = initialPos.y + (y - initialPos.y) * i / MOUSE_MOVE_STEPS;
            robot.mouseMove(mov_x, mov_y);
            robot.delay(MOUSE_MOVE_DELAY);
        }
    }

    private void validateBoardState() {
        if (minesweeperConfig.knownMines > minesweeperConfig.totalMines) {
            throw new MineLimitExceededException();
        }
        int reasonableEmptyTiles = (int) (minesweeperConfig.totalTiles - (minesweeperConfig.totalTiles * 0.5) - minesweeperConfig.knownMines);
        if (minesweeperConfig.emptyTiles > reasonableEmptyTiles) {
            throw new AbnormalEmptyTilesRatioException(reasonableEmptyTiles, minesweeperConfig.emptyTiles);
        }
        if (minesweeperConfig.emptyTiles > 0 && minesweeperConfig.openedTiles == 0) {
            throw new UnknownBoardImageException();
        }
    }

    public void setSelectedRegion(Rectangle selectedRegion) {
        this.selectedRegion = selectedRegion;
    }

    public MinesweeperConfig getMinesweeperConfigs() {
        return minesweeperConfig;
    }

    public static class UnknownBoardImageException extends RuntimeException {
        UnknownBoardImageException() {
            this("Board image is suspicious!");
        }
        UnknownBoardImageException(String s) {
            super(s);
        }
    }

    public static class MineLimitExceededException extends UnknownBoardImageException {
        MineLimitExceededException() {
            super("The number of currently known mines surpasses the anticipated total number of mines.");
        }
    }

    public static class AbnormalEmptyTilesRatioException extends UnknownBoardImageException {
        AbnormalEmptyTilesRatioException(int expected, int actual) {
            super("There are an unexpectedly high number of empty tiles. Expected: " + expected + ", Actual: " + actual);
        }
    }
}
