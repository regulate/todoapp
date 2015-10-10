package todoapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

public class XStreamXmlRepository implements Repository<Record> {

	private static final Logger log = Logger.getLogger(XStreamXmlRepository.class);

	private static final String XML_ROOT = "<records></records>";

	private static final String XML_REPO_PATH = "data/records.xml";

	private XStream xStream;

	public XStreamXmlRepository(HierarchicalStreamDriver hsd) {
		setupXStream(hsd);
	}

	private void setupXStream(HierarchicalStreamDriver hsd) {
		xStream = new XStream(hsd);
		xStream.registerConverter(new LocalDateTimeConverter());
		xStream.alias("records", List.class);
		xStream.alias("record", Record.class);
		xStream.aliasField("time-added", Record.class, "timeAdded");
		xStream.omitField(Record.class, "serialVersionUID");
	}

	private boolean destinationUnitExists(String path) {
		File repo = new File(path);
		return repo.exists() && !repo.isDirectory();
	}

	@Override
	public void save(final List<Record> data) {
		FileWriter out = null;

		if (!destinationUnitExists(XML_REPO_PATH)) {
			log.debug("Repo isn't found. Creating new one in " + System.getProperty("user.dir") + "/" + XML_REPO_PATH);
		}

		// if records present than store them
		if (!data.isEmpty()) {
			try {
				xStream.marshal(data, new PrettyPrintWriter(new FileWriter(XML_REPO_PATH)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// if no records found than clear file properly
			try {
				out = new FileWriter(XML_REPO_PATH);
				out.write(XML_ROOT);
				out.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
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
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Record> load() {
		List<Record> records = null;

		if (destinationUnitExists(XML_REPO_PATH)) {
			Reader reader = null;
			try {
				reader = new FileReader(XML_REPO_PATH);
				// cast is ok
				records = (List<Record>) xStream.fromXML(reader);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			// set records to empty list
			records = new ArrayList<>();
		}
		return records;
	}

}
