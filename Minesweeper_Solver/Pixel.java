package Minesweeper_Solver;

public enum Pixel {
    BLACK(-16777216),
    WHITE(-1),
    RED(-65536),
    BLUE(-16776961),
    DARK_BLUE(-16774527),
    GREEN(-16744448);

    private final int value;

    Pixel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
