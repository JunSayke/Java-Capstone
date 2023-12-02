package Minesweeper_Solver;

import Minesweeper_Solver.analyzer.factory.CharPoint;

public class Tile {
    private final int x;
    private final int y;
    private final Block state;
    private final double probability;

    public Tile(int x, int y, Block state) {
        this(x, y, state, 0);
    }

    public Tile(int x, int y, Block state, double probability) {
        this.x = x;
        this.y = y;
        this.state = state;
        this.probability = probability;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getProbability() {
        return probability;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + " '" + state + "')";
    }

    public Block getState() {
        return state;
    }
}
