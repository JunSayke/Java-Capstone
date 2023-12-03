package Minesweeper_Solver;

import Minesweeper_Solver.analyzer.factory.AbstractAnalyze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver extends AbstractAnalyze<Tile> {
    private final Tile[][] board;
    private final int rows;
    private final int cols;
    private final int hiddenMines;

    public Solver(Tile[][] board, int hiddenMines) {
        this.rows = board[0].length;
        this.cols = board.length;
        this.board = board;

        this.hiddenMines = hiddenMines;
        this.createRules(getAllPoints());
    }

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
                if (row == x && col == y)
                    continue;
                if (row < 0 || col < 0)
                    continue;
                if (row >= rows || col >= cols)
                    continue;
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
}