package src.data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MinesweeperBot {
    private final Robot robot;
    private Rectangle screenRegion;
    private BufferedImage boardImage;
    private MinesweeperSolver minesweeperSolver;
    private Tile[][] board;
    private int rows, cols, totalMines, knownMines;
    public MinesweeperBot(Rectangle screenRegion, MinesweeperSolver minesweeperSolver, int rows, int cols, int totalMines) throws AWTException {
        this.screenRegion = screenRegion;
        this.minesweeperSolver = minesweeperSolver;
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;
        board = new Tile[rows][cols];
        robot = new Robot();
    }

    public void run() {
        captureBoardImage();
        scanBoardImage();
        minesweeperSolver.solveBoard(board, totalMines - knownMines);
        minesweeperSolver.displayBoard(board);
        minesweeperSolver.displayProbability(board);
    }

    public void captureBoardImage() {
        saveImage(robot.createScreenCapture(screenRegion), "src\\data\\temp\\MinesweeperBoard.png");
        try {
            boardImage = ImageIO.read(new File("src\\data\\temp\\MinesweeperBoard.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void scanBoardImage() {
        ImageAnalyzer imageAnalyzer = new ImageAnalyzer(boardImage);
        int width = boardImage.getWidth();
        int height = boardImage.getHeight();
        int side = width / cols;
        if (rows != height / side || cols != width / side) {
            throw new MismatchRowsAndColsException();
        }
        int offset = 1, i = 1;
        knownMines = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                BufferedImage crop = imageAnalyzer.cropImage(col * (side + offset), row * (side + offset), side - offset, side - offset);
//                saveImage(crop, "src\\data\\temp\\tile" + i + ".png");
//                try {
//                    crop = ImageIO.read(new File("src\\data\\temp\\tile" + i + ".png"));
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
                Block state = checkState(crop);
                if (state == Block.FLAG || state == Block.MINE) {
                    knownMines++;
                }
                board[row][col] = new Tile(row, col, state);
                i++;
            }
        }
    }

    private void saveImage(BufferedImage image, String pathname) {
        try {
            ImageIO.write(image, "png", new File(pathname));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Block checkState(BufferedImage image) {
        ImageAnalyzer imageAnalyzer = new ImageAnalyzer(image);
        int tolerance = 15;
        Point foundWhite = imageAnalyzer.pixelSearch(Pixels.WHITE.getValue(), tolerance);
        Point foundBlack = imageAnalyzer.pixelSearch(Pixels.BLACK.getValue(), tolerance);
        Point foundRed = imageAnalyzer.pixelSearch(Pixels.RED.getValue(), tolerance);

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
        if (imageAnalyzer.pixelSearch(Pixels.BLUE.getValue(), tolerance) != null) {
            return Block.ONE;
        }
        if (imageAnalyzer.pixelSearch(Pixels.GREEN.getValue(), tolerance) != null) {
            return Block.TWO;
        }
        if (imageAnalyzer.pixelSearch(Pixels.DARK_BLUE.getValue(), tolerance) != null) {
            return Block.FOUR;
        }
        // TODO: ADD MORE CASES

        return Block.EMPTY;
    }

    // TODO: GET ALL EDGE TILES

    // TODO: AUTOMATE CLICKS

    public void setScreenRegion(Rectangle screenRegion) {
        this.screenRegion = screenRegion;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
        this.rows = board.length;
        this.cols = board[0].length;
    }

    public void setTotalMines(int totalMines) {
        this.totalMines = totalMines;
    }

    public Tile[][] getBoard() {
        return board;
    }

    public BufferedImage getBoardImage() {
        return boardImage;
    }

    public static class MismatchRowsAndColsException extends RuntimeException {
        // TODO: ADD POSSIBLE CODE
    }
}