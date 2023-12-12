package src.data.exceptions;

public class InvalidBoardException extends RuntimeException {
    public InvalidBoardException() {
        this("Board image is suspicious!");
    }
    public InvalidBoardException(String s) {
        super(s);
    }
}
