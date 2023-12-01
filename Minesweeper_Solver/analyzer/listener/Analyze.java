package Minesweeper_Solver.analyzer.listener;

import Minesweeper_Solver.analyzer.GroupValues;
import Minesweeper_Solver.analyzer.RuleConstraint;

public interface Analyze<T> {

    int getDepth();
    void addRule(RuleConstraint<T> stringFieldRule);
    GroupValues<T> getKnownValues();

}
