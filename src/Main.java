package src;

import src.data.MinesweeperAI;
import src.data.Tile;
import src.data.enums.Block;
import src.data.exceptions.InvalidBoardException;
import src.data.solver.AdvancedAlgo;
import src.data.utils.image_analysis.PixelTileAnalyzer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main extends JFrame {
    private JTextField tfRow;
    private JTextField tfColumn;
    private JTextField tfTotalMines;
    private JCheckBox cbAutoClick;
    private JButton btnScanBoard;
    private JButton btnAutoSolve;
    private JButton btnSelectRegion;
    private JLabel lblRow;
    private JLabel lblColumn;
    private JLabel lblTotalMines;
    private JPanel jpanel;

    private BoardJPanel boardJPanel;

    public Main() {
        tfRow.setText("16");
        tfColumn.setText("16");
        tfTotalMines.setText("40");

        boardJPanel = new BoardJPanel(32);
        add(boardJPanel);

        btnScanBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MinesweeperAI minesweeperAI = new MinesweeperAI(Integer.parseInt(tfRow.getText()),
                        Integer.parseInt(tfColumn.getText()),
                        Integer.parseInt(tfTotalMines.getText()),
                        new AdvancedAlgo());
                Rectangle rectangle = new Rectangle(224, 272, 512, 512);
                Tile[][] board;
                try {
                    board = minesweeperAI.scanBoardImage(rectangle, new PixelTileAnalyzer());
                } catch (AWTException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnAutoSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnSelectRegion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) throws IOException, AWTException {
        Main app = new Main();
        app.setContentPane(app.jpanel);
        app.setSize(900, 600);
        app.setDefaultCloseOperation(EXIT_ON_CLOSE);
        app.setTitle("Minesweeper Solver - Group 11");
        app.setLocationRelativeTo(null);
        app.setResizable(false);
        app.setVisible(true);
    }
}

class BoardJPanel extends JPanel {
    private int tileSize;
    private final Map<String, BufferedImage> resources;
    public static String DIRECTORY_PATH = "src\\data\\resources\\";
    public static String[] IMAGES_NAME = {
            "FLAG", "MINE", "CLOSED", "EMPTY", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT"
    };

    // Constructor
    public BoardJPanel(int tileSize) {
        this.tileSize = tileSize;
        setPreferredSize(new Dimension(30 * 25 - 10, 16 * 33));
        resources = new HashMap<>();
        try {
            for (String filename : IMAGES_NAME) {
                BufferedImage image = ImageIO.read(new File(DIRECTORY_PATH + filename + ".png"));
                resources.put(filename, image);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Image not found!");
        }
    }

    private void paintBackground(Graphics g) throws IOException {
        super.paint(g);
        Image backgroundImage = ImageIO.read(new File("src\\data\\resources\\background.jpg"));
        g.drawImage(backgroundImage, 0, 0, null);
    }

    // CREATING THE BOARD
    public void paintBoard(Graphics g, Tile[][] board) throws IOException {
        System.out.println(g);
        super.paintComponent(g);
        paintBackground(g);
        double mineTile;
        int row = 0;
        int col = 0;
        for (Tile[] tiles : board) {
            for (Tile tile : tiles) {
                mineTile = tile.getProbability();
                Block state = tile.getState();
                paintComponent(g, resources.get(state.toString()), row, col, (state == Block.CLOSED ? mineTile : -1));
                row += tileSize;
            }
            row = 0;
            col += tileSize;
        }
    }

    // PRINTS EACH CELL OF THE BOARD
    public void paintComponent(Graphics g, BufferedImage image, int x, int y, double mineTile) {
        Color col = new Color(255, 255, 255);

        if (mineTile >= 1.0) {
            col = new Color(255, 0, 0);
        }
        else if (mineTile > 0.9) {
            col = new Color(250, 250, 250);
        }
        else if (mineTile > 0.5) {
            col = new Color(200, 200, 200);
        }
        else if (mineTile > 0.3) {
            col = new Color(140, 140, 140);
        }
        else if (mineTile == 0) {
            col = new Color(0, 255, 0);
        }

        float[] scales = {col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f};
        float[] offsets = {0, 0, 0, 0};
        RescaleOp tintOp = new RescaleOp(scales, offsets, null);

        BufferedImage tintedImage = tintOp.filter(image, null);
        g.drawImage(tintedImage, x, y, x + tileSize, y + tileSize, 0, 0, 200, 200, null);
    }
}
