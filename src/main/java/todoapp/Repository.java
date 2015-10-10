package todoapp;

import java.util.List;

public interface Repository<T> {
	
	public void save(final List<T> data);

	public List<T> load();
	
}
