package Minesweeper_Solver;

import Minesweeper_Solver.analyzer.factory.AbstractAnalyze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver extends AbstractAnalyze<Tile> {
    private final Tile[][] board;
    private final int width;
    private final int height;
    private final int remainingMines;

    public Solver(Block[][] board, int remainingMines) {
        this.width = board[0].length;
        this.height = board.length;
        this.board = new Tile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.board[x][y] = new Tile(x, y, board[x][y]);
            }
        }

        this.remainingMines = remainingMines;
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
        return isClicked(field) && !isDiscoveredMine(field);
    }

    @Override
    protected int getRemainingMinesCount() {
        return remainingMines;
    }

    @Override
    protected List<Tile> getAllUnclickedFields() {
        List<Tile> tiles = new ArrayList<>();
        for (Tile[] row : board) {
            for (Tile tile : row) {
                if (!isClicked(tile))
                    tiles.add(tile);
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
        return field.getState().getValue();
    }

    @Override
    protected List<Tile> getNeighbors(Tile field) {
        List<Tile> neighbors = new ArrayList<>();
        int x = field.getX();
        int y = field.getY();
        for (int xx = x - 1; xx <= x + 1; xx++) {
            for (int yy = y - 1; yy <= y + 1; yy++) {
                if (xx == x && yy == y)
                    continue;
                if (xx < 0 || yy < 0)
                    continue;
                if (xx >= width || yy >= height)
                    continue;
                neighbors.add(this.board[xx][yy]);
            }
        }

        return neighbors;
    }

    @Override
    protected boolean isClicked(Tile neighbor) {
        return neighbor.getState() != Block.CLOSED;
    }
}
