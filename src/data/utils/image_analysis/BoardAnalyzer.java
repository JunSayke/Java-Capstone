package src.data.utils.image_analysis;

import src.data.enums.Block;
import src.data.Tile;
import src.data.exceptions.AbnormalEmptyTilesRatioException;
import src.data.exceptions.InvalidBoardException;
import src.data.exceptions.MismatchRowsAndColsException;

import java.awt.image.BufferedImage;

public class BoardAnalyzer extends ImageAnalyzer {
    private final TileAnalyzer tileAnalyzer;
    private final Tile[][] board;
    private final int rows, cols, tileHeight, tileWidth;
    private int knownMines, emptyTiles, openedTiles, tileCounter;

    private static final int TILE_OFFSET = 1;
    private static final double EMPTY_TILES_RATIO = 0.5;

    public BoardAnalyzer(BufferedImage image, TileAnalyzer tileAnalyzer, int rows, int cols) {
        super(image);
        saveImage("src\\data\\temp\\MinesweeperBoard.png");
        this.tileAnalyzer = tileAnalyzer;
        this.rows = rows;
        this.cols = cols;
        tileHeight = getHeight() / rows;
        tileWidth = getWidth() / cols;
        board = new Tile[rows][cols];
        initTileStatistics();
    }

    private void initTileStatistics() {
        knownMines = emptyTiles = openedTiles = tileCounter = 0;
    }

    public Tile[][] analyzeBoardImage() {
        if (rows != getHeight() / tileHeight || cols != getWidth() / tileWidth) {
            throw new MismatchRowsAndColsException();
        }
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                analyzeTile(row, col);
            }
        }
        int totalTiles = rows * cols;
        int reasonableEmptyTiles = (int) (totalTiles - (totalTiles * EMPTY_TILES_RATIO) - knownMines);
        if (emptyTiles > reasonableEmptyTiles) {
            throw new AbnormalEmptyTilesRatioException();
        }
        if (emptyTiles > 0 && openedTiles == 0) {
            throw new InvalidBoardException();
        }
        return board;
    }

    private void analyzeTile(int row, int col) {
        Block state = tileAnalyzer.analyzeTileImage(cropTileImage(col, row));
        updateTileStatistics(state);
        board[row][col] = new Tile(row, col, state);
    }

    private BufferedImage cropTileImage(int x, int y) {
        BufferedImage crop = cropImage(x * (tileWidth + TILE_OFFSET),
                y * (tileHeight + TILE_OFFSET),
                tileWidth - TILE_OFFSET,
                tileHeight - TILE_OFFSET);
        saveImage(crop, "src\\data\\temp\\tile" + (++tileCounter) + ".png");
        return crop;
    }

    private void updateTileStatistics(Block state) {
        if (state == Block.FLAG || state == Block.MINE) {
            knownMines++;
        }
        if (state == Block.EMPTY) {
            emptyTiles++;
        }
        if (Character.isDigit(state.getValue())) {
            openedTiles++;
        }
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getKnownMines() {
        return knownMines;
    }

    public int getEmptyTiles() {
        return emptyTiles;
    }

    public int getOpenedTiles() {
        return openedTiles;
    }
}
