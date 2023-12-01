package Minesweeper_Solver.analyzer;

public class NoInterrupt implements InterruptCheck {
    @Override
    public boolean isInterrupted() {
            return false;
        }
}

