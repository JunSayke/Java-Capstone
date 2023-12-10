package src;

import src.data.Block;
import src.data.MinesweeperBot;
import src.data.MinesweeperSolver;
import src.data.Tile;
import src.data.solver.AdvancedAlgo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
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

//Mainframe
public class BoardGui extends JFrame{
    //GUI Test
    public static void main(String[] args) throws IOException, AWTException {
        BoardGui jf = new BoardGui();
        jf.add(boardPanel);
        jf.setVisible(true);
        jf.setLocationRelativeTo(null);
        jf.setTitle("Minesweeper Solver");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    //Main code
    static BoardPanel boardPanel;
    JPanel headerPanel;

    //Constructor
    public BoardGui(){
        boardPanel = new BoardPanel(this);
        this.headerPanel = new HeaderPanel(this, boardPanel);
        this.setLayout(new BorderLayout());
        this.add(headerPanel, BorderLayout.PAGE_START);
        this.setResizable(false);

        this.add(boardPanel);
        this.pack();
        boardPanel.setFocusable(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    //Paints the board
    public static void scanNewImage() throws IOException, AWTException {
        Rectangle screenRegion = new Rectangle(224, 274, 511, 511);
        MinesweeperSolver minesweeperSolver = new AdvancedAlgo();
        MinesweeperBot minesweeperBot = new MinesweeperBot(screenRegion, minesweeperSolver, HeaderPanel.getRow(), HeaderPanel.getCol(), HeaderPanel.getMineCount());
        minesweeperBot.calculateProbabilities();

        boardPanel.setTileSize();
        boardPanel.paintBoard(boardPanel.getGraphics(), minesweeperBot.getBoard());

        minesweeperBot.automateClicks();
        /*
        screenshot(new Rectangle(129, 274, 512, 510), "screenshot");
        BufferedImage image = ImageIO.read(new File("screenshot.png"));

        Tile[][] board = scanBoardImage(image, HeaderPanel.getRow(), HeaderPanel.getCol());
        mineCount = getMineCount();
        minesLeft = boardPanel.countFlagged(board, mineCount);
        Tile[][] boardSolved = solveBoard(board, minesLeft);

        boardPanel.setTileSize();
        boardPanel.paintBoard(boardPanel.getGraphics(), boardSolved);
        System.out.println(minesLeft);
         */
    }
}

//The minefield itself
class BoardPanel extends JPanel{
    BoardGui boardGui;
    int tileSize;
    Image mine;
    Image closed;
    Image flag;
    Image empty;
    Image one;
    Image two;
    Image three;
    Image four;
    Image five;
    Image six;
    Image seven;
    Image eight;

    //Constructor
    public BoardPanel(BoardGui boardGui) {
        this.boardGui = boardGui;
        setPreferredSize(new Dimension(30*25 - 10, 16*33));
        this.tileSize = (int)Math.min(getPreferredSize().getHeight() / getCol(), getPreferredSize().getWidth()/ getRow());;
        try{
            this.mine = ImageIO.read(getClass().getResource("data/resources/bomb.png"));
            this.closed = ImageIO.read(getClass().getResource("data/resources/closed.png"));
            this.flag = ImageIO.read(getClass().getResource("data/resources/flag.png"));
            this.empty = ImageIO.read(getClass().getResource("data/resources/empty.png"));
            this.one = ImageIO.read(getClass().getResource("data/resources/1.png"));
            this.two = ImageIO.read(getClass().getResource("data/resources/2.png"));
            this.three = ImageIO.read(getClass().getResource("data/resources/3.png"));
            this.four = ImageIO.read(getClass().getResource("data/resources/4.png"));
            this.five = ImageIO.read(getClass().getResource("data/resources/5.png"));
            this.six = ImageIO.read(getClass().getResource("data/resources/6.png"));
            this.seven = ImageIO.read(getClass().getResource("data/resources/7.png"));
            this.eight = ImageIO.read(getClass().getResource("data/resources/8.png"));
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    //Tile size setter
    public void setTileSize() {
        this.tileSize = (int)Math.min(getSize().getHeight() / getCol(), getSize().getWidth()/ getRow());
    }

    //COUNTING MINES LEFT ON THE BOARD
    int countFlagged(Tile[][] board, int n){
        int mines = n;
        //1st pass for mines
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                System.out.println(board[i][j]);
                if(board[i][j].getState() == Block.FLAG){
                    mines -= 1;
                }
            }
        }
        return mines;
    }

    //CREATING THE BOARD
    public void paintBoard(Graphics g, Tile[][] board){
        super.paintComponent(g);
        double mineTile = 0;
        int row = 0;
        int col = 0;
        //2nd pass for printing of the cells
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                mineTile = board[i][j].getProbability();
                switch(board[i][j].getState()){
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

    //PRINTS EACH CELL OF THE BOARD
    public void paintComponent(Graphics g, Image i, int x, int y, double mineTile) {
        // Create a BufferedImage to work with
        BufferedImage bufferedImage = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the original image onto the BufferedImage
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(i, 0, 0, null);
        g2d.dispose();

        Color col = new Color(255,255,255, 255);
        if(mineTile >= 1.0){
            col = new Color(255, 0, 0, 200);
//            System.out.println("Mine!");
        }else if(mineTile > 0.9) {
            col = new Color(250, 250, 250, 255);
//            System.out.println("Unsure");
        }else if(mineTile > 0.5) {
            col = new Color(200, 200, 200, 255);
//            System.out.println("Unsure");
        }else if(mineTile > 0.3) {
            col = new Color(140, 140, 140, 255);
//            System.out.println("Unsure");
        }else if(mineTile == 0){
            col = new Color(0, 255, 0, 200);
//            System.out.println("Guaranteed no mines!");
        }

        float[] scales = { col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, col.getAlpha() / 255f };
        float[] offsets = { 0, 0, 0, 0 };
        RescaleOp tintOp = new RescaleOp(scales, offsets, null);
        tintOp.filter(bufferedImage, bufferedImage);

        // Draw the tinted image
        g.drawImage(bufferedImage, x, y, x + tileSize , y + tileSize, 0, 0, 200, 200, null);
    }
}

//The header GUI
class HeaderPanel extends JPanel implements ActionListener {
    BoardGui boardGui;
    BoardPanel boardPanel;
    static TextField row = new TextField("0", 3);
    static TextField col = new TextField("0", 3);
    static TextField totalMines;
    static JCheckBox linkTiles;
    Button scan;

    //Very confusing constructor
    public HeaderPanel(BoardGui boardGui, BoardPanel boardPanel) {
        this.boardGui = boardGui;
        this.boardPanel = boardPanel;
        this.setMinimumSize(new Dimension(1000, 100));

        row = new TextField("16", 3);
        col = new TextField("16", 3);
        totalMines = new TextField("40", 3);

        linkTiles = new JCheckBox("Link tile size");
        linkTiles.setActionCommand("Link Tile Size");
        linkTiles.addActionListener(this);

        this.scan = new Button("Scan board again");
        scan.setActionCommand("Scan");
        scan.addActionListener(this);

        this.add(new JLabel("Row"));
        this.add(row);
        this.add(new JLabel("Column"));
        this.add(col);
        this.add(linkTiles);
        this.add(new JLabel("Total Mines"));
        this.add(totalMines);
        this.add(scan);

        Toolkit.getDefaultToolkit().addAWTEventListener(new CapsLockListener(), AWTEvent.KEY_EVENT_MASK);
        if (row.getText().equals(col.getText())) {
            linkTiles.setSelected(true);
        }
    }

    //When buttons are clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand() == null) {
                this.repaint();
                boardPanel.repaint();
            } else {
                if (e.getActionCommand().equals("Link Tile Size")) {
                    if (linkTiles.isSelected()) {
                        if (!row.getText().equals(col.getText())) {
                            row.setText(col.getText());
                            col.setText(row.getText());
                        }
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

    //Makes sure the capslock will run scanNewImage() even when messing with values on the headerPanel
    //If it's just the normal KeyListener, it will not run scanNewImage() unless nothing is highlighted
    private static class CapsLockListener implements AWTEventListener {
        private boolean capsLockPressed = false;
        @Override
        public void eventDispatched(AWTEvent event) {
            if (event instanceof KeyEvent) {
                KeyEvent keyEvent = (KeyEvent) event;
                if (keyEvent.getKeyCode() == KeyEvent.VK_CAPS_LOCK) {
                    if (keyEvent.getID() == KeyEvent.KEY_PRESSED) {
                        capsLockPressed = true;
                    } else if (keyEvent.getID() == KeyEvent.KEY_RELEASED) {
                        if (capsLockPressed) {
                            System.out.println("Scannning additional pylons with capslock");
                            try {
                                BoardGui.scanNewImage();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (AWTException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        capsLockPressed = false;
                    }
                }
            }
        }
    }
}