package src.data.utils.image_analysis;

import src.data.enums.Block;
import src.data.enums.Pixels;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PixelTileAnalyzer implements TileAnalyzer {
    private ArrayList<Pixels> foundPixels;
    private ImageAnalyzer imageAnalyzer;
    public static int PIXEL_TOLERANCE = 40;

    @Override
    public Block analyzeTileImage(BufferedImage image) {
        foundPixels = new ArrayList<>();
        imageAnalyzer = new ImageAnalyzer(image);
        identifyPixels();
        return checkTileState();
    }

    private void identifyPixels() {
        for (Pixels pixel : Pixels.values()) {
            if (imageAnalyzer.pixelSearch(pixel.getValue(), PIXEL_TOLERANCE) != null) {
                foundPixels.add(pixel);
            }
        }
    }

    private Block checkTileState() {
        // Order Matters
        if (isFlag())
            return Block.FLAG;
        if (isMine())
            return Block.MINE;
        if (isClosed())
            return Block.CLOSED;
        if (isThree())
            return Block.THREE;
        if (isOne())
            return Block.ONE;
        if (isTwo())
            return Block.TWO;
        if (isFour())
            return Block.FOUR;
        if (isFive())
            return Block.FIVE;
        if (isSix())
            return Block.SIX;
//        if (isSeven())
//            return Block.SEVEN;
//        if (isEight())
//            return Block.EIGHT;

        return Block.EMPTY;
    }

    private boolean isFlag() {
        Pixels[] expectedPixels = {Pixels.WHITE, Pixels.BLACK, Pixels.RED};
        return foundPixels.containsAll(List.of(expectedPixels));
    }

    private boolean isMine() {
        Pixels[] expectedPixels = {Pixels.BLACK};
        return foundPixels.containsAll(List.of(expectedPixels));
    }

    private boolean isClosed() {
        Pixels[] expectedPixels = {Pixels.WHITE};
        return foundPixels.containsAll(List.of(expectedPixels));
    }

    private boolean isOne() {
        Pixels[] expectedPixels = {Pixels.BLUE};
        return foundPixels.containsAll(List.of(expectedPixels));
    }

    private boolean isTwo() {
        Pixels[] expectedPixels = {Pixels.GREEN};
        return foundPixels.containsAll(List.of(expectedPixels));
    }

    private boolean isThree() {
        Pixels[] expectedPixels = {Pixels.RED};
        return foundPixels.containsAll(List.of(expectedPixels));
    }

    private boolean isFour() {
        Pixels[] expectedPixels = {Pixels.PURPLE};
        return foundPixels.containsAll(List.of(expectedPixels));
    }

    private boolean isFive() {
        Pixels[] expectedPixels = {Pixels.MAROON};
        return foundPixels.containsAll(List.of(expectedPixels));
    }

    private boolean isSix() {
        Pixels[] expectedPixels = {Pixels.CYAN};
        return foundPixels.containsAll(List.of(expectedPixels));
    }
//    private boolean isSeven() {
//        Pixels[] expectedPixels = {Pixels.MAROON};
//        return foundPixels.containsAll(List.of(expectedPixels));
//    }
//
//    private boolean isEight() {
//        Pixels[] expectedPixels = {Pixels.CYAN};
//        return foundPixels.containsAll(List.of(expectedPixels));
//    }
}
