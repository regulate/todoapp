package todoapp;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.io.xml.StaxDriver;

public class RecordService {

	private static final Logger log = Logger.getLogger(RecordService.class);

	private List<Record> model;
	private Formatter<Record> formatter;
	private Repository<Record> repo;

	public RecordService() {
		log.debug("Init started");
		repo = new XStreamXmlRepository(new StaxDriver());
		setFormatter(new FancyFormatter());
		initState();
		log.debug("Init finished");
	}

	public List<Record> getRecords() {
		return model;
	}

	public void setRecords(List<Record> records) {
		this.model = records;
	}

	public Formatter<Record> getFormatter() {
		return formatter;
	}

	public void setFormatter(Formatter<Record> formatter) {
		this.formatter = formatter;
	}

	private void initState() {
		model = repo.load();
	}

	public void show() {
		if (model.size() > 0) {
			System.out.print(getFormatter().format(model));
		} else {
			System.out.println(Message.NO_REC_FOUND);
		}
	}

	// this one shows filtered list
	public void show(boolean marked) {
		List<Record> filtered = filter(model, marked);
		if (filtered.size() > 0) {
			System.out.print(getFormatter().format(filtered));
		} else {
			System.out.println(Message.NO_REC_FOUND);
		}
	}

	private List<Record> filter(List<Record> model, boolean marked) {
		List<Record> filtered = model;
		return filtered.stream().filter(new Predicate<Record>() {
			@Override
			public boolean test(Record t) {
				if (t.isCompleted() == marked)
					return true;
				return false;
			}
		}).collect(Collectors.toList());
	}

	public void add(String... values) {
		for (int i = 0; i < values.length; i++) {
			Record record = new Record(values[i]);

			Integer idxLast = -1;

			if (!model.isEmpty()) {
				idxLast = model.get(model.size() - 1).getIndex();
			}

			record.setIndex(++idxLast);
			model.add(record);
		}
		System.out.println(values.length + " record(s) added.");
	}

	public void remove(Integer... indexes) {
		final String del = "delete";
		operate(del, indexes);
	}

	public void mark(Integer... indexes) {
		final String mark = "mark";
		operate(mark, indexes);
	}

	private void operate(String operationName, Integer... indexes) {
		// for each index
		for (int i = 0; i < indexes.length; i++) {
			// search over all records
			for (int j = 0; j < model.size(); j++) {
				// for such index
				if (model.get(j).getIndex().equals(indexes[i])) {
					// which action to perform
					if (operationName.equals("delete")) {
						model.remove(model.get(j));
						System.out.println("Record " + indexes[i] + " has been successfuly removed.");
					} else {
						model.get(j).setCompleted(true);
						System.out.println("Record " + indexes[i] + " has been marked with \"done\".");
					}
					// will break inner loop if index found
					break;
					// checks if it was the last element of records
				} else if (j == model.size() - 1) {
					// if loop doesn't break, it means that index isn't found
					System.out.println("No record found with index " + indexes[i] + ".");
				}
			}
		}
	}

	public void removeCompleted() {
		for (int i = model.size() - 1; i > -1; i--) {
			if (model.get(i).isCompleted()) {
				model.remove(i);
			}
		}
		System.out.println(Message.COMP_REMOVED);
	}

	public void removeAll() {
		model.clear();
		System.out.println(Message.ALL_REC_REMOVED);
	}

	public void completeAll() {
		for (Record r : model) {
			r.setCompleted(true);
		}
		System.out.println(Message.ALL_REC_COMPLETED);
	}

	public void saveState() {
		repo.save(model);
	}

}
