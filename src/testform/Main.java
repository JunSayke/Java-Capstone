package src.testform;

import src.data.MinesweeperAI;
import src.data.Tile;
import src.data.enums.Block;
import src.data.utils.DrawRegionOnScreen;
import src.data.utils.image_analysis.PixelTileAnalyzer;
import src.data.utils.ini_file_handler.IniFileHandler;
import src.data.utils.ini_file_handler.IniFileReader;
import src.data.utils.ini_file_handler.IniFileWriter;

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

import static java.lang.Integer.parseInt;

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
    private static BoardJPanel boardJPanel;

    private final static String backgroundImage = "src\\data\\resources\\background.jpg";

    public Main() throws IOException {
        tfRow.setText("16");
        tfColumn.setText("16");
        tfTotalMines.setText("40");

        boardJPanel = new BoardJPanel();
        setResizable(false);
        add(boardJPanel);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getGameSettingsConfig();

        btnScanBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Scanning additional pylons");
                try {
                    setGameSettingsConfig();
                    scanNewImage();
                } catch (IOException | AWTException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnAutoSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    automateAll();
                } catch (IOException | AWTException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnSelectRegion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectRegion();
            }
        });
    }

    public void selectRegion() {
        new DrawRegionOnScreen("src\\data\\configSelectedRegion.ini").setVisible(true);
    }

    public void automateAll() throws IOException, AWTException {
        Rectangle selectedRegion = getSelectedRegionConfig();
        MinesweeperAI minesweeperAI;
        do {
            minesweeperAI = new MinesweeperAI(getRow(), getCol(), getMineCount());
            Tile[][] board = minesweeperAI.scanBoardImage(selectedRegion, new PixelTileAnalyzer());
            boardJPanel.setTileSize(getRow(), getCol());
            boardJPanel.paintBoard(boardJPanel.getGraphics(), board);
            minesweeperAI.clickMineTiles(true);
            minesweeperAI.clickSafeTiles(true);
            if (minesweeperAI.getSafeTiles().isEmpty()) {
                break;
            }
        } while (minesweeperAI.getBoardAnalyzer().getKnownMines() < getMineCount());
        JOptionPane.showMessageDialog(null, "Automation is done!");
    }

    private Rectangle getSelectedRegionConfig() throws IOException {
        IniFileHandler iniFileHandler = new IniFileReader("src\\data\\configSelectedRegion.ini");
        iniFileHandler.processFile();
        int x = Integer.parseInt(iniFileHandler.getProperty("x"));
        int y = Integer.parseInt(iniFileHandler.getProperty("y"));
        int width = Integer.parseInt(iniFileHandler.getProperty("width"));
        int height = Integer.parseInt(iniFileHandler.getProperty("height"));
        Rectangle selectedRegion = new Rectangle(x, y, width, height);
        System.out.println(selectedRegion);
        return selectedRegion;
    }

    private void getGameSettingsConfig() throws IOException {
        IniFileHandler iniFileHandler = new IniFileReader("src\\data\\configGameSettings.ini");
        iniFileHandler.processFile();
        cbAutoClick.setSelected(Boolean.parseBoolean(iniFileHandler.getProperty("automateClicks")));
        tfRow.setText(iniFileHandler.getProperty("rows"));
        tfColumn.setText(iniFileHandler.getProperty("cols"));
        tfTotalMines.setText(iniFileHandler.getProperty("totalMines"));
    }

    private void setGameSettingsConfig() throws IOException {
        IniFileHandler iniFileHandler = new IniFileWriter("src\\data\\configGameSettings.ini");
        String rows = tfRow.getText();
        String cols = tfColumn.getText();
        String totalMines = tfTotalMines.getText();
        String automateClick;
        if (cbAutoClick.isSelected()) automateClick = "true";
        else automateClick = "false";

        iniFileHandler.setProperty("automateClick", automateClick);
        iniFileHandler.setProperty("rows", rows);
        iniFileHandler.setProperty("cols", cols);
        iniFileHandler.setProperty("totalMines", totalMines);
        iniFileHandler.processFile();
    }


    // Paints the board
    private void scanNewImage() throws IOException, AWTException {
        Rectangle selectedRegion = getSelectedRegionConfig();
        MinesweeperAI minesweeperAI = new MinesweeperAI(getRow(), getCol(), getMineCount());
        Tile[][] board = minesweeperAI.scanBoardImage(selectedRegion, new PixelTileAnalyzer());
        boardJPanel.setTileSize(getRow(), getCol());
        boardJPanel.paintBoard(boardJPanel.getGraphics(), board);
        if (cbAutoClick.isSelected()) {
            minesweeperAI.clickMineTiles(true);
            minesweeperAI.clickSafeTiles(true);
        } else {
            minesweeperAI.clickMineTiles(false);
            minesweeperAI.clickSafeTiles(false);
        }
    }

    public int getRow() {
        return parseInt(tfRow.getText());
    }

    public int getCol() {
        return parseInt(tfColumn.getText());
    }

    public int getMineCount() {
        return parseInt(tfTotalMines.getText());
    }

    public void paint(Graphics g) {
        super.paint(g);
        try {
            g.drawImage(ImageIO.read(new File(backgroundImage)), 0, 80, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, AWTException {
        Main app = new Main();
        app.setContentPane(app.jpanel);
        app.setVisible(true);
        app.setLocationRelativeTo(null);
        app.setTitle("Minesweeper Solver - Group 11");
        app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

class BoardJPanel extends JPanel {
    private int tileSize;
    private final Map<String, BufferedImage> resources;
    public static String DIRECTORY_PATH = "src\\data\\resources\\";
    public static String[] IMAGES_NAME = {
            "FLAG", "MINE", "CLOSED", "EMPTY", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT"
    };

    public BoardJPanel() {
        setPreferredSize(new Dimension(30 * 25 - 10, 16 * 33));
        resources = new HashMap<>();
        try {
            for (String filename : IMAGES_NAME) {
                BufferedImage image = ImageIO.read(new File(DIRECTORY_PATH + filename + ".png"));
                resources.put(filename, image);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Image not found!");
        }
    }

    private void paintBackground(Graphics g) throws IOException {
        Image backgroundImage = ImageIO.read(new File("src\\data\\resources\\background.jpg"));
        g.drawImage(backgroundImage, 0, 0, null);
    }

    public void paintBoard(Graphics g, Tile[][] board) throws IOException {
        paintBackground(g);
        int row = 0;
        int col = 0;
        for (Tile[] tiles : board) {
            for (Tile tile : tiles) {
                Block state = tile.getState();
                paintComponent(g, resources.get(state.toString()), row, col, (state == Block.CLOSED ? tile.getProbability() : -1));
                row += tileSize;
            }
            row = 0;
            col += tileSize;
        }
    }

    public void paintComponent(Graphics g, BufferedImage image, int x, int y, double mineTile) {
        Color col = new Color(255, 255, 255);

        if (mineTile >= 1.0) {
            col = new Color(255, 0, 0);
        } else if (mineTile > 0.9) {
            col = new Color(250, 250, 250);
        } else if (mineTile > 0.5) {
            col = new Color(200, 200, 200);
        } else if (mineTile > 0.3) {
            col = new Color(140, 140, 140);
        } else if (mineTile == 0) {
            col = new Color(0, 255, 0);
        }

        float[] scales = {col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f};
        float[] offsets = {0, 0, 0, 0};
        RescaleOp tintOp = new RescaleOp(scales, offsets, null);

        BufferedImage tintedImage = tintOp.filter(image, null);
        g.drawImage(tintedImage, x, y, x + tileSize, y + tileSize, 0, 0, 200, 200, null);
    }

    public void setTileSize(int rows, int cols) {
        tileSize = (int) Math.min(getSize().getHeight() / cols, getSize().getWidth() / rows);
        System.out.println(tileSize);
    }
}