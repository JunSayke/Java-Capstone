package src.data.solver.advanced;

public class NoInterrupt implements InterruptCheck {
    @Override
    public boolean isInterrupted() {
            return false;
        }
}

