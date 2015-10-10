package todoapp;

import java.util.List;

public interface Formatter<T> {
	public String format(List<T> data);
}
