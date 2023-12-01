package Minesweeper_Solver.analyzer.detail;

public interface ProxyProvider<T> {

	FieldProxy<T> getProxyFor(T field);
	
}
