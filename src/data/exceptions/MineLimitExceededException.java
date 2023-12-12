package src.data.exceptions;

public class MineLimitExceededException extends InvalidBoardException {
    public MineLimitExceededException() {
        super("The number of currently known mines surpasses the anticipated total number of mines.");
    }
}