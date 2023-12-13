package src;

import src.data.*;
import src.data.solver.AdvancedAlgo;
import src.data.Tile;
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

import static java.lang.Integer.parseInt;
import static src.HeaderPanel.getCol;
import static src.HeaderPanel.getRow;

/*
 * Changes I made to other classes:
 * - In Block class, I changed Block state to be package private so paintBoard can access the states
 *      for painting each tile
 * - In the Debugging class, I changed solveBoard(Tile, int) to a static class, so I can access it
 *      here
 * - In the Debugging class, I changed screenshot(Rectangle, String) to a package private static class,
 *      so I can access it here
 *
 * Problems:
 * - Capslock only works if it's the highlighted application
 * - I have concerns over the amount of static classes and methods I made 😭
 * - Setting columns and rows that are not the same values, an error occurs
 * - Setting columns and rows that are not 2^n, an error occurs
 * - Link tile size checkbox, does not work as intended
 *
 * First order of business:
 * (✔) 1. Create a board template using images
 * (✔) 2. Add probability markers. If 100% a mine, add a red gradient
 * (✔) 3. Remove all static context and place all actions on Main
 * (✔) 3. Flip table cause flipped
 * (✔) 4. Display other probabilities (The algorithm doesn't work well displaying other probabilities)
 * (✔) 5. Create header with: Scan (keyboard shortcut: capslock or button click) and Width and Height setters
 * (✔) 6. Auto change tileSize based on amount of tiles
 *
 * (✔) 7. Cleanup code
 *
 * Changes(Version 3)
 *  - Removed BoardFrame
 *  - Cleaned up bulk of psvm
 *  - finally set row, column, and total bombs properly
 *  - removed BoardGui's Component listener as it is obsolete
 */

// Mainframe
public class BoardGui extends JFrame {
    // GUI Test
    public static void main(String[] args) {
        BoardGui jf = new BoardGui();
        jf.add(boardPanel);
        jf.setVisible(true);
        jf.setLocationRelativeTo(null);
        jf.setTitle("Minesweeper Solver");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    // Main code
    static BoardPanel boardPanel;
    JPanel headerPanel;

    // Constructor
    public BoardGui() {
        boardPanel = new BoardPanel(this);
        headerPanel = new HeaderPanel(this, boardPanel);
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.PAGE_START);
        setResizable(false);

        add(boardPanel);
        pack();
        boardPanel.setFocusable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // Paints the board
    public static void scanNewImage() throws IOException, AWTException, InterruptedException {
        Rectangle selectedRegion = new Rectangle(224, 273, 512, 512); // mnsw.pro
//        Rectangle selectedRegion = new Rectangle(210, 373, 540, 420); // minesweeper google
        MinesweeperAI minesweeperAI = new MinesweeperAI(HeaderPanel.getRow(), HeaderPanel.getCol(), HeaderPanel.getMineCount(), new AdvancedAlgo());
        Tile[][] board = minesweeperAI.scanBoardImage(selectedRegion, PixelTileAnalyzer.getInstance());
        boardPanel.setTileSize();
        boardPanel.paintBoard(boardPanel.getGraphics(), board);
        minesweeperAI.shuffleSafeAndMineTiles();
        minesweeperAI.clickMineTiles(true);
        minesweeperAI.clickSafeTiles(true);
    }
}

// The minefield itself
class BoardPanel extends JPanel {
    BoardGui boardGui;
    int tileSize;
    BufferedImage mine, closed, flag, empty;
    BufferedImage one, two, three, four, five, six, seven, eight;

    // Constructor
    public BoardPanel(BoardGui boardGui) {
        this.boardGui = boardGui;
        setPreferredSize(new Dimension(30 * 25 - 10, 16 * 33));
        this.tileSize = (int) Math.min(getPreferredSize().getHeight() / getCol(), getPreferredSize().getWidth() / getRow());
        ;
        try {
            this.mine = ImageIO.read(new File("src\\data\\resources\\bomb.png"));
            this.closed = ImageIO.read(new File("src\\data\\resources\\closed.png"));
            this.flag = ImageIO.read(new File("src\\data\\resources\\flag.png"));
            this.empty = ImageIO.read(new File("src\\data\\resources\\empty.png"));
            this.one = ImageIO.read(new File("src\\data\\resources\\1.png"));
            this.two = ImageIO.read(new File("src\\data\\resources\\2.png"));
            this.three = ImageIO.read(new File("src\\data\\resources\\3.png"));
            this.four = ImageIO.read(new File("src\\data\\resources\\4.png"));
            this.five = ImageIO.read(new File("src\\data\\resources\\5.png"));
            this.six = ImageIO.read(new File("src\\data\\resources\\6.png"));
            this.seven = ImageIO.read(new File("src\\data\\resources\\7.png"));
            this.eight = ImageIO.read(new File("src\\data\\resources\\8.png"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Tile size setter
    public void setTileSize() {
        this.tileSize = (int) Math.min(getSize().getHeight() / getCol(), getSize().getWidth() / getRow());
    }

    // CREATING THE BOARD
    public void paintBoard(Graphics g, Tile[][] board) throws InterruptedException {
        super.paintComponent(g);
        double mineTile = 0;
        int row = 0;
        int col = 0;
        // 2nd pass for printing of the cells
        for (Tile[] tiles : board) {
            for (int j = 0; j < board.length; j++) {
                mineTile = tiles[j].getProbability();
                switch (tiles[j].getState()) {
                    case MINE:
                        paintComponent(g, mine, row, col, -1);
                        break;
                    case CLOSED:
                        paintComponent(g, closed, row, col, mineTile);
                        break;
                    case FLAG:
                        paintComponent(g, flag, row, col, -1);
                        break;
                    case EMPTY:
                        paintComponent(g, empty, row, col, -1);
                        break;
                    case ONE:
                        paintComponent(g, one, row, col, -1);
                        break;
                    case TWO:
                        paintComponent(g, two, row, col, -1);
                        break;
                    case THREE:
                        paintComponent(g, three, row, col, -1);
                        break;
                    case FOUR:
                        paintComponent(g, four, row, col, -1);
                        break;
                    case FIVE:
                        paintComponent(g, five, row, col, -1);
                        break;
                    case SIX:
                        paintComponent(g, six, row, col, -1);
                        break;
                    case SEVEN:
                        paintComponent(g, seven, row, col, -1);
                        break;
                    case EIGHT:
                        paintComponent(g, eight, row, col, -1);
                        break;
                }
                row += tileSize;
            }
            row = 0;
            col += tileSize;
        }
    }

    // PRINTS EACH CELL OF THE BOARD
    public void paintComponent(Graphics g, BufferedImage i, int x, int y, double mineTile) {
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

        // Create a RescaleOp with the color scales
        float[] scales = {col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f};
        float[] offsets = {0, 0, 0, 0};
        RescaleOp tintOp = new RescaleOp(scales, offsets, null);

        // Apply the tint to the BufferedImage
        BufferedImage tintedImage = tintOp.filter(i, null);

        // Draw the tinted image
        g.drawImage(tintedImage, x, y, x + tileSize, y + tileSize, 0, 0, 200, 200, null);
    }
}

// The header GUI
class HeaderPanel extends JPanel implements ActionListener {
    BoardGui boardGui;
    BoardPanel boardPanel;
    static TextField row = new TextField("0", 3);
    static TextField col = new TextField("0", 3);
    static TextField totalMines;
    static JCheckBox automateClick;
    Button scan;

    // Very confusing constructor
    public HeaderPanel(BoardGui boardGui, BoardPanel boardPanel) {
        this.boardGui = boardGui;
        this.boardPanel = boardPanel;
        this.setMinimumSize(new Dimension(1000, 100));

        row = new TextField("16", 3);
        col = new TextField("16", 3);
        totalMines = new TextField("40", 3);

        automateClick = new JCheckBox("Toggle Auto-Click");
        automateClick.setActionCommand("Toggle Auto-Click");
        automateClick.addActionListener(this);

        this.scan = new Button("Scan board again");
        scan.setActionCommand("Scan");
        scan.addActionListener(this);

        this.add(new JLabel("Row"));
        this.add(row);
        this.add(new JLabel("Column"));
        this.add(col);
        this.add(automateClick);
        this.add(new JLabel("Total Mines"));
        this.add(totalMines);
        this.add(scan);
    }

    // When buttons are clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand() == null) {
                this.repaint();
                boardPanel.repaint();
            } else {
                if (e.getActionCommand().equals("Toggle Auto-Click")) {
                    if (automateClick.isSelected()) {

                    }
                }
                if (e.getActionCommand().equals("Scan")) {
                    System.out.println("Scanning additional pylons");
                    BoardGui.scanNewImage();
                }
            }
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
    }

    public static int getRow() {
        return parseInt(row.getText());
    }

    public static int getCol() {
        return parseInt(col.getText());
    }

    public static int getMineCount() {
        return parseInt(totalMines.getText());
    }
}