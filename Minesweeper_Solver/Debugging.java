package Minesweeper_Solver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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
 *   5. Append each cell to the 2d array until we are finished duplicating all cells
 *     from the minesweeper game board itself.
 */

public class Debugging {
    public static void main(String[] args) throws IOException, AWTException {
        // SAMPLE USE CASE
        BufferedImage image = ImageIO.read(new File("Minesweeper_Solver\\imgs\\cell1.png"));
        BufferedImage image2 = ImageIO.read(new File("Minesweeper_Solver\\imgs\\cell3.png"));
        System.out.println(compareImage(image, image2));
    }

    // AUXILIARY FUNCTIONS

    // THIS IS HOW TO COMPARE TWO IMAGES BASED ON THEIR PIXELS
    public static boolean compareImage(BufferedImage image, BufferedImage target) {
        byte[] imagePixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        byte[] targetPixels = ((DataBufferByte) target.getRaster().getDataBuffer()).getData();
        return Arrays.equals(imagePixels, targetPixels);
    }

    // THIS IS HOW TO PRINT THE BOARD INTERNALLY
    public static void displayMinesweeperBoard(State[][] board) {
        System.out.println("Minesweeper Board:");
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                switch (board[y][x]) {
                    case BLOCK_EMPTY:
                        System.out.print(" □ ");
                        break;
                    case BLOCK_CLOSED:
                        System.out.print(" ■ ");
                        break;
                    case BLOCK_ONE:
                        System.out.print(" 1 ");
                        break;
                    case BLOCK_TWO:
                        System.out.print(" 2 ");
                        break;
                    case BLOCK_THREE:
                        System.out.print(" 3 ");
                        break;
                    case BLOCK_FOUR:
                        System.out.print(" 4 ");
                        break;
                    case BLOCK_FIVE:
                        System.out.print(" 5 ");
                        break;
                    case BLOCK_SIX:
                        System.out.print(" 6 ");
                        break;
                    case BLOCK_SEVEN:
                        System.out.print(" 7 ");
                        break;
                    case BLOCK_EIGHT:
                        System.out.print(" 8 ");
                        break;
                    case BLOCK_FLAG:
                        System.out.print(" F ");
                        break;
                    case BLOCK_MINE_EXPLODED:
                        System.out.print(" X ");
                        break;
                }
            }
            System.out.println();
        }
    }

    // THIS IS HOW TO CROP AN IMAGE FROM AN EXISTING IMAGE
    private static BufferedImage cropImage(BufferedImage image, int x, int y, int w, int h) {
        if (x + w > image.getWidth()) {
            x = image.getWidth() - w;
        }
        if (y + h > image.getHeight()) {
            y = image.getHeight() - h;
        }
       return image.getSubimage(x, y, w, h);
    }

    // THIS IS HOW TO TAKE A SCREENSHOT
    private static void screenshot(Rectangle rect, String pathname) {
        screenshot(rect, pathname, "png");
    }

    private static void screenshot(Rectangle rect, String pathname, String extension) {
        try {
            Robot robot = new Robot();
            BufferedImage img = robot.createScreenCapture(rect);
            ImageIO.write(img, extension, new File(pathname + "." + extension));
        } catch (AWTException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    // THIS IS HOW TO DISPLAY AN RGB INT IN A STANDARD RGB FORM
    private static Color displayRGBInt(int RGBInt) {
        Color color = new Color(RGBInt, true);
        System.out.println("Red: " + color.getRed() + ", Green: " + color.getGreen() + ", Blue: " + color.getBlue() + ", Opacity: " + color.getAlpha());
        return color;
    }

    // THIS IS HOW TO DISPLAY THE MOUSE POINTER COORDINATES RELATIVE TO THE SCREEN COORDINATES
    private static Point displayMouseCoordinates() {
        Point coordinates = MouseInfo.getPointerInfo().getLocation();
        System.out.println("Mouse pointer coordinates: (" + coordinates.getX() + ", " + coordinates.getY() + ")");
        return coordinates;
    }

    // THIS IS HOW TO ENUMERATE ALL PIXEL FROM AN IMAGE IN THE FORM OF RGB INT
    private static void enumeratePixels(BufferedImage image) {
        enumeratePixels(image, 0, 0);
    }

    private static void enumeratePixels(BufferedImage image, int x, int y) {
        enumeratePixels(image, x, y, image.getWidth() - x, image.getHeight() - y);
    }

    private static void enumeratePixels(BufferedImage image, int x, int y, int width, int height) {
        if (x + width > image.getWidth()) {
            x = image.getWidth() - width;
        }
        if (y + height > image.getHeight()) {
            y = image.getHeight() - height;
        }
        for (int pointY = y; pointY < y + height; pointY++) {
            for (int pointX = x; pointX < x + width; pointX++) {
                if (x < image.getWidth() - 1) {
                    System.out.print(image.getRGB(pointX, pointY) + ", ");
                } else {
                    System.out.println(image.getRGB(pointX, pointY));
                }
            }
        }
    }

    // THIS IS HOW TO SEARCH FOR A SPECIFIC PIXEL FROM AN IMAGE
    private static Point searchPixel(BufferedImage image, int targetColor) {
        return searchPixel(image, targetColor, 0, 0);
    }

    private static Point searchPixel(BufferedImage image, int targetColor, int x, int y) {
        return searchPixel(image, targetColor, x, y, image.getWidth() - x, image.getHeight() - y);
    }

    private static Point searchPixel(BufferedImage image, int targetColor, int x, int y, int width, int height) {
        if (x + width > image.getWidth()) {
            x = image.getWidth() - width;
        }
        if (y + height > image.getHeight()) {
            y = image.getHeight() - height;
        }
        for (int pointY = y; pointY < y + height; pointY++) {
            for (int pointX = x; pointX < x + width; pointX++) {
                if (image.getRGB(pointX, pointY) == targetColor) {
                    System.out.println("Pixel found at coordinates: (" + pointX + ", " + pointY + ")");
                    return new Point(pointX, pointY);
                }
            }
        }
        System.out.println("Pixel cannot be found on the target image");
        return null;
    }
}
