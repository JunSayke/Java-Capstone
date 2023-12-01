package Minesweeper_Solver;

import java.util.Arrays;

public class Board {
    private State[][] field;
    private int bombs;
    private int rows;
    private int cols;

    public Board(State[][] field, int bombs) {
        this(field, bombs, field.length, field[0].length);
    }

    public Board(State[][] field, int bombs, int rows, int cols) {
        this.field = field;
        this.bombs = bombs;
        this.rows = rows;
        this.cols = cols;
    }

    public int getRows() {
        return field.length;
    }

    public int getCols() {
        return field[0].length;
    }

    public int getBombs() {
        return bombs;
    }

    public void setBombs(int bombs) {
        this.bombs = bombs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Minesweeper Board:\n");
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                switch (field[y][x]) {
                    case BLOCK_EMPTY:
                        sb.append(" □ ");
                        break;
                    case BLOCK_CLOSED:
                        sb.append(" ■ ");
                        break;
                    case BLOCK_ONE:
                        sb.append(" 1 ");
                        break;
                    case BLOCK_TWO:
                        sb.append(" 2 ");
                        break;
                    case BLOCK_THREE:
                        sb.append(" 3 ");
                        break;
                    case BLOCK_FOUR:
                        sb.append(" 4 ");
                        break;
                    case BLOCK_FIVE:
                        sb.append(" 5 ");
                        break;
                    case BLOCK_SIX:
                        sb.append(" 6 ");
                        break;
                    case BLOCK_SEVEN:
                        sb.append(" 7 ");
                        break;
                    case BLOCK_EIGHT:
                        sb.append(" 8 ");
                        break;
                    case BLOCK_FLAG:
                        sb.append(" F ");
                        break;
                    case BLOCK_MINE_EXPLODED:
                        sb.append(" X ");
                        break;
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
