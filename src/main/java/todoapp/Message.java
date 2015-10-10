package todoapp;

public enum Message {
	
	HELLO(0, "Welcome to TODOAPP. Use \"help\" for list of available commands."),
	BYE(1, "Bye dude! Don't be lazy..."),
	NO_COMMAND_FOUND(2, "There is no such command. Use \"help\"."),
	NO_REC_FOUND(3, "No records found. To add a record use command \"add\"."),
	COMP_REMOVED(4, "All completed records have been removed."),
	ALL_REC_REMOVED(5, "All records have been removed."),
	ALL_REC_COMPLETED(6, "All records have been marked with \"done\"."),
	INC_PARAM_FOUND(7, "Incorrect parameters found. Use \"help\"."),
	NOTHING_TO_ADD(8, "Nothing to add. See \"help\"."),
	DONT_FORG(9, "Don't forget to use \"exit\" when you finished to save your changes."),
	
	//Help message
	HELP(10,"\nDescription of available commands:\n\n"
			+ "show - shows all records that you've added,\n"
			+ "show [-m, -unm] - shows only completed records or only incompleted,\n"
			+ "add [word word...] - adds one or more words to list. Used for one-word tasks,\n"
			+ "add [\"task\" \"task\"...] - adds one or more tasks to list,\n"
			+ "add [\"task\" \"task\" word word...] - you can combine addition approaches,\n"
			+ "done [index index...] - marks tasks with specified indexes with \"done\",\n"
			+ "done [-a] - marks all the tasks with \"done\",\n"
			+ "rm [index index...] - removes tasks with specified indexes from list,\n"
			+ "rm [-m] - removes only completed tasks from the list,\n"
			+ "rm [-a] - removes all tasks from the list,\n"
			+ "exit - saves state of list and terminates the program. Don't forget to use if you want to save your changes.\n\n"
			+ "Definitions of command units:\n\n"
			+ "-a - flag which means \"all\",\n"
			+ "-m - flag which means \"completed tasks\" or \"marked tasks\",\n"
			+ "-unm - flag which means \"incopmleted tasks\" or \"unmarked tasks\",\n\n"
			+ "index - it's a digit or a number,\n"
			+ "word - it may be a character or combination of characters without spaces,\n"
			+ "task - it's a combinations of words splitted by spaces. Must be concluded in quotes.");
	
	private final int code;
	private final String message;
	
	private Message(int code, String message){
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	@Override
	public String toString() {
		return message;
	}

}
