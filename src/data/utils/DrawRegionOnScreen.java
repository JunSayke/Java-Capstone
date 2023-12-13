package src.data.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DrawRegionOnScreen extends JFrame {
    private final Rectangle selectedRegion;
    private Point startPoint;

    public DrawRegionOnScreen(Rectangle selectedRegion) throws AWTException {
        this.selectedRegion = selectedRegion;

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
                if (selectedRegion != null) {
                    if (selectedRegion.width == 0 && selectedRegion.height == 0) {
                        dispose();
                    }
                    selectedRegion.add(e.getPoint());
                    repaint();
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

