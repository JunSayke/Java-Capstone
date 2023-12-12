package src.data.exceptions;

public class AbnormalEmptyTilesRatioException extends InvalidBoardException {
    public AbnormalEmptyTilesRatioException() {
        super("There are an unexpectedly high number of empty tiles");
    }
}
