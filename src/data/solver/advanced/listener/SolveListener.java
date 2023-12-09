package src.data.solver.advanced.listener;

import src.data.solver.advanced.FieldGroup;

public interface SolveListener<T> {

    void onValueSet(Analyze<T> analyze, FieldGroup<T> group, int value);

}
