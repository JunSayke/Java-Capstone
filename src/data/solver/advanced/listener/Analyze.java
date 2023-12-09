package src.data.solver.advanced.listener;

import src.data.solver.advanced.GroupValues;
import src.data.solver.advanced.RuleConstraint;

public interface Analyze<T> {

    int getDepth();
    void addRule(RuleConstraint<T> stringFieldRule);
    GroupValues<T> getKnownValues();

}
