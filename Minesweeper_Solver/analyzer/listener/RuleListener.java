package Minesweeper_Solver.analyzer.listener;

import Minesweeper_Solver.analyzer.FieldGroup;
import Minesweeper_Solver.analyzer.GameAnalyze;

public interface RuleListener<T> {

    void onValueSet(FieldGroup<T> group, int value);

}
