package src.data;

public class MinesweeperConfigs {
    public final Tile[][] board;
    public final int rows;
    public final int cols;
    public final int totalMines;
    public final int totalTiles;
    public int tileSide, knownMines, emptyTiles, openedTiles;

    public MinesweeperConfigs(Tile[][] board, int totalMines) {
        this.board = board;
        this.totalMines = totalMines;
        rows = board.length;
        cols = board[0].length;
        totalTiles = rows * cols;
        tileSide = knownMines = emptyTiles = openedTiles = 0;
    }

    @Override
    public String toString() {
        return "MinesweeperConfigs{" +
                "rows=" + rows +
                ", cols=" + cols +
                ", totalMines=" + totalMines +
                ", totalTiles=" + totalTiles +
                ", tileSide=" + tileSide +
                ", knownMines=" + knownMines +
                ", emptyTiles=" + emptyTiles +
                ", openedTiles=" + openedTiles +
                '}';
    }
}
