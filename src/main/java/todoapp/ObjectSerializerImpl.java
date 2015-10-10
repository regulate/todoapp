package todoapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.List;

public class ObjectSerializerImpl<T> implements Repository<T> {

	private static final File REPO_PATH = new File(Paths.get("").toAbsolutePath().toString() + "/data/records.dat");

	@Override
	public void save(final List<T> data) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(REPO_PATH));
			out.writeObject(data);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> load() {
		List<T> loaded = null;
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(REPO_PATH));
			// cast is ok
			loaded = (List<T>) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return loaded;
	}

}
