package Minesweeper_Solver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
*   The idea behind the minesweeper solver is to duplicate the game internally
*   and then calculate the probability through a simple algorithm for those
*   unopened cells. However only those near to the opened cells are calculated.
*
*   In order to duplicate the game internally we have to do the following:
*   1. Take a screenshot of the minesweeper board.
*   2. Iterate the board from upper-left corner to the lower-right corner.
*   3. For each iteration, check the state of the cell whether is it opened or not.
*     If it is opened check for its content whether it is an empty, a bomb, a one and so on.
*   4. After determining the state of the cell, assign a constant integer value to represent
*     its state.
*   5. Append each cell to the 2d array until we finished duplicating all cells from the
*     minesweeper game board.
*/

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
        // THIS IS HOW TO CONVERT AN RGB INT TO A STANDARD RGB
/*
        Color c = new Color(-12105399, true);
        System.out.println(c.getRed());
        System.out.println(c.getGreen());
        System.out.println(c.getBlue());
        System.out.println(c.getAlpha());
*/
        // THIS IS HOW TO GET THE MOUSE POINTER COORDINATES RELATIVE TO THE SCREEN COORDINATES
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
        // THIS IS HOW TO GET THE PIXELS FROM AN IMAGE IN THE FORM OF RGB INT
/*
        BufferedImage image = ImageIO.read(new File("screenshot.png"));
        // FROM LEFT TO RIGHT
        for (int i = 0; i < image.getWidth(); i++) {
            // ONLY THE TOPMOST PIXELS
            System.out.print(image.getRGB(i, 0) + ", ");
        }
*/
    }
}
