package todoapp;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO: exit dialog
public class CommandController {

	/***************************************************/
	public static final String COMMAND_SHOW = "show";
	public static final String COMMAND_ADD = "add";
	public static final String COMMAND_REMOVE = "rm";
	public static final String COMMAND_DONE = "done";
	public static final String COMMAND_HELP = "help";
	public static final String COMMAND_EXIT = "exit";

	public static final String FLAG_ALL = "-a";
	public static final String FLAG_MARKED = "-m";
	public static final String FLAG_UNMARKED = "-unm";
	/***************************************************/

	private RecordService recordService;

	private InputTaker inputTaker = InputTaker.getInstance();

	public CommandController(RecordService recordService) {
		setRecordService(recordService);
	}

	public RecordService getRecordService() {
		return recordService;
	}

	public void setRecordService(RecordService recordService) {
		if (recordService == null) {
			throw new IllegalArgumentException();
		}
		this.recordService = recordService;
	}

	public InputTaker getInputTaker() {
		return this.inputTaker;
	}

	private static class InputTaker {

		private static final InputTaker INSTANCE = new InputTaker();

		private InputTaker() {
		}

		public static InputTaker getInstance() {
			return INSTANCE;
		}

		// returns an array of values without command
		public String[] getInputValues(String[] in) {
			String[] values = new String[in.length - 1];
			int c = 0;

			for (int i = 1; i < in.length; i++) {
				values[c++] = in[i];
			}
			return values;
		}

		// splits input to elements
		public String[] parseInput(String in) {
			ArrayList<String> splitted = null;
			// searching for quotes, in cases |add "test"| or |add "test" "test"
			// ...|
			for (int i = 0; i < in.length(); i++) {
				char c = in.charAt(i);
				if (c == '"') {
					splitted = new ArrayList<>();
					Matcher m = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'").matcher(in);
					while (m.find()) {
						splitted.add(m.group().replace("\"", ""));
					}
					break;
				}
			}
			// if no quotes found split by space
			if (splitted == null) {
				String[] parts = in.split("\\s+");
				return parts;
			}
			return (String[]) splitted.toArray(new String[splitted.size()]);
		}

		// checks if input elements contain non-digit symbols
		public boolean hasSymbols(String[] input) {
			char[] chars;
			for (int i = 0; i < input.length; i++) {
				chars = input[i].trim().toCharArray();
				for (int j = 0; j < chars.length; j++) {
					if (!Character.isDigit(chars[j])) {
						return true;
					}
				}
			}
			return false;
		}

		// converts indexes from strings to Integers
		public Integer[] parseArrayToInt(String[] input) {
			Integer nums[] = new Integer[input.length];

			for (int i = 0; i < nums.length; i++) {
				nums[i] = Integer.parseInt(input[i]);
			}

			return nums;
		}

	}

	public static void main(String[] args) {
		CommandController controller = new CommandController(new RecordService());

		CommandController.InputTaker inputTaker = controller.getInputTaker();

		Scanner sc = new Scanner(System.in);
		String[] input = null;
		String[] values = null;

		System.out.println(Message.HELLO);
		System.out.println(Message.DONT_FORG);
		do {
			// taking input
			input = inputTaker.parseInput(sc.nextLine());
			values = inputTaker.getInputValues(input);
			// analyzing command
			switch (input[0]) {
			case COMMAND_ADD:
				// if there is something to add it will add
				if (values.length > 0) {
					controller.getRecordService().add(values);
					// if there is nothing to add
				} else {
					System.out.println(Message.NOTHING_TO_ADD);
				}
				break;
			case COMMAND_REMOVE:
				// flag to remove all records
				if (values[0].equals(FLAG_ALL)) {
					controller.getRecordService().removeAll();
					// flag to remove marked records
				} else if (values[0].equals(FLAG_MARKED)) {
					controller.getRecordService().removeCompleted();
					// in case of deletion by index |rm 1|
				} else if (!inputTaker.hasSymbols(values)) {
					controller.getRecordService().remove(inputTaker.parseArrayToInt(values));
					// in case of incorrect input such as characters
				} else {
					System.out.println(Message.INC_PARAM_FOUND);
				}
				break;
			case COMMAND_SHOW:
				// show all records, just |show|
				if (values.length == 0) {
					controller.getRecordService().show();
					// flag to show only marked records
				} else if (values[0].equals(FLAG_MARKED)) {
					controller.getRecordService().show(true);
					// flag to show only unmarked records
				} else if (values[0].equals(FLAG_UNMARKED)) {
					controller.getRecordService().show(false);
					// in case of incorrect input
				} else {
					System.out.println(Message.INC_PARAM_FOUND);
				}
				break;
			case COMMAND_HELP:
				// list commands with description
				System.out.println(Message.HELP);
				break;
			case COMMAND_DONE:
				// flag to mark all records with done
				if (values[0].equals(FLAG_ALL)) {
					controller.getRecordService().completeAll();
					// in case of marking by index |done 1 2 3...|
				} else if (!inputTaker.hasSymbols(values)) {
					controller.getRecordService().mark(inputTaker.parseArrayToInt(values));
					// in case of incorrect input such as characters
				} else {
					System.out.println(Message.INC_PARAM_FOUND);
				}
				break;
			case COMMAND_EXIT:
				System.out.println(Message.BYE);
				break;
			default:
				System.out.println(Message.NO_COMMAND_FOUND);
				break;
			}
		} while (!input[0].equals(COMMAND_EXIT));
		controller.getRecordService().saveState();
		sc.close();
		System.exit(0);
	}
}
