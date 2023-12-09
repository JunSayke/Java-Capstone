package src.data.solver.advanced.detail;

public interface ProxyProvider<T> {

	FieldProxy<T> getProxyFor(T field);
	
}
