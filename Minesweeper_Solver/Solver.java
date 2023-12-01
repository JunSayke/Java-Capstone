package Minesweeper_Solver;

import Minesweeper_Solver.analyzer.factory.AbstractAnalyze;
import Minesweeper_Solver.analyzer.factory.General2DAnalyze;

import java.util.List;

public class Solver extends AbstractAnalyze {
    @Override
    protected List getAllPoints() {
        return null;
    }

    @Override
    protected boolean fieldHasRule(Object field) {
        return false;
    }

    @Override
    protected int getRemainingMinesCount() {
        return 0;
    }

    @Override
    protected List getAllUnclickedFields() {
        return null;
    }

    @Override
    protected boolean isDiscoveredMine(Object neighbor) {
        return false;
    }

    @Override
    protected int getFieldValue(Object field) {
        return 0;
    }

    @Override
    protected List getNeighbors(Object field) {
        return null;
    }

    @Override
    protected boolean isClicked(Object neighbor) {
        return false;
    }
}
