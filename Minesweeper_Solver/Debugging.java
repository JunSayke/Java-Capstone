package Minesweeper_Solver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Debugging {
    public static void main(String[] args) throws AWTException, IOException {

        // THIS IS HOW TO TAKE A SCREENSHOT
/*
        try {
            Robot robot = new Robot();
            BufferedImage img = robot.createScreenCapture(new Rectangle(0, 0, 500, 500));
            ImageIO.write(img, "png", new File("screenshot.png"));
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
*/
        // CONVERSATION FROM RGB int TO standard RGB
/*
        Color c = new Color(-12105399, true);
        System.out.println(c.getRed());
        System.out.println(c.getGreen());
        System.out.println(c.getBlue());
        System.out.println(c.getAlpha());
*/
        // GET MOUSE COORDINATES
/*
        int x = MouseInfo.getPointerInfo().getLocation().x;
        int y = MouseInfo.getPointerInfo().getLocation().y;
        while (true) {
            if (x != MouseInfo.getPointerInfo().getLocation().x || y != MouseInfo.getPointerInfo().getLocation().y) {
                System.out.println("(" + MouseInfo.getPointerInfo().getLocation().x + ", " + MouseInfo.getPointerInfo().getLocation().y + ")");
                x = MouseInfo.getPointerInfo().getLocation().x;
                y = MouseInfo.getPointerInfo().getLocation().y;
            }
        }
*/
    }
}
