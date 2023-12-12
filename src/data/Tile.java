package src.data;

import src.data.enums.Block;

public class Tile {
    private final int x;
    private final int y;
    private Block state;
    private double probability;

    public Tile(int x, int y, Block state) {
        this.x = x;
        this.y = y;
        this.state = state;
        this.probability = 0;
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

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + " '" + state + "')";
    }

    public Block getState() {
        return state;
    }
}
