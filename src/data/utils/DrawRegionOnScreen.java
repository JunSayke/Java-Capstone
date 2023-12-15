package src.data.utils;

import src.data.utils.ini_file_handler.IniFileHandler;
import src.data.utils.ini_file_handler.IniFileWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class DrawRegionOnScreen extends JFrame {
    private final Rectangle selectedRegion;
    private Point startPoint;

    public final IniFileHandler iniFileHandler;

    public DrawRegionOnScreen(String filepath) {
        selectedRegion = new Rectangle();
        iniFileHandler = new IniFileWriter(filepath);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setBackground(new Color(0, 0, 0, 100)); // Set background color to transparent

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                selectedRegion.setBounds(e.getX(), e.getY(), 0, 0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedRegion.width == 0 && selectedRegion.height == 0) {
                    dispose();
                }
                selectedRegion.add(e.getPoint());
                repaint();
                try {
                    int x = (int) selectedRegion.getX();
                    int y = (int) selectedRegion.getY();
                    int width = (int) selectedRegion.getWidth();
                    int height = (int) selectedRegion.getHeight();
                    iniFileHandler.setProperty("x", String.valueOf(x));
                    iniFileHandler.setProperty("y", String.valueOf(y));
                    iniFileHandler.setProperty("width", String.valueOf(width));
                    iniFileHandler.setProperty("height", String.valueOf(height));
                    iniFileHandler.processFile();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,"Failed to set configSelectedRegion.ini!", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    dispose();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                // Determine the bounds of the selectedRegion
                int newX = Math.min(startPoint.x, x);
                int newY = Math.min(startPoint.y, y);
                int width = Math.abs(x - startPoint.x);
                int height = Math.abs(y - startPoint.y);

                // Update selectedRegion bounds
                selectedRegion.setBounds(newX, newY, width, height);
                repaint();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (selectedRegion != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(new Color(255, 0, 0, 50)); // Semi-transparent red
            g2d.fillRect(selectedRegion.x, selectedRegion.y, selectedRegion.width, selectedRegion.height);
            g2d.setColor(Color.RED);
            g2d.drawRect(selectedRegion.x, selectedRegion.y, selectedRegion.width, selectedRegion.height);
            g2d.dispose();
        }
    }
}

