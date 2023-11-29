package Minesweeper_Solver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Robot robot = new Robot();
            BufferedImage img = robot.createScreenCapture(new Rectangle(0, 0, 500, 500));
            ImageIO.write(img, "png", new File("screenshot.png"));
        } catch (AWTException | IOException e) {
            System.err.println(e.getMessage());
        }
        // TODO: Duplicate minesweeper board layout internally


    }
}

