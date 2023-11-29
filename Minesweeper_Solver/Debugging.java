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
*   5. Append each cell to the 2d array until we are finished duplicating all cells internally
*     from the minesweeper game board itself.
*/

public class Debugging {
    public static void main(String[] args) throws IOException, AWTException {
        screenshot(new Rectangle(0, 0, 10, 10));
        BufferedImage image = ImageIO.read(new File("screenshot.png"));
        enumeratePixels(image);
        displayMouseCoordinates();
        displayRGBInt(-12764367);
        searchPixel(image, -12764367);
    }

    // THIS IS HOW TO TAKE A SCREENSHOT
    private static void screenshot(Rectangle rect) {
        try {
            Robot robot = new Robot();
            BufferedImage img = robot.createScreenCapture(rect);
            ImageIO.write(img, "png", new File("screenshot.png"));
        } catch (AWTException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    // THIS IS HOW TO DISPLAY AN RGB INT IN A STANDARD RGB FORM
    private static void displayRGBInt(int RGBInt) {
        Color c = new Color(RGBInt, true);
        System.out.println("Red: " + c.getRed() + ", Green: " + c.getGreen() + ", Blue: " + c.getBlue() + ", Opacity: " + c.getAlpha());
    }

    // THIS IS HOW TO DISPLAY THE MOUSE POINTER COORDINATES RELATIVE TO THE SCREEN COORDINATES
    private static void displayMouseCoordinates() {
        Point coordinates = MouseInfo.getPointerInfo().getLocation();
        System.out.println("Mouse pointer coordinates: (" + coordinates.getX() + ", " + coordinates.getY() + ")");
    }

    // THIS IS HOW TO ENUMERATE ALL PIXEL FROM AN IMAGE IN THE FORM OF RGB INT
    private static void enumeratePixels(BufferedImage image) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (x < image.getWidth() - 1) {
                    System.out.print(image.getRGB(x, y) + ", ");
                } else {
                    System.out.println(image.getRGB(x, y));
                }
            }
        }
    }

    // THIS IS HOW TO SEARCH FOR A SPECIFIC PIXEL FROM AN IMAGE
    private static void searchPixel(BufferedImage image, int targetColor) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (image.getRGB(x, y) == targetColor) {
                    System.out.println("Pixel found at coordinates: (" + x + ", " + y + ")");
                }
            }
        }
        System.out.println("Pixel cannot be found on the target image");
    }
}
