package src.data.exceptions;

import javax.swing.*;

public class MismatchRowsAndColsException extends InvalidBoardException {
    public MismatchRowsAndColsException() {
        super("Specified board rows and columns do not match the captured board image.");
        JOptionPane.showMessageDialog(null,"Specified board rows and columns do not match the captured board image.");
    }
}
