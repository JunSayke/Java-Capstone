package src.data;

import java.awt.*;
import java.awt.event.InputEvent;

public class MinesweeperBot {
    private final Robot robot;
    private Rectangle screenRegion;
    private MinesweeperSolver minesweeperSolver;
    private Tile[][] board;
    private int rows, cols, tileSide, totalMines, knownMines;

    public MinesweeperBot(Rectangle screenRegion, MinesweeperSolver minesweeperSolver) throws AWTException {
        this(screenRegion, minesweeperSolver, 16, 16, 40);
    }

    public MinesweeperBot(Rectangle screenRegion, MinesweeperSolver minesweeperSolver, int rows, int cols, int totalMines) throws AWTException {
        this.screenRegion = screenRegion;
        this.minesweeperSolver = minesweeperSolver;
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;
        board = new Tile[rows][cols];
        robot = new Robot();
    }

    public void calculateProbabilities() {
        captureBoardImage();
        int hiddenMines = totalMines - knownMines;
        minesweeperSolver.solveBoard(board, hiddenMines);
        minesweeperSolver.displayBoard(board);
        minesweeperSolver.displayProbability(board);
    }

    private void captureBoardImage() {
        BoardAnalyzer boardAnalyzer = new BoardAnalyzer(robot.createScreenCapture(screenRegion), rows, cols);
        boardAnalyzer.saveImage("src\\data\\temp\\MinesweeperBoard.png");
        boardAnalyzer.setImageTolerance(20).setTileOffset(1).setSaveTiles(true);
        board = boardAnalyzer.scanBoardImage();
        knownMines = boardAnalyzer.getKnownMines();
        tileSide = boardAnalyzer.getTileSide();
    }

    // COULD POSSIBLY THROW AN EXCEPTION IF BOARD DOES NOT CONTAIN A TILE. CAN BE OPTIMIZED
    public void automateClicks() {
        for (Tile[] rows : board) {
            for (Tile col : rows) {
                double prob = col.getProbability();
                if ((prob == 0 || prob == 1) && col.getState() == Block.CLOSED) {
                    int x = col.getY() * tileSide + (int) screenRegion.getX() + tileSide / 2;
                    int y = col.getX() * tileSide + (int) screenRegion.getY() + tileSide / 2;

                    moveMouse(x, y);

                    int mouseButton = (prob == 0) ? InputEvent.BUTTON1_DOWN_MASK : InputEvent.BUTTON3_DOWN_MASK;
                    robot.mousePress(mouseButton);
                    robot.mouseRelease(mouseButton);
                }
            }
        }
    }

    // MOVE SLOWLY
    public void moveMouse(int x, int y) {
        Point initialPos = MouseInfo.getPointerInfo().getLocation();

        int start_x = (int) initialPos.getX();
        int start_y = (int) initialPos.getY();

        for (int i = 0; i <= 100; i++){
            int mov_x = start_x + (x - start_x) * i / 100;
            int mov_y = start_y + (y - start_y) * i / 100;
            robot.mouseMove(mov_x,mov_y);
            robot.delay(2);
        }
    }

    public void setScreenRegion(Rectangle screenRegion) {
        this.screenRegion = screenRegion;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
        this.rows = board.length;
        this.cols = board[0].length;
    }

    public void setTotalMines(int totalMines) {
        this.totalMines = totalMines;
    }

    public Tile[][] getBoard() {
        return board;
    }
}