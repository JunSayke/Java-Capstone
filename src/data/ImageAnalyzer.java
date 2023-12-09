package src.data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageAnalyzer {
    private final BufferedImage image;
    private final int width;
    private final int height;
    private final boolean hasAlphaChannel;
    private final int pixelLength;
    private final byte[] pixels;

    public ImageAnalyzer(BufferedImage image) {
        this.image = image;
        this.pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.hasAlphaChannel = image.getAlphaRaster() != null;
        this.pixelLength = hasAlphaChannel ? 4 : 3;
    }

    public int getRGB(int x, int y) {
        int pos = (y * pixelLength * width) + (x * pixelLength);

        int argb = hasAlphaChannel ? ((int) pixels[pos++] & 0xff) << 24 : -16777216;

        argb += ((int) pixels[pos++] & 0xff); // blue
        argb += (((int) pixels[pos++] & 0xff) << 8); // green
        argb += (((int) pixels[pos] & 0xff) << 16); // red
        return argb;
    }

    public BufferedImage cropImage(int x, int y, int width, int height) {
        x = Math.max(0, Math.min(this.width, x));
        y = Math.max(0, Math.min(this.height, y));
        width = Math.min(width, this.width - x);
        height = Math.min(height, this.height - y);
        return image.getSubimage(x, y, width, height);
    }

    public Point pixelSearch(int argb) {
        return pixelSearch(argb, 0);
    }

    public Point pixelSearch(int argb, int tolerance) {
        return pixelSearch(argb, 0, 0, width, height, tolerance);
    }

    public Point pixelSearch(int argb, int x, int y, int width, int height, int tolerance) {
        Color target = new Color(argb, true);
        x = Math.max(0, Math.min(this.width, x));
        y = Math.max(0, Math.min(this.height, y));
        width = Math.min(width, this.width - x);
        height = Math.min(height, this.height - y);

        for (int row = x; row < width; row++) {
            for (int col = y; col < height; col++) {
                Color curr = new Color(getRGB(row, col));
                int diffA = Math.abs(target.getAlpha() - curr.getAlpha());
                int diffR = Math.abs(target.getRed() - curr.getRed());
                int diffG = Math.abs(target.getGreen() - curr.getGreen());
                int diffB = Math.abs(target.getBlue() - curr.getBlue());
                if (diffA <= tolerance && diffR <= tolerance && diffG <= tolerance && diffB <= tolerance) {
                    return new Point(row, col);
                }
            }
        }
        return null;
    }

    public void enumeratePixels() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.println(getRGB(x, y) + ", ");
            }
            System.out.println();
        }
    }

    public BufferedImage convertToType(int targetType) {
        if (image.getType() == targetType) {
            return image;
        }

        BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), targetType);
        Graphics2D g = convertedImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return convertedImage;
    }
}
