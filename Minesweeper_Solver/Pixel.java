package Minesweeper_Solver;

public enum Pixel {
    BLOCK_MINE_EXPLODED(-3),
    BLOCK_CLOSED(-1),
    BLOCK_FLAG(-16777216),
    BLOCK_ONE(-16639745),
    BLOCK_TWO(-16613367),
    BLOCK_THREE(-65536),
    BLOCK_FOUR(-16774272);

    private final int value;

    Pixel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
