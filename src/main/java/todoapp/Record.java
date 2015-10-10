package todoapp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Record implements Serializable, Comparable<Record> {

	private static final long serialVersionUID = -6963793433039035528L;

	private Integer index;
	private String content;
	private LocalDateTime timeAdded;
	private Boolean completed;

	public Record(String value) {
		setTimeAddedAsCurrent();
		this.content = value;
		this.completed = false;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTimeAddedAsString() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E, dd-MM-yy, HH:mm");
		return timeAdded.format(dtf);
	}

	public LocalDateTime getTimeAdded() {
		return timeAdded;
	}

	private void setTimeAddedAsCurrent() {
		this.timeAdded = LocalDateTime.now();
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Override
	public int compareTo(Record r) {
		return this.getIndex().compareTo(r.getIndex());
	}

}
