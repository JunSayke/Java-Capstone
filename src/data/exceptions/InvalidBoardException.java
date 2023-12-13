package src.data.exceptions;

import javax.swing.*;

public class InvalidBoardException extends RuntimeException {
    public InvalidBoardException() {
        this("Board image is suspicious!");
        JOptionPane.showMessageDialog(null,"Board image is suspicious!");
    }
    public InvalidBoardException(String s) {
        super(s);
    }
}
