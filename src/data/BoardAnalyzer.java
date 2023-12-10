package src.data;

import java.awt.image.BufferedImage;

public class BoardAnalyzer extends ImageAnalyzer {
    private final MinesweeperConfig minesweeperConfig;
    private int tileOffset, imageTolerance;
    private boolean saveTiles;
    private static final String TILE_SAVE_PATH = "src\\data\\temp\\tile%s.png";

    public BoardAnalyzer(BufferedImage image, MinesweeperConfig minesweeperConfig) {
        super(image);
        this.minesweeperConfig = minesweeperConfig;
        tileOffset = imageTolerance = 0;
        saveTiles = false;
    }

    public void analyzeBoard() {
        minesweeperConfig.tileSide = getWidth() / minesweeperConfig.cols;
        validateBoardDimensions();
        resetTileStatistics();
        int i = 1;
        for (int row = 0; row < minesweeperConfig.rows; row++) {
            for (int col = 0; col < minesweeperConfig.cols; col++, i++) {
                if (saveTiles) {
                    BufferedImage crop = cropTileImage(col, row);
                    saveImage(crop, String.format(TILE_SAVE_PATH, i));
                }
                analyzeTile(row, col);
            }
        }
    }

    private void resetTileStatistics() {
        minesweeperConfig.knownMines = 0;
        minesweeperConfig.emptyTiles = 0;
        minesweeperConfig.openedTiles = 0;
    }

    private void validateBoardDimensions() {
        if (minesweeperConfig.rows != getHeight() / minesweeperConfig.tileSide || minesweeperConfig.cols != getWidth() / minesweeperConfig.tileSide) {
            throw new MismatchRowsAndColsException();
        }
    }

    private BufferedImage cropTileImage(int col, int row) {
        return cropImage(col * (minesweeperConfig.tileSide + tileOffset),
                row * (minesweeperConfig.tileSide + tileOffset),
                minesweeperConfig.tileSide - tileOffset,
                minesweeperConfig.tileSide - tileOffset);
    }

    private void analyzeTile(int row, int col) {
        Block state = checkState(col, row);
        updateTileStatistics(state);
        minesweeperConfig.board[row][col] = new Tile(row, col, state);
    }

    private void updateTileStatistics(Block state) {
        if (state == Block.FLAG || state == Block.MINE) {
            minesweeperConfig.knownMines++;
        }
        if (state == Block.EMPTY) {
            minesweeperConfig.emptyTiles++;
        }
        if (Character.isDigit(state.getValue())) {
            minesweeperConfig.openedTiles++;
        }
    }

    private boolean checkPixel(int argb, int x, int y) {
        int xOffset = x * (minesweeperConfig.tileSide + tileOffset);
        int yOffset = y * (minesweeperConfig.tileSide + tileOffset);
        int width = minesweeperConfig.tileSide - tileOffset;
        int height = minesweeperConfig.tileSide - tileOffset;

        return pixelSearch(argb, xOffset, yOffset, width, height, imageTolerance) != null;
    }

    private Block checkState(int x, int y) {
        boolean foundWhite = checkPixel(Pixels.WHITE.getValue(), x, y);
        boolean foundBlack = checkPixel(Pixels.BLACK.getValue(), x, y);
        boolean foundRed = checkPixel(Pixels.RED.getValue(), x, y);

        if (foundRed && foundBlack && foundWhite) {
            return Block.FLAG;
        }
        if (foundBlack) {
            return Block.MINE;
        }
        if (foundWhite) {
            return Block.CLOSED;
        }
        if (checkPixel(Pixels.RED.getValue(), x, y)) {
            return Block.THREE;
        }
        if (checkPixel(Pixels.BLUE.getValue(), x, y)) {
            return Block.ONE;
        }
        if (checkPixel(Pixels.GREEN.getValue(), x, y)) {
            return Block.TWO;
        }
        if (checkPixel(Pixels.PURPLE.getValue(), x, y)) {
            return Block.FOUR;
        }
        if (checkPixel(Pixels.MAROON.getValue(), x, y)) {
            return Block.FIVE;
        }
        if (checkPixel(Pixels.CYAN.getValue(), x, y)) {
            return Block.SIX;
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

    private static class MismatchRowsAndColsException extends RuntimeException {
        MismatchRowsAndColsException() {
            super("Specified board rows and columns do not match the captured board image.");
        }
    }
}
