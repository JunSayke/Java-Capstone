package src.data;

import java.awt.*;

public class MinesweeperBot {
    private final Robot robot;
    private Rectangle screenRegion;
    private MinesweeperSolver minesweeperSolver;
    private Tile[][] board;
    private int rows, cols, totalMines, knownMines;

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

    public void run() {
        captureBoardImage();
        minesweeperSolver.solveBoard(board, totalMines - knownMines);
//        minesweeperSolver.displayBoard(board);
//        minesweeperSolver.displayProbability(board);
    }

    private void captureBoardImage() {
        BoardAnalyzer boardAnalyzer = new BoardAnalyzer(robot.createScreenCapture(screenRegion), rows, cols);
        boardAnalyzer.saveImage("src\\data\\temp\\MinesweeperBoard.png");
        boardAnalyzer.setImageTolerance(15).setTileOffset(1);
        board = boardAnalyzer.scanBoardImage();
        knownMines = boardAnalyzer.getKnownMines();
    }

    // TODO: GET ALL EDGE TILES

    // TODO: AUTOMATE CLICKS

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