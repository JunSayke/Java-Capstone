package src.data.exceptions;

import javax.swing.*;

public class MineLimitExceededException extends InvalidBoardException {
    public MineLimitExceededException() {
        super("The number of currently known mines surpasses the anticipated total number of mines.");
        JOptionPane.showMessageDialog(null,"The number of currently known mines surpasses the anticipated total number of mines.");
    }
}