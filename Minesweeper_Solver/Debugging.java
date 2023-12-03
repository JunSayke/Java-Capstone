package Minesweeper_Solver;

import Minesweeper_Solver.analyzer.AnalyzeResult;
import Minesweeper_Solver.analyzer.detail.DetailedResults;
import Minesweeper_Solver.analyzer.detail.ProbabilityKnowledge;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/*   mnsw.pro
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
        screenshot(new Rectangle(223, 272, 512, 512), "screenshot");
        BufferedImage image = ImageIO.read(new File("screenshot.png"));

        Tile[][] board = scanBoardImage(image, 16, 16);
        Tile[][] solveBoard = solveBoard(board, 40);
        displayBoard(solveBoard, true);
    }

    // AUXILIARY FUNCTIONS

    // THIS IS HOW TO DISPLAY THE LAYOUT OF THE BOARD
    public static void displayBoard(Tile[][] board) {
        displayBoard(board, false);
    }

    public static void displayBoard(Tile[][] board, boolean locateMine) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                Tile cur = board[row][col];
                if (locateMine) {
                    double prob = cur.getProbability();
                    if (prob > 0.5) {
                        System.out.print("!  ");
                        continue;
                    }
                    if (prob > 0.3){
                        System.out.print("?  ");
                        continue;
                    }
                }
                System.out.print(cur.getState().getValue() + "  ");
            }
            System.out.println();
        }
    }

    // THIS IS HOW TO DUPLICATE AN EXTERNAL MINESWEEPER GAME
    public static Tile[][] scanBoardImage(BufferedImage image, int rows, int cols) {
        Tile[][] board = new Tile[rows][cols];
        int width = image.getWidth();
        int height = image.getHeight();
        int side = width / rows;
        int offset = 5;
        for (int row = 0; row < height; row += side) {
            for (int col = 0; col < width; col += side) {
                BufferedImage crop = cropImage(image, col, row, side - offset, side - offset);
                int x = row / side;
                int y = col / side;
                board[x][y] = new Tile(x, y, checkState(crop));
            }
        }
        return board;
    }

    // THIS IS HOW TO DISPLAY EACH TILES PROBABILITY
    public static void displayProbability(Tile[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                System.out.printf("%.2f ", board[row][col].getProbability());
            }
            System.out.println();
        }
    }

    // THIS IS HOW TO CHECK EACH TILES PROBABILITY
    public static Tile[][] solveBoard(Tile[][] board, int mines) {
        Tile[][] solveBoard = board.clone();
        Solver analyze = new Solver(board, mines);
        AnalyzeResult<Tile> results = analyze.solve();
        DetailedResults<Tile> detail = results.analyzeDetailed(analyze);

        for (ProbabilityKnowledge<Tile> ee : detail.getProxies()) {
            Tile cur = ee.getField();
            int x = cur.getX();
            int y = cur.getY();
            double prob = ee.getMineProbability();
            board[x][y].setProbability(prob);
        }
        return solveBoard;
    }

    // THIS IS HOW TO CHECK FOR A TILE STATE/CONTENT
    public static Block checkState(BufferedImage image) {
        Point foundWhite = searchPixel(image, Pixel.WHITE.getValue());
        Point foundBlack = searchPixel(image, Pixel.BLACK.getValue());
        Point foundRed = searchPixel(image, Pixel.RED.getValue());

        if (foundRed != null && foundBlack != null && foundWhite != null) {
            return Block.FLAG;
        }
        if (foundBlack != null) {
            return Block.MINE;
        }
        if (foundWhite != null) {
            return Block.CLOSED;
        }
        if (foundRed != null) {
            return Block.THREE;
        }
        if (searchPixel(image, Pixel.BLUE.getValue()) != null) {
            return Block.ONE;
        }
        if (searchPixel(image, Pixel.GREEN.getValue()) != null) {
            return Block.TWO;
        }
        if (searchPixel(image, Pixel.DARK_BLUE.getValue()) != null) {
            return Block.FOUR;
        }
        return Block.EMPTY;
    }

    // THIS IS HOW TO COMPARE TWO IMAGES BASED ON THEIR PIXELS
    public static boolean compareImage(BufferedImage image, BufferedImage target) {
        byte[] imagePixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        byte[] targetPixels = ((DataBufferByte) target.getRaster().getDataBuffer()).getData();
        return Arrays.equals(imagePixels, targetPixels);
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
                System.out.print(image.getRGB(pointX, pointY) + ", ");
            }
            System.out.println();
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
                    return new Point(pointX, pointY);
                }
            }
        }
        return null;
    }
}
