package Minesweeper_Solver.analyzer;

import java.util.List;
import java.util.Random;

import Minesweeper_Solver.analyzer.detail.DetailedResults;
import Minesweeper_Solver.analyzer.detail.NeighborFind;

public interface AnalyzeResult<T> {
	AnalyzeResult<T> cloneAddSolve(List<RuleConstraint<T>> extraRules);
	
	List<T> getFields();
	
	FieldGroup<T> getGroupFor(T field);
	
	List<FieldGroup<T>> getGroups();
	
	List<RuleConstraint<T>> getOriginalRules();
	
	double getProbabilityOf(List<RuleConstraint<T>> extraRules);
	
	List<RuleConstraint<T>> getRules();
	
	List<T> getSolution(double solution);

	Iterable<Solution<T>> getSolutionIteration();

	List<Solution<T>> getSolutions();

	/**
	 * Get the total number of combinations of Field placements.
	 * @return The number of combinations for the analyze. 0 if the analyze is impossible.
	 */
	double getTotal();

	List<T> randomSolution(Random random);

	DetailedResults<T> analyzeDetailed(NeighborFind<T> neighborStrategy);
}
