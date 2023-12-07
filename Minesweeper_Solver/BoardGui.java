package Minesweeper_Solver;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import static Minesweeper_Solver.Debugging.*;
import static java.lang.Integer.parseInt;

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
 * - Everything calls for BoardFrame, but has nothing inside it
 * - Capslock only works if the focused application is this one and nothing in the HeaderPanel is
 *      highlighted
 *
 * First order of business:
 * (✔) 1. Create a board template using images
 * (✔) 2. Add probability markers. If 100% a mine, add a red gradient
 * (✔) 3. Remove all static context and place all actions on Main
 * (✔) 3. Flip table cause flipped
 * (✔) 4. Display other probabilities (The algorithm doesn't work well displaying other probabilities)
 * (✔) 5. Create header with: Scan (keyboard shortcut: capslock or button click) and Width and Height setters
 * (✔) 6. Auto change tileSize based on amount of tiles
 * Finish this week
 * () 7. Cleanup code
 */
public class BoardGui extends JFrame implements ComponentListener{

    //GUI Test
    public static void main(String[] args) throws IOException {
        screenshot(new Rectangle(129, 274, 641-129,784-274), "screenshot");
        BufferedImage image = ImageIO.read(new File("screenshot.png"));
        BoardGui jf = new BoardGui();
        int minesLeft;

        Tile[][] board = scanBoardImage(image, HeaderPanel.getRow(), HeaderPanel.getCol());
        minesLeft = boardPanel.countFlagged(board, mineCount);
        Tile[][] boardSolved = solveBoard(board, minesLeft);

        //jf.setSize(600, 600);
        jf.add(boardPanel);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Added this because sometimes it will not print the first few tiles
        System.out.println("Pausing");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Unpaused");

        boardPanel.paintBoard(boardPanel.getGraphics(), boardSolved);
        System.out.println(minesLeft);
    }
    //Main code
    BoardFrame boardFrame;
    static BoardPanel boardPanel;
    JPanel headerPanel;
    static int mineCount = 40;

    public BoardGui(){
        this.boardFrame = new BoardFrame();
        this.boardPanel = new BoardPanel(boardFrame);
        this.headerPanel = new HeaderPanel(boardFrame, boardPanel);
        this.setLayout(new BorderLayout());
        this.add(headerPanel, BorderLayout.PAGE_START);
        this.setResizable(false);

        this.add(boardPanel);
        this.pack();
        boardPanel.setFocusable(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        boardPanel.setSize(new Dimension(this.getWidth()-20, this.getHeight() - 20));
        boardPanel.setTileSize();
        boardPanel.repaint();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
    public static void scanNewImage() throws IOException {
        screenshot(new Rectangle(129, 274, 641-129,784-274), "screenshot");
        BufferedImage image = ImageIO.read(new File("screenshot.png"));
        Tile[][] board = scanBoardImage(image, HeaderPanel.getRow(), HeaderPanel.getCol());
        int minesLeft = boardPanel.countFlagged(board, mineCount);
        Tile[][] boardSolved = solveBoard(board, minesLeft);
        boardPanel.paintBoard(boardPanel.getGraphics(), boardSolved);
        System.out.println(minesLeft);
    }
}
class BoardFrame extends JFrame {

}
class BoardPanel extends JPanel{
    BoardFrame boardFrame;
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
    boolean keyDown = false;

    public BoardPanel(BoardFrame boardFrame) {
        this.boardFrame = boardFrame;
        setPreferredSize(new Dimension(30*30 - 10, 16*33));
        this.tileSize = (int)Math.min(getPreferredSize().getHeight() / 16, getPreferredSize().getWidth()/ 16);;
        try{
            this.mine = ImageIO.read(getClass().getResource("images_gui/bomb.png"));
            this.closed = ImageIO.read(getClass().getResource("images_gui/closed.png"));
            this.flag = ImageIO.read(getClass().getResource("images_gui/flag.png"));
            this.empty = ImageIO.read(getClass().getResource("images_gui/empty.png"));
            this.one = ImageIO.read(getClass().getResource("images_gui/1.png"));
            this.two = ImageIO.read(getClass().getResource("images_gui/2.png"));
            this.three = ImageIO.read(getClass().getResource("images_gui/3.png"));
            this.four = ImageIO.read(getClass().getResource("images_gui/4.png"));
            this.five = ImageIO.read(getClass().getResource("images_gui/5.png"));
            this.six = ImageIO.read(getClass().getResource("images_gui/6.png"));
            this.seven = ImageIO.read(getClass().getResource("images_gui/7.png"));
            this.eight = ImageIO.read(getClass().getResource("images_gui/8.png"));
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public BoardFrame getBoard() {
        return boardFrame;
    }
    public void setTileSize() {
        this.tileSize = (int)Math.min(getSize().getHeight() / 16, getSize().getWidth()/ 16);
    }

    //COUNTING MINES LEFT ON THE BOARD
    int countFlagged(Tile[][] board, int n){
        int mines = n;
        //1st pass for mines
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
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
    public void paintComponent(Graphics g, Image i, int x, int y, double mineTile) {
        // Create a BufferedImage to work with
        BufferedImage bufferedImage = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the original image onto the BufferedImage
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(i, 0, 0, null);
        g2d.dispose();

        Color col = new Color(255,255,255, 255);
        if(mineTile > 0.5){
            col = new Color(255, 0, 0, 200);
            System.out.println("Mine!");
        }
        else if(mineTile > 0.3) {
            col = new Color(140, 140, 140, 255);
            System.out.println("Warmer");
        }else if(mineTile == 0){
            col = new Color(0, 255, 0, 200);
        }

        float[] scales = { col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, col.getAlpha() / 255f };
        float[] offsets = { 0, 0, 0, 0 };
        RescaleOp tintOp = new RescaleOp(scales, offsets, null);
        tintOp.filter(bufferedImage, bufferedImage);

        // Draw the tinted image
        g.drawImage(bufferedImage, x, y, x + tileSize , y + tileSize, 0, 0, 200, 200, null);
    }
}

class HeaderPanel extends JPanel implements ActionListener{
    BoardFrame boardFrame;
    BoardPanel boardPanel;
    static TextField row;
    static TextField col;
    TextField totalMines;
    static JCheckBox linkTiles;
    Button scan;

    public HeaderPanel(BoardFrame boardFrame, BoardPanel boardPanel) {
        this.boardFrame = boardFrame;
        this.boardPanel = boardPanel;

        this.setMinimumSize(new Dimension(1000, 100));
        row = new TextField("1", 3);
        col = new TextField("1", 3);
        this.totalMines = new TextField("40", 3);
        this.linkTiles = new JCheckBox("Link tile size");
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

        try {
            Toolkit.getDefaultToolkit().addAWTEventListener(new CapsLockListener(), AWTEvent.KEY_EVENT_MASK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (row.getText().equals(col.getText())) {
            linkTiles.setSelected(true);
        }
    }

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
                            System.out.println("Caps Lock key pressed. Simulating Scan button press.");
                            System.out.println("Scannning additional pylons with capslock");
                            try {
                                BoardGui.scanNewImage();
                            } catch (IOException e) {
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

