package src.data.solver.advanced.listener;

import src.data.solver.advanced.FieldGroup;

public interface RuleListener<T> {

    void onValueSet(FieldGroup<T> group, int value);

}
