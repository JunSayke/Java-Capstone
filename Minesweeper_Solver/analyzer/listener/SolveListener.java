package Minesweeper_Solver.analyzer.listener;

import Minesweeper_Solver.analyzer.FieldGroup;

public interface SolveListener<T> {

    void onValueSet(Analyze<T> analyze, FieldGroup<T> group, int value);

}
