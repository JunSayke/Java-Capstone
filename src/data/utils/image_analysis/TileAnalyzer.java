package src.data.utils.image_analysis;

import src.data.enums.Block;
import java.awt.image.BufferedImage;

public interface TileAnalyzer {
    Block analyzeTileImage(BufferedImage image);
}
