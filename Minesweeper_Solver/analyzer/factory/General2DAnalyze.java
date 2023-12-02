package Minesweeper_Solver.analyzer.factory;

import Minesweeper_Solver.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class General2DAnalyze extends AbstractAnalyze<CharPoint> {

    public static final char UNCLICKED = '?';
    public static final char HIDDEN_MINE = 'x';
    public static final char KNOWN_MINE = '!';

    private final CharPoint[][] points;
    private final int width;
    private final int height;
    private final int hiddenMines;

    public General2DAnalyze(char[][] map, int hiddenMines) {
        this.width = map[0].length;
        this.height = map.length;
        this.points = new CharPoint[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                points[x][y] = new CharPoint(x, y, map[y][x]);
            }
        }

        this.hiddenMines = hiddenMines;
        this.createRules(getAllPoints());
    }

    @Override
    protected List<CharPoint> getAllPoints() {
        List<CharPoint> point = new ArrayList<>();
        for (CharPoint[] ps : points) {
            Collections.addAll(point, ps);
        }
        return point;
    }

    @Override
    protected boolean fieldHasRule(CharPoint field) {
        return isClicked(field) && !isDiscoveredMine(field);
    }

    @Override
    protected int getRemainingMinesCount() {
        return hiddenMines;
    }

    @Override
    protected List<CharPoint> getAllUnclickedFields() {
        List<CharPoint> point = new ArrayList<>();
        for (CharPoint[] ps : points) {
            for (CharPoint p : ps) {
                if (!isClicked(p))
                    point.add(p);
            }
        }
        return point;
    }

    @Override
    protected boolean isDiscoveredMine(CharPoint neighbor) {
        return neighbor.getValue() == KNOWN_MINE;
    }

    @Override
    protected int getFieldValue(CharPoint field) {
        return Character.digit(field.getValue(), 10);
    }

    @Override
    protected List<CharPoint> getNeighbors(CharPoint field) {
        List<CharPoint> neighbors = new ArrayList<>();
        int x = field.getX();
        int y = field.getY();
        for (int xx = x - 1; xx <= x + 1; xx++) {
            for (int yy = y - 1; yy <= y + 1; yy++) {
                if (xx == x && yy == y)
                    continue;
                if (xx < 0 || yy < 0)
                    continue;
                if (xx >= width || yy >= height)
                    continue;
                neighbors.add(this.points[xx][yy]);
            }
        }

        return neighbors;
    }

    @Override
    protected boolean isClicked(CharPoint neighbor) {
        return neighbor.getValue() != '?';
    }
}
