package src.data;

import src.data.enums.Block;
import src.data.exceptions.MineLimitExceededException;
import src.data.utils.image_analysis.BoardAnalyzer;
import src.data.utils.image_analysis.TileAnalyzer;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.Collections;

// Facade Structural Design Pattern
public class MinesweeperAI {
    private MinesweeperSolver minesweeperSolver;
    private MinesweeperRobot minesweeperRobot;
    private BoardAnalyzer boardAnalyzer;
    private int rows, cols, totalMines;
    private Tile[][] board;
    private final ArrayList<Tile> safeTiles, mineTiles;

    public MinesweeperAI(int rows, int cols, int totalMines, MinesweeperSolver minesweeperSolver) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;
        this.minesweeperSolver = minesweeperSolver;
        safeTiles = new ArrayList<>();
        mineTiles = new ArrayList<>();
    }

    public Tile[][] scanBoardImage(Rectangle selectedRegion, TileAnalyzer tileAnalyzer) throws AWTException {
        minesweeperRobot = new MinesweeperRobot(selectedRegion);
        boardAnalyzer = new BoardAnalyzer(minesweeperRobot.captureBoardImage(), tileAnalyzer, rows, cols);
        if (boardAnalyzer.getKnownMines() > totalMines) {
            throw new MineLimitExceededException();
        }
        board = minesweeperSolver.solveBoard(boardAnalyzer.analyzeBoardImage(), totalMines - boardAnalyzer.getKnownMines());
        minesweeperSolver.displayBoard(board);
        minesweeperSolver.displayProbability(board);
        updateSafeAndMineTiles();
        return board;
    }

    private void updateSafeAndMineTiles() {
        resetSafeAndMineTiles();
        for (Tile[] rows : board) {
            for (Tile tile : rows) {
                if (tile.getProbability() == 0 && tile.getState() == Block.CLOSED) {
                    safeTiles.add(tile);
                } else if (tile.getProbability() == 1 && !isMine(tile)) {
                    mineTiles.add(tile);
                }
            }
        }
    }

    public void shuffleSafeAndMineTiles() {
        Collections.shuffle(safeTiles);
        Collections.shuffle(mineTiles);
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
            minesweeperRobot.clickTile(tile, boardAnalyzer.getTileHeight(), boardAnalyzer.getTileWidth(), InputEvent.BUTTON1_DOWN_MASK);
            if (!toggleAll) {
                return;
            }
        }
    }

    public void clickMineTiles(boolean toggleAll) {
        for (Tile tile : mineTiles) {
            minesweeperRobot.clickTile(tile, boardAnalyzer.getTileHeight(), boardAnalyzer.getTileWidth(), InputEvent.BUTTON3_DOWN_MASK);
            if (!toggleAll) {
                return;
            }
        }
    }

    public ArrayList<Tile> getSafeTiles() {
        return safeTiles;
    }

    public ArrayList<Tile> getMineTiles() {
        return mineTiles;
    }

    public MinesweeperRobot getMinesweeperRobot() {
        return minesweeperRobot;
    }

    public BoardAnalyzer getBoardAnalyzer() {
        return boardAnalyzer;
    }

    public Tile[][] getBoard() {
        return board;
    }

    public MinesweeperSolver getMinesweeperSolver() {
        return minesweeperSolver;
    }

    public void setMinesweeperSolver(MinesweeperSolver minesweeperSolver) {
        this.minesweeperSolver = minesweeperSolver;
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
        return "MinesweeperAI{}";
    }
}
