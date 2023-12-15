package src;

import src.data.*;
import src.data.enums.Block;
import src.data.solver.AdvancedAlgo;
import src.data.Tile;
import src.data.utils.DrawRegionOnScreen;
import src.data.utils.image_analysis.PixelTileAnalyzer;
import src.data.utils.ini_file_handler.IniFileHandler;
import src.data.utils.ini_file_handler.IniFileReader;
import src.data.utils.ini_file_handler.IniFileWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class BoardGui extends JFrame {
    public static void main(String[] args) {
        BoardGui jf = new BoardGui();
        jf.setVisible(true);
        jf.setLocationRelativeTo(null);
        jf.setTitle("Minesweeper Solver");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    static boolean IS_AUTOMATING = false;
    public static String DIRECTORY_PATH = "src\\data\\resources\\";
    public final static String BACKGROUND_IMAGE = "src\\data\\resources\\background.jpg";
    public final static String CONFIG_OTHER = "src\\data\\configOther.ini";
    public final static String CONFIG_GAME_SETTINGS = "src\\data\\configGameSettings.ini";
    public final static String CONFIG_SELECTED_REGION = "src\\data\\configSelectedRegion.ini";

    public BoardGui() {
        BoardPanel boardPanel = new BoardPanel();
        JPanel headerPanel = new HeaderPanel(boardPanel);
        add(headerPanel, BorderLayout.PAGE_START);
        setResizable(false);

        add(boardPanel);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void paint(Graphics g) {
        super.paint(g);
        try {
            g.drawImage(ImageIO.read(new File(BACKGROUND_IMAGE)), 0, 60, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class BoardPanel extends JPanel {
    private int tileSize;
    private final Map<String, BufferedImage> resources;
    public static String[] IMAGES_NAME = {
            "FLAG", "MINE", "CLOSED", "EMPTY", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT"
    };

    public BoardPanel() {
        setPreferredSize(new Dimension(750, 550));
        resources = new HashMap<>();
        try {
            for (String filename : IMAGES_NAME) {
                BufferedImage image = ImageIO.read(new File(BoardGui.DIRECTORY_PATH + filename + ".png"));
                resources.put(filename, image);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Image not found!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void paintBackground(Graphics g) throws IOException {
        Image backgroundImage = ImageIO.read(new File(BoardGui.BACKGROUND_IMAGE));
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

// The header GUI
class HeaderPanel extends JPanel {
    private final BoardPanel boardPanel;
    private final TextField tfRow;
    private final TextField tfCol;
    private final TextField tfTotalMines;
    private final JCheckBox cbAutomateClick;
    private final MinesweeperAI minesweeperAI;

    public HeaderPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;

        setMinimumSize(new Dimension(1000, 100));

        tfRow = new TextField("16", 3);
        tfCol = new TextField("16", 3);
        tfTotalMines = new TextField("40", 3);

        minesweeperAI = new MinesweeperAI(getRow(), getCol(), getMineCount());
        getOtherConfig();

        cbAutomateClick = new JCheckBox("Toggle Auto-Click");
        Button btnScan = new Button("Scan Board");
        Button btnAutomate = new Button("Solve for me");
        Button btnSelectRegion = new Button("Select Region");

        add(new JLabel("Row"));
        add(tfRow);
        add(new JLabel("Column"));
        add(tfCol);
        add(new JLabel("Total Mines"));
        add(tfTotalMines);
        add(cbAutomateClick);
        add(btnScan);
        add(btnAutomate);
        add(btnSelectRegion);

        getGameSettingsConfig();

        btnScan.addActionListener(e -> {
            System.out.println("Scanning additional pylons");
            try {
                btnScan.setEnabled(false);
                setGameSettingsConfig();
                scanNewImage();
            } catch (IOException | AWTException ex) {
                throw new RuntimeException(ex);
            } finally {
                btnScan.setEnabled(true);
            }
        });
        btnAutomate.addActionListener(e -> {
            try {
                btnAutomate.setEnabled(false);
                automateAll();
            } catch (IOException | AWTException ex) {
                throw new RuntimeException(ex);
            } finally {
                btnAutomate.setEnabled(true);
            }
        });
        btnSelectRegion.addActionListener(e -> selectRegion());
    }

    public void selectRegion() {
        new DrawRegionOnScreen(src.BoardGui.CONFIG_SELECTED_REGION).setVisible(true);
    }

    public boolean validateInputs() {
        boolean isValid = true;
        try {
            int rows = Integer.parseInt(tfRow.getText());
            int cols = Integer.parseInt(tfCol.getText());
            int mines = Integer.parseInt(tfTotalMines.getText());
            if (rows < 0 || cols < 0 || mines < 0) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Invalid Inputs! Please input a valid positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return isValid;
    }

    public void automateAll() throws IOException, AWTException {
        if (!validateInputs()) {
            return;
        }
        Rectangle selectedRegion = getSelectedRegionConfig();
        BoardGui.IS_AUTOMATING = true;
        updateMinesweeperAIFields();
        do {
            Tile[][] board = minesweeperAI.scanBoardImage(selectedRegion, new PixelTileAnalyzer());
            Tile[][] solveBoard = minesweeperAI.solveBoard(board, new AdvancedAlgo());
            if (isSolvedOrGameOver()) {
                return;
            }
            boardPanel.setTileSize(getRow(), getCol());
            boardPanel.paintBoard(boardPanel.getGraphics(), solveBoard);
            minesweeperAI.clickMineTiles(true);
            minesweeperAI.clickSafeTiles(true);
            if (minesweeperAI.getSafeTiles().isEmpty()) {
                break;
            }
        } while (minesweeperAI.getKnownMines() < getMineCount());
        BoardGui.IS_AUTOMATING = false;
        JOptionPane.showMessageDialog(null, "Ikay tiwas!", "Notice", JOptionPane.INFORMATION_MESSAGE);
    }

    private Rectangle getSelectedRegionConfig() {
        Rectangle selectedRegion = null;
        try {
            IniFileHandler iniFileHandler = new IniFileReader(src.BoardGui.CONFIG_SELECTED_REGION);
            iniFileHandler.processFile();
            int x = Integer.parseInt(iniFileHandler.getProperty("x"));
            int y = Integer.parseInt(iniFileHandler.getProperty("y"));
            int width = Integer.parseInt(iniFileHandler.getProperty("width"));
            int height = Integer.parseInt(iniFileHandler.getProperty("height"));
            selectedRegion = new Rectangle(x, y, width, height);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Failed to read configSelectedRegion.ini!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return selectedRegion;
    }

    private void setGameSettingsConfig() {
        try {
            IniFileHandler iniFileHandler = new IniFileWriter(src.BoardGui.CONFIG_GAME_SETTINGS);
            String rows = tfRow.getText();
            String cols = tfCol.getText();
            String totalMines = tfTotalMines.getText();
            String automateClick;
            if (cbAutomateClick.isSelected()) automateClick = "true";
            else automateClick = "false";

            iniFileHandler.setProperty("automateClick", automateClick);
            iniFileHandler.setProperty("rows", rows);
            iniFileHandler.setProperty("cols", cols);
            iniFileHandler.setProperty("totalMines", totalMines);
            iniFileHandler.processFile();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Failed to set configGameSettings.ini!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void getGameSettingsConfig() {
        try {
            IniFileHandler iniFileHandler = new IniFileReader(src.BoardGui.CONFIG_GAME_SETTINGS);
            iniFileHandler.processFile();
            cbAutomateClick.setSelected(Boolean.parseBoolean(iniFileHandler.getProperty("automateClicks")));
            tfRow.setText(iniFileHandler.getProperty("rows"));
            tfCol.setText(iniFileHandler.getProperty("cols"));
            tfTotalMines.setText(iniFileHandler.getProperty("totalMines"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Failed to read configGameSettings.ini!", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void getOtherConfig() {
        try {
            IniFileHandler iniFileHandler = new IniFileReader(BoardGui.CONFIG_OTHER);
            iniFileHandler.processFile();
            int mouseMoveSteps = Integer.parseInt(iniFileHandler.getProperty("mouseMoveSteps"));
            int mouseMoveDelay = Integer.parseInt(iniFileHandler.getProperty("mouseMoveDelay"));
            int pixelTolerance = Integer.parseInt(iniFileHandler.getProperty("pixelTolerance"));
            int tileOffset = Integer.parseInt(iniFileHandler.getProperty("tileOffset"));
            boolean saveTileImage = Boolean.parseBoolean(iniFileHandler.getProperty("saveTileImage"));
            boolean saveBoardImage = Boolean.parseBoolean(iniFileHandler.getProperty("saveBoardImage"));
            String directoryPath = iniFileHandler.getProperty("directoryPath");

            minesweeperAI.setMouseMoveSteps(mouseMoveSteps);
            minesweeperAI.setMouseMoveDelay(mouseMoveDelay);
            minesweeperAI.setPixelTolerance(pixelTolerance);
            minesweeperAI.setTileOffset(tileOffset);
            minesweeperAI.setSaveTileImage(saveTileImage);
            minesweeperAI.setSaveBoardImage(saveBoardImage);
            minesweeperAI.setDirectoryPath(directoryPath);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Failed to read configOther.ini!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void scanNewImage() throws IOException, AWTException {
        if (!validateInputs()) {
            return;
        }
        BoardGui.IS_AUTOMATING = true;
        Rectangle selectedRegion = getSelectedRegionConfig();
        updateMinesweeperAIFields();
        Tile[][] board = minesweeperAI.scanBoardImage(selectedRegion, new PixelTileAnalyzer());
        Tile[][] solveBoard = minesweeperAI.solveBoard(board, new AdvancedAlgo());
        boardPanel.setTileSize(getRow(), getCol());
        boardPanel.paintBoard(boardPanel.getGraphics(), solveBoard);
        if (isSolvedOrGameOver()) {
            return;
        }
        if (cbAutomateClick.isSelected()) {
            minesweeperAI.clickMineTiles(true);
            minesweeperAI.clickSafeTiles(true);
        }
        BoardGui.IS_AUTOMATING = false;
    }

    private void updateMinesweeperAIFields() {
        minesweeperAI.setRows(getRow());
        minesweeperAI.setCols(getCol());
        minesweeperAI.setTotalMines(getMineCount());
    }

    private boolean isSolvedOrGameOver() {
        if (minesweeperAI.isSolved()) {
            JOptionPane.showMessageDialog(null, "Plus points na ka!", "Notice", JOptionPane.INFORMATION_MESSAGE);
            BoardGui.IS_AUTOMATING = false;
            return true;
        }
        if (minesweeperAI.isGameOver()) {
            JOptionPane.showMessageDialog(null, "Naa pay lain?", "Notice", JOptionPane.INFORMATION_MESSAGE);
            BoardGui.IS_AUTOMATING = false;
            return true;
        }
        return false;
    }

    public int getRow() {
        return parseInt(tfRow.getText());
    }

    public int getCol() {
        return parseInt(tfCol.getText());
    }

    public int getMineCount() {
        return parseInt(tfTotalMines.getText());
    }
}