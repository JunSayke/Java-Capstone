package src.data.enums;

public enum Block {
    MINE('X'),
    CLOSED('■'),
    FLAG('F'),
    EMPTY('□'),
    ONE('1'),
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8');

    private final char value;

    Block(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }
}