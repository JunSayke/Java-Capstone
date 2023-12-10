package src.data;

import java.awt.image.BufferedImage;

public class BoardAnalyzer extends ImageAnalyzer {
    private final MinesweeperConfigs minesweeperConfigs;
    private int tileOffset, imageTolerance;
    private boolean saveTiles;
    private static final String TILE_SAVE_PATH = "src\\data\\temp\\tile%s.png";

    public BoardAnalyzer(BufferedImage image, MinesweeperConfigs minesweeperConfigs) {
        super(image);
        this.minesweeperConfigs = minesweeperConfigs;
        tileOffset = imageTolerance = 0;
        saveTiles = false;
    }

    public void analyzeBoard() {
        minesweeperConfigs.tileSide = getWidth() / minesweeperConfigs.cols;
        validateBoardDimensions();
        resetTileStatistics();
        int i = 1;
        for (int row = 0; row < minesweeperConfigs.rows; row++) {
            for (int col = 0; col < minesweeperConfigs.cols; col++, i++) {
                if (saveTiles) {
                    BufferedImage crop = cropTileImage(col, row);
                    saveImage(crop, String.format(TILE_SAVE_PATH, i));
                }
                analyzeTile(row, col);
            }
        }
    }

    private void resetTileStatistics() {
        minesweeperConfigs.knownMines = 0;
        minesweeperConfigs.emptyTiles = 0;
        minesweeperConfigs.openedTiles = 0;
    }

    private void validateBoardDimensions() {
        if (minesweeperConfigs.rows != getHeight() / minesweeperConfigs.tileSide || minesweeperConfigs.cols != getWidth() / minesweeperConfigs.tileSide) {
            throw new MismatchRowsAndColsException();
        }
    }

    private BufferedImage cropTileImage(int col, int row) {
        return cropImage(col * (minesweeperConfigs.tileSide + tileOffset),
                row * (minesweeperConfigs.tileSide + tileOffset),
                minesweeperConfigs.tileSide - tileOffset,
                minesweeperConfigs.tileSide - tileOffset);
    }

    private void analyzeTile(int row, int col) {
        Block state = checkState(col, row);
        updateTileStatistics(state);
        minesweeperConfigs.board[row][col] = new Tile(row, col, state);
    }

    private void updateTileStatistics(Block state) {
        if (state == Block.FLAG || state == Block.MINE) {
            minesweeperConfigs.knownMines++;
        }
        if (state == Block.EMPTY) {
            minesweeperConfigs.emptyTiles++;
        }
        if (Character.isDigit(state.getValue())) {
            minesweeperConfigs.openedTiles++;
        }
    }

    private boolean checkPixel(int argb, int x, int y) {
        int xOffset = x * (minesweeperConfigs.tileSide + tileOffset);
        int yOffset = y * (minesweeperConfigs.tileSide + tileOffset);
        int width = minesweeperConfigs.tileSide - tileOffset;
        int height = minesweeperConfigs.tileSide - tileOffset;

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
