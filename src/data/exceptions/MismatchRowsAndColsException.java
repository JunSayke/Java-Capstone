package src.data.exceptions;

public class MismatchRowsAndColsException extends InvalidBoardException {
    public MismatchRowsAndColsException() {
        super("Specified board rows and columns do not match the captured board image.");
    }
}
