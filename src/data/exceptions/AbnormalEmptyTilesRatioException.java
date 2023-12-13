package src.data.exceptions;

import javax.swing.*;

public class AbnormalEmptyTilesRatioException extends InvalidBoardException {
    public AbnormalEmptyTilesRatioException() {
        super("There are an unexpectedly high number of empty tiles");
        JOptionPane.showMessageDialog(null,"There are an unexpectedly high number of empty tiles");
    }
}
