package src.data;

import java.awt.image.BufferedImage;

public class BoardAnalyzer extends ImageAnalyzer {
    private final int rows, cols, tileSide;
    private int knownMines, tileCount, tileOffset, imageTolerance;
    private boolean saveTiles;
    public BoardAnalyzer(BufferedImage image, int rows, int cols) {
        super(image);
        this.rows = rows;
        this.cols = cols;
        tileSide = getWidth() / cols;
        knownMines = 0;
        tileCount = 0;
        // default values
        tileOffset = 0;
        imageTolerance = 0;
        saveTiles = false;
    }

    public Tile[][] scanBoardImage() {
        Tile[][] board = new Tile[rows][cols];

        if (rows != getHeight() / tileSide || cols != getWidth() / tileSide) {
            throw new MismatchRowsAndColsException();
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                tileCount++;
                Block state = checkState(col, row);
                if (saveTiles) {
                    BufferedImage crop = cropImage(col * (tileSide + tileOffset),row * (tileSide + tileOffset), tileSide - tileOffset, tileSide - tileOffset);
                    saveImage(crop,"src\\data\\temp\\tile" + tileCount + ".png");
                }
                if (state == Block.FLAG || state == Block.MINE) {
                    knownMines++;
                }
                board[row][col] = new Tile(row, col, state);
            }
        }

        return board;
    }

    private boolean pixelSearch(int argb, int x, int y) {
        return pixelSearch(argb, x * (tileSide + tileOffset), y * (tileSide + tileOffset), tileSide - tileOffset, tileSide - tileOffset, imageTolerance) != null;
    }

    private Block checkState(int x, int y) {
        boolean foundWhite = pixelSearch(Pixels.WHITE.getValue(), x, y);
        boolean foundBlack = pixelSearch(Pixels.BLACK.getValue(), x, y);
        boolean foundRed = pixelSearch(Pixels.RED.getValue(), x, y);

        if (foundRed && foundBlack && foundWhite) {
            return Block.FLAG;
        }
        if (foundBlack) {
            return Block.MINE;
        }
        if (foundWhite) {
            return Block.CLOSED;
        }
        if (foundRed) {
            return Block.THREE;
        }
        if (pixelSearch(Pixels.BLUE.getValue(), x, y)) {
            return Block.ONE;
        }
        if (pixelSearch(Pixels.GREEN.getValue(), x, y)) {
            return Block.TWO;
        }
        if (pixelSearch(Pixels.PURPLE.getValue(), x, y)) {
            return Block.FOUR;
        }
        if (pixelSearch(Pixels.MAROON.getValue(), x, y)) {
            return Block.FIVE;
        }
        // TODO: ADD MORE CASES

        return Block.EMPTY;
    }

    public BoardAnalyzer setSaveTiles(boolean saveTiles) {
        this.saveTiles = saveTiles;
        return this;
    }

    public BoardAnalyzer setTileOffset(int tileOffset) {
        this.tileOffset = tileOffset;
        return this;
    }

    public BoardAnalyzer setImageTolerance(int imageTolerance) {
        this.imageTolerance = imageTolerance;
        return this;
    }

    public int getKnownMines() {
        return knownMines;
    }

    public int getTileSide() {
        return tileSide;
    }

    public static class MismatchRowsAndColsException extends RuntimeException {
        // TODO: ADD POSSIBLE CODE
        MismatchRowsAndColsException() {
            super("Specified board rows and columns do not match the captured board image.");
        }
    }
}
