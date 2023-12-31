package src.data.solver.advanced;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class GroupValues<T> {
	private int bufferedHash = 0;
	
	private final Map<FieldGroup<T>, Integer> data;
	
	public GroupValues(GroupValues<T> values) {
		this.data = new HashMap<FieldGroup<T>, Integer>(values.data);
	}

	public GroupValues() {
		this.data = new HashMap<FieldGroup<T>, Integer>();
	}

	@Override
	public int hashCode() {
	    if (bufferedHash == 0) {
	        this.bufferedHash = data.hashCode();
	    }

	    return this.bufferedHash;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof GroupValues<?>)) {
			return false;
		}
		GroupValues<?> other = (GroupValues<?>) arg0;
		return data.equals(other.data);
	}
	
	public int calculateHash() {
		this.bufferedHash = 0;
		return this.hashCode();
	}

	public Set<Entry<FieldGroup<T>, Integer>> entrySet() {
		return data.entrySet();
	}

	public Set<FieldGroup<T>> keySet() {
		return data.keySet();
	}
	
	public void put(FieldGroup<T> group, int value) {
		data.put(group, value);
	}
	
	public Integer get(FieldGroup<T> group) {
		return data.get(group);
	}
	
	public int size() {
		return data.size();
	}

	public boolean isEmpty() {
		return data.isEmpty();
	}
	
	public void remove(FieldGroup<T> group) {
		data.remove(group);
	}

	@Override
	public String toString() {
		return data.toString();
	}
	
}
