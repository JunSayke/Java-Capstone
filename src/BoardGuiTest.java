package src;

import src.data.*;
import src.data.enums.Block;
import src.data.exceptions.InvalidBoardException;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static src.BoardGuiTest.isAutomating;
import static src.HeaderPanel.*;

// Mainframe
public class BoardGuiTest extends JFrame {
    // GUI Test
    public static void main(String[] args) throws IOException {
        BoardGuiTest jf = new BoardGuiTest();
        jf.setVisible(true);
        jf.setLocationRelativeTo(null);
        jf.setTitle("Minesweeper Solver");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    // Main code
    static BoardPanel boardPanel;
    JPanel headerPanel;
    static boolean isAutomating = false;

    private final static String backgroundImage = "src\\data\\resources\\background.jpg";

    // Constructor
    public BoardGuiTest() throws IOException {
        boardPanel = new BoardPanel();
        headerPanel = new HeaderPanel(this, boardPanel);
        add(headerPanel, BorderLayout.PAGE_START);
        setResizable(false);

        add(boardPanel);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getGameSettingsConfig();
    }

    public void paint(Graphics g) {
        super.paint(g);
        try {
            g.drawImage(ImageIO.read(new File(backgroundImage)), 0, 60, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Paints the board
    public static void scanNewImage() throws IOException, AWTException {
        Rectangle selectedRegion = getSelectedRegionConfig();
        MinesweeperAI minesweeperAI = new MinesweeperAI(HeaderPanel.getTfRow(), HeaderPanel.getTfCol(), HeaderPanel.getMineCount(), new AdvancedAlgo());
        Tile[][] board = minesweeperAI.scanBoardImage(selectedRegion, new PixelTileAnalyzer());
        boardPanel.setTileSize();
        boardPanel.paintBoard(boardPanel.getGraphics(), board);
        if (cbAutomateClick.isSelected()) {
            minesweeperAI.clickMineTiles(true);
            minesweeperAI.clickSafeTiles(true);
        } else {
            minesweeperAI.clickMineTiles(false);
            minesweeperAI.clickSafeTiles(false);
        }
    }

    //Automate solving the board
    public static void automateAll() throws IOException, AWTException {
        Rectangle selectedRegion = getSelectedRegionConfig();
        MinesweeperAI minesweeperAI;
        isAutomating = true;
        do {
            minesweeperAI = new MinesweeperAI(HeaderPanel.getTfRow(), HeaderPanel.getTfCol(), HeaderPanel.getMineCount(), new AdvancedAlgo());
            Tile[][] board = minesweeperAI.scanBoardImage(selectedRegion, new PixelTileAnalyzer());
            boardPanel.setTileSize();
            boardPanel.paintBoard(boardPanel.getGraphics(), board);
            minesweeperAI.clickMineTiles(true);
            minesweeperAI.clickSafeTiles(true);
            if (minesweeperAI.getSafeTiles().isEmpty()) {
                break;
            }
        } while (minesweeperAI.getBoardAnalyzer().getKnownMines() < HeaderPanel.getMineCount());
        isAutomating = false;
        JOptionPane.showMessageDialog(null, "Automation is done!");
    }

    //Selecting the board
    public static void selectRegion() {
        try {
            new DrawRegionOnScreen("src\\data\\configSelectedRegion.ini").setVisible(true);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    private static Rectangle getSelectedRegionConfig() throws IOException {
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

    private static void getGameSettingsConfig() throws IOException {
        IniFileHandler iniFileHandler = new IniFileReader("src\\data\\configGameSettings.ini");
        iniFileHandler.processFile();
        cbAutomateClick.setSelected(Boolean.parseBoolean(iniFileHandler.getProperty("automateClicks")));
        tfRow.setText(iniFileHandler.getProperty("rows"));
        tfCol.setText(iniFileHandler.getProperty("cols"));
        tfTotalMines.setText(iniFileHandler.getProperty("totalMines"));
    }

    static void setGameSettingsConfig() throws IOException {
        IniFileHandler iniFileHandler = new IniFileWriter("src\\data\\configGameSettings.ini");
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
    }
}

class BoardPanel extends JPanel {
    private int tileSize;
    private final Map<String, BufferedImage> resources;
    public static String DIRECTORY_PATH = "src\\data\\resources\\";
    public static String[] IMAGES_NAME = {
            "FLAG", "MINE", "CLOSED", "EMPTY", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT"
    };

    // Constructor
    public BoardPanel() {
        setPreferredSize(new Dimension(30 * 25 - 10, 16 * 33));
        this.tileSize = (int) Math.min(getPreferredSize().getHeight() / getTfCol(), getPreferredSize().getWidth() / getTfRow());
        System.out.println("TEST:" + tileSize);
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

    // CREATING THE BOARD
    public void paintBoard(Graphics g, Tile[][] board) throws IOException {
        paintBackground(g);
        double mineTile;
        double averageProbability = 0;
        int row = 0;
        int col = 0;
        for (Tile[] tiles : board) {
            for (Tile tile : tiles) {
                mineTile = tile.getProbability();
                averageProbability += mineTile;
                Block state = tile.getState();
                paintComponent(g, resources.get(state.toString()), row, col, (state == Block.CLOSED ? mineTile : -1));
                row += tileSize;
            }
            row = 0;
            col += tileSize;
        }
        if (averageProbability / board.length == 0 && !isAutomating) throw new InvalidBoardException();
    }

    // PRINTS EACH CELL OF THE BOARD
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

    // Tile size setter
    public void setTileSize() {
        tileSize = (int) Math.min(getSize().getHeight() / getTfCol(), getSize().getWidth() / getTfRow());
        System.out.println(tileSize);
    }
}



// The header GUI
class HeaderPanel extends JPanel implements ActionListener {

    BoardGuiTest BoardGuiTest;
    BoardPanel boardPanel;
    static TextField tfRow = new TextField("0", 3);
    static TextField tfCol = new TextField("0", 3);
    static TextField tfTotalMines;
    static JCheckBox cbAutomateClick;
    Button btnScan;
    Button btnAutomate;
    Button btnSelectRegion;

    // Very confusing constructor
    public HeaderPanel(BoardGuiTest BoardGuiTest, BoardPanel boardPanel) {
        this.BoardGuiTest = BoardGuiTest;
        this.boardPanel = boardPanel;
        this.setMinimumSize(new Dimension(1000, 100));

        tfRow = new TextField("16", 3);
        tfCol = new TextField("16", 3);
        tfTotalMines = new TextField("40", 3);

        cbAutomateClick = new JCheckBox("Toggle Auto-Click");
        cbAutomateClick.setActionCommand("Toggle Auto-Click");
        cbAutomateClick.addActionListener(this);

        this.btnScan = new Button("Scan Board");
        btnScan.setActionCommand("Scan");
        btnScan.addActionListener(this);

        this.btnAutomate = new Button("Solve for me");
        btnAutomate.setActionCommand("Solve");
        btnAutomate.addActionListener(this);

        this.btnSelectRegion = new Button("Select Region");
        btnSelectRegion.setActionCommand("Select Region");
        btnSelectRegion.addActionListener(this);

        this.add(new JLabel("Row"));
        this.add(tfRow);
        this.add(new JLabel("Column"));
        this.add(tfCol);
        this.add(new JLabel("Total Mines"));
        this.add(tfTotalMines);
        this.add(cbAutomateClick);
        this.add(btnScan);
        this.add(btnAutomate);
        this.add(btnSelectRegion);
    }

    // When buttons are clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand() == null) {
                this.repaint();
                boardPanel.repaint();
            } else {
                if (e.getActionCommand().equals("Scan")) {
                    System.out.println("Scanning additional pylons");
                    BoardGuiTest.setGameSettingsConfig();
                    BoardGuiTest.scanNewImage();
                }
                if (e.getActionCommand().equals("Solve")) {
                    BoardGuiTest.automateAll();
                }
                if (e.getActionCommand().equals("Select Region")) {
                    BoardGuiTest.selectRegion();
                }
            }
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
    }

    public static int getTfRow() {
        return parseInt(tfRow.getText());
    }

    public static int getTfCol() {
        return parseInt(tfCol.getText());
    }

    public static int getMineCount() {
        return parseInt(tfTotalMines.getText());
    }
}