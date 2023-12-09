package src.data;

public enum Pixels {
    BLACK(-16777216),
    WHITE(-1),
    RED(-65536),
    BLUE(-16776961),
    DARK_BLUE(-16774527),
    GREEN(-16744448);
    // TODO: ADD MORE PIXELS
    private final int value;

    Pixels(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
