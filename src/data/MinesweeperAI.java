package src.data;

import src.data.enums.Block;
import src.data.exceptions.MineLimitExceededException;
import src.data.utils.image_analysis.BoardAnalyzer;
import src.data.utils.image_analysis.PixelTileAnalyzer;
import src.data.utils.image_analysis.TileAnalyzer;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.HashSet;
import java.util.Set;

// Facade and Bridge Structural Design Pattern
public class MinesweeperAI {
    private MinesweeperSolver minesweeperSolver;
    private MinesweeperRobot minesweeperRobot;
    private BoardAnalyzer boardAnalyzer;
    private int rows, cols, totalMines;
    private final Set<Tile> safeTiles, mineTiles;
    public static boolean SAVE_BOARD_IMAGE = true;
    public static String DIRECTORY_PATH = "src\\data\\temp\\";

    public MinesweeperAI(int rows, int cols, int totalMines) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;
        safeTiles = new HashSet<>();
        mineTiles = new HashSet<>();
    }

    public Tile[][] scanBoardImage(Rectangle selectedRegion, TileAnalyzer tileAnalyzer) throws AWTException {
        minesweeperRobot = new MinesweeperRobot(selectedRegion);
        boardAnalyzer = new BoardAnalyzer(minesweeperRobot.captureBoardImage(), tileAnalyzer, rows, cols);
        if (SAVE_BOARD_IMAGE) {
            boardAnalyzer.saveImage(DIRECTORY_PATH + "MinesweeperBoard.png");
        }
        if (boardAnalyzer.getKnownMines() > totalMines) {
            throw new MineLimitExceededException();
        }
        return boardAnalyzer.analyzeBoardImage();
    }

    public Tile[][] solveBoard(Tile[][] board, MinesweeperSolver minesweeperSolver) {
        this.minesweeperSolver = minesweeperSolver;
        Tile[][] solveBoard = minesweeperSolver.solveBoard(board, totalMines - boardAnalyzer.getKnownMines());
        minesweeperSolver.displayBoard(board);
        minesweeperSolver.displayProbability(board);
        updateSafeAndMineTiles(board);
        return solveBoard;
    }

    public boolean isSolved() {
        return boardAnalyzer.getFlaggedMines() == totalMines;
    }

    public boolean isGameOver() {
        return boardAnalyzer.getKnownMines() - boardAnalyzer.getFlaggedMines() != 0;
    }

    private void updateSafeAndMineTiles(Tile[][] board) {
        resetSafeAndMineTiles();
        for (Tile[] rows : board) {
            for (Tile tile : rows) {
                if (tile.getProbability() <= 0 && tile.getState() == Block.CLOSED) {
                    safeTiles.add(tile);
                } else if (tile.getProbability() >= 0.9 && !isMine(tile)) {
                    mineTiles.add(tile);
                }
            }
        }
    }

    private void resetSafeAndMineTiles() {
        safeTiles.clear();
        mineTiles.clear();
    }

    private boolean isMine(Tile tile) {
        return tile.getState() == Block.MINE || tile.getState() == Block.FLAG;
    }

    public void clickSafeTiles(boolean toggleAll) {
        for (Tile tile : safeTiles) {
            if (!toggleAll) {
                safeTiles.remove(tile);
                return;
            }
            minesweeperRobot.clickTile(tile, boardAnalyzer.getTileHeight(), boardAnalyzer.getTileWidth(), InputEvent.BUTTON1_DOWN_MASK);
        }
    }

    public void clickMineTiles(boolean toggleAll) {
        for (Tile tile : mineTiles) {
            if (!toggleAll) {
                mineTiles.remove(tile);
                return;
            }
            minesweeperRobot.clickTile(tile, boardAnalyzer.getTileHeight(), boardAnalyzer.getTileWidth(), InputEvent.BUTTON3_DOWN_MASK);
        }
    }

    public void setMouseMoveSteps(int steps) {
        MinesweeperRobot.MOUSE_MOVE_STEPS = steps;
    }

    public void setMouseMoveDelay(int delay) {
        MinesweeperRobot.MOUSE_MOVE_DELAY = delay;
    }

    public void setPixelTolerance(int tolerance) {
        PixelTileAnalyzer.PIXEL_TOLERANCE = tolerance;
    }

    public void setTileOffset(int offset) {
        BoardAnalyzer.TILE_OFFSET = offset;
    }

    public void setSaveBoardImage(boolean toggle) {
        SAVE_BOARD_IMAGE = toggle;
    }

    public void setSaveTileImage(boolean toggle) {
        BoardAnalyzer.SAVE_TILE_IMAGE = toggle;
    }

    public void setDirectoryPath(String directoryPath) {
        DIRECTORY_PATH = directoryPath;
        BoardAnalyzer.DIRECTORY_PATH = directoryPath;
    }

    public int getKnownMines() {
        return boardAnalyzer.getKnownMines();
    }

    public int getFlaggedMines() {
        return boardAnalyzer.getFlaggedMines();
    }

    public int getOpenedTiles() {
        return boardAnalyzer.getOpenedTiles();
    }

    public int getEmptyTiles() {
        return boardAnalyzer.getEmptyTiles();
    }

    public MinesweeperRobot getMinesweeperRobot() {
        return minesweeperRobot;
    }

    public MinesweeperSolver getMinesweeperSolver() {
        return minesweeperSolver;
    }

    public BoardAnalyzer getBoardAnalyzer() {
        return boardAnalyzer;
    }

    public Set<Tile> getSafeTiles() {
        return safeTiles;
    }

    public Set<Tile> getMineTiles() {
        return mineTiles;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getTotalMines() {
        return totalMines;
    }

    public void setTotalMines(int totalMines) {
        this.totalMines = totalMines;
    }

    @Override
    public String toString() {
        return "MinesweeperAI{" +
                "minesweeperSolver=" + minesweeperSolver +
                ", minesweeperRobot=" + minesweeperRobot +
                ", boardAnalyzer=" + boardAnalyzer +
                ", rows=" + rows +
                ", cols=" + cols +
                ", totalMines=" + totalMines +
                '}';
    }
}
