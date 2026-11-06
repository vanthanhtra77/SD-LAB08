package chapter6.exercise16;

import java.util.ArrayList;
import java.util.List;
import chapter6.common.UndoableCommand;

/**
 * Exercise 16: CommandProcessor abstraction.
 * Encapsulates the execution and storage of commands, providing an undoLast method.
 *
 * Law of Demeter (LoD):
 * Client code never accesses the internal list of commands or inspects elements within it.
 * The client only calls consume() and undoLast() directly on the CommandProcessor.
 */
public class CommandProcessor {

	private final List<UndoableCommand> aCommands = new ArrayList<>();

	/**
	 * Consumes a command by executing it and recording it into the history stack.
	 *
	 * @param pCommand The command to execute and store. Must not be null.
	 */
	public void consume(UndoableCommand pCommand) {
		assert pCommand != null;
		pCommand.execute();
		aCommands.add(pCommand);
	}

	/**
	 * Undoes the most recently executed command and removes it from the history stack.
	 */
	public void undoLast() {
		assert !aCommands.isEmpty() : "No command to undo";
		if (!aCommands.isEmpty()) {
			// Compatible with Java 17+ (equivalent to removeLast() in Java 21+)
			UndoableCommand command = aCommands.remove(aCommands.size() - 1);
			command.undo();
		}
	}

	/**
	 * @return true if there is at least one command in history to undo.
	 */
	public boolean canUndo() {
		return !aCommands.isEmpty();
	}

	/**
	 * @return the number of executed commands currently in history.
	 */
	public int historySize() {
		return aCommands.size();
	}
}
