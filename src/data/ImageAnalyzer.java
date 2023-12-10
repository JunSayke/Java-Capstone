package src.data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public class ImageAnalyzer {
    private final int width, height, pixelLength;
    private final boolean hasAlphaChannel;
    private final BufferedImage image;
    private final byte[] pixels;

    public ImageAnalyzer(BufferedImage image) {
        this.image = processImage(image);
        width = this.image.getWidth();
        height = this.image.getHeight();
        pixels = ((DataBufferByte) this.image.getRaster().getDataBuffer()).getData();
        hasAlphaChannel = this.image.getAlphaRaster() != null;
        pixelLength = hasAlphaChannel ? 4 : 3;
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
        return processImage(image.getSubimage(x, y, width, height));
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

        for (int row = x; row < x + width; row++) {
            for (int col = y; col < y + height; col++) {
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
                System.out.print(getRGB(x, y) + ", ");
            }
            System.out.println();
        }
    }

    private BufferedImage processImage(BufferedImage image) {
        BufferedImage processImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = processImage.getGraphics();
        g.drawImage(image, 0, 0,null);
        g.dispose();
        return processImage;
    }

    public void saveImage(String pathname) {
        saveImage(image, pathname);
    }

    public void saveImage(BufferedImage image, String pathname) {
        try {
            ImageIO.write(image, "png", new File(pathname));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
