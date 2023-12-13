package src.data.solver;

import src.data.enums.Block;
import src.data.MinesweeperSolver;
import src.data.Tile;
import src.data.solver.advanced.AnalyzeResult;
import src.data.solver.advanced.detail.DetailedResults;
import src.data.solver.advanced.detail.ProbabilityKnowledge;
import src.data.solver.advanced.factory.AbstractAnalyze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdvancedAlgo extends AbstractAnalyze<Tile> implements MinesweeperSolver {
    private Tile[][] board;
    private int rows;
    private int cols;
    private int hiddenMines;

    @Override
    protected List<Tile> getAllPoints() {
        List<Tile> tiles = new ArrayList<>();
        for (Tile[] row : board) {
            Collections.addAll(tiles, row);
        }
        return tiles;
    }

    @Override
    protected boolean fieldHasRule(Tile field) {
        return !isEmpty(field) && isClicked(field) && !isDiscoveredMine(field);
    }

    @Override
    protected int getRemainingMinesCount() {
        return hiddenMines;
    }

    @Override
    protected List<Tile> getAllUnclickedFields() {
        List<Tile> tiles = new ArrayList<>();
        for (Tile[] row : board) {
            for (Tile col : row) {
                if (!isClicked(col))
                    tiles.add(col);
            }
        }
        return tiles;
    }

    @Override
    protected boolean isDiscoveredMine(Tile neighbor) {
        return neighbor.getState() == Block.FLAG;
    }

    @Override
    protected int getFieldValue(Tile field) {
        return Character.digit(field.getState().getValue(), 10);
    }

    @Override
    protected List<Tile> getNeighbors(Tile field) {
        List<Tile> neighbors = new ArrayList<>();
        int x = field.getX();
        int y = field.getY();
        for (int row = x - 1; row <= x + 1; row++) {
            for (int col = y - 1; col <= y + 1; col++) {
                if ( (row == x && col == y) || row < 0 || col < 0 || row >= rows || col >= cols) {
                    continue;
                }
                neighbors.add(this.board[row][col]);
            }
        }
        return neighbors;
    }

    private boolean isEmpty(Tile field) {
        return field.getState() == Block.EMPTY;
    }

    @Override
    protected boolean isClicked(Tile neighbor) {
        return neighbor.getState() != Block.CLOSED;
    }


    @Override
    public Tile[][] solveBoard(Tile[][] board, int hiddenMines) {
        this.board = board.clone();
        this.hiddenMines = hiddenMines;
        rows = board.length;
        cols = board[0].length;

        createRules();
        AnalyzeResult<Tile> results = solve();
        DetailedResults<Tile> detail = results.analyzeDetailed(this);

        for (ProbabilityKnowledge<Tile> ee : detail.getProxies()) {
            Tile cur = ee.getField();
            board[cur.getX()][cur.getY()].setProbability(ee.getMineProbability());
        }
        return this.board;
    }
}