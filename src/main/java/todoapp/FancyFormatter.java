package todoapp;

import java.util.Arrays;
import java.util.List;

public class FancyFormatter implements Formatter<Record> {
	
	@Override
	public String format(List<Record> records) {
		String[] indexes = new String[records.size()];
		String[] tasks = new String[records.size()];
		String[] time = new String[records.size()];
		String[] dones = new String[records.size()];
		
		for (int i = 0; i < records.size(); i++) {
			indexes[i] = records.get(i).getIndex().toString();
			tasks[i] = records.get(i).getContent();
			time[i] = records.get(i).getTimeAddedAsString();
			dones[i] = (records.get(i).isCompleted() == true) ? "+" : "";
		}
		
		StringBuilder table = new StringBuilder();
		
		String firstRow = formRow("#", indexes);
		String secondRow = formRow("Task", tasks);
		String thirdRow = formRow("Time", time);
		String fourthRow = formRow("Done", dones);
		
		String rp = "\\r?\\n";
		int counter = firstRow.split(rp).length;
		
		for (int i = 0; i < counter; i++) {
			table.append(firstRow.split(rp)[i]);
			table.append(secondRow.split(rp)[i]);
			table.append(thirdRow.split(rp)[i]);
			table.append(fourthRow.split(rp)[i]);
			table.append("\r\n");
		}
		
		return table.toString();
	}

	private String formRow(String title, String[] data) {
		int maxWidth = 0;
		int[] width = new int[data.length + 1];
		
		width[0] = title.length();

		int count = 1;
		for (int i = 0; i < data.length; i++) {
			width[count++] = data[i].length();
		}

		maxWidth = Arrays.stream(width).max().getAsInt();

		StringBuilder sb = new StringBuilder();

		sb.append(createDelimiter(maxWidth, '-'));
		sb.append("| ").append(title);
		sb.append(createSpaces(maxWidth > width[0]?maxWidth-width[0]:0));
		sb.append(" |").append("\n");
		sb.append(createDelimiter(maxWidth, '-'));

		for (int i = 0; i < data.length; i++) {
			sb.append("| ").append(data[i]);
			sb.append(createSpaces(maxWidth - data[i].length()));//!
			sb.append(" |").append("\n");
			sb.append(createDelimiter(maxWidth, '-'));
		}
		
		return sb.toString();
	}


	private String createSpaces(int number) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < number; i++) {
			sb.append(" ");
		}

		return sb.toString();
	}

	private String createDelimiter(int width, char pattern) {
		// because of additional spaces in the start and in the end of line
		width = width + 2;

		StringBuilder sb = new StringBuilder();
		sb.append("+");
		for (int i = 0; i < width; i++) {
			sb.append(pattern);
		}
		sb.append("+\n");

		return sb.toString();
	}
}
