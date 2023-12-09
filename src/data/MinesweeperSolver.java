package src.data;

public interface MinesweeperSolver {
    void solveBoard(Tile[][] board, int hiddenMines);

     default void displayBoard(Tile[][] board) {
         for (Tile[] rows : board) {
             for (Tile col : rows) {
                 double prob = col.getProbability();
                 if (prob == 1) {
                     System.out.print("!  ");
                 } else if (prob >= 0.5) {
                     System.out.print("?  ");
                 } else {
                     System.out.print(col.getState().getValue() + "  ");
                 }
             }
             System.out.println();
         }
    }

    default void displayProbability(Tile[][] board) {
        for (Tile[] rows : board) {
            for (Tile col : rows) {
                System.out.printf("%.2f ", col.getProbability());
            }
            System.out.println();
        }
    }
}
