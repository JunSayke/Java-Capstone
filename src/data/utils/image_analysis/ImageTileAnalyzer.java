package src.data.utils.image_analysis;

import src.data.enums.Block;
import src.data.exceptions.InvalidBoardException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageTileAnalyzer implements TileAnalyzer {
    private ImageAnalyzer imageAnalyzer;
    private final Map<String, BufferedImage> images;
    private final static String IMAGES_PATH = "src\\data\\temp\\tiles\\";
    private final static String[] IMAGES_NAME = {
            "FLAG", "CLOSED", "EMPTY", "ONE", "TWO", "THREE", // "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT"
    };
    private static final int IMAGE_TOLERANCE = 10;

    // Singleton Structural Pattern Design
    private static ImageTileAnalyzer imageTileAnalyzer = null;
    public static ImageTileAnalyzer getInstance() throws IOException {
        if (imageTileAnalyzer == null) {
            imageTileAnalyzer = new ImageTileAnalyzer();
        }
        return imageTileAnalyzer;
    }

    private ImageTileAnalyzer() throws IOException {
        images = new HashMap<>();
        for (String filename : IMAGES_NAME) {
            images.put(filename, readImage(filename));
        }
    }

    @Override
    public Block analyzeTileImage(BufferedImage image) {
        imageAnalyzer = new ImageAnalyzer(image);
        return identifyImage();
    }

    private Block identifyImage() {
        for (Map.Entry<String, BufferedImage> tileImage : images.entrySet()) {
            if (compare(tileImage.getValue())) {
                return switch (tileImage.getKey()) {
                    case "FLAG" -> Block.FLAG;
                    case "CLOSED" -> Block.CLOSED;
                    case "EMPTY" -> Block.EMPTY;
                    case "ONE" -> Block.ONE;
                    case "TWO" -> Block.TWO;
                    case "THREE" -> Block.THREE;
//                    case "FOUR" -> Block.FOUR;
//            case "FIVE" -> Block.FIVE;
//            case "SIX" -> Block.SIX;
//            case "SEVEN" -> Block.SEVEN;
//            case "EIGHT" -> Block.EIGHT;
                    default -> Block.MINE;
                };
            }
        }
        return Block.MINE;
    }

    private boolean compare(BufferedImage image) {
        return imageAnalyzer.compareImage(image, IMAGE_TOLERANCE);
    }

    private BufferedImage readImage(String filename) throws IOException {
        return ImageIO.read(new File(IMAGES_PATH + filename + ".png"));
    }
}
