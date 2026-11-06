package chapter6.exercise17;

import java.util.ArrayList;
import java.util.List;
import chapter6.common.UndoableCommand;

/**
 * Exercise 17: CommandProcessor with Redo capabilities.
 * Uses a two-stack model (aExecutedCommands and aUndoneCommands).
 */
public class CommandProcessor {

	private final List<UndoableCommand> aExecutedCommands = new ArrayList<>();
	private final List<UndoableCommand> aUndoneCommands = new ArrayList<>();

	/**
	 * Consumes and executes a new command.
	 * When a new command is executed, the redo stack is cleared to maintain history consistency.
	 *
	 * @param pCommand The command to execute. Must not be null.
	 */
	public void consume(UndoableCommand pCommand) {
		assert pCommand != null;
		pCommand.execute();
		aExecutedCommands.add(pCommand);
		aUndoneCommands.clear();
	}

	/**
	 * Undoes the most recently executed command and moves it to the redo stack.
	 */
	public void undoLast() {
		assert !aExecutedCommands.isEmpty() : "No command to undo";
		if (!aExecutedCommands.isEmpty()) {
			UndoableCommand command = aExecutedCommands.remove(aExecutedCommands.size() - 1);
			command.undo();
			aUndoneCommands.add(command);
		}
	}

	/**
	 * Redoes the most recently undone command and moves it back to the executed stack.
	 */
	public void redoLast() {
		assert !aUndoneCommands.isEmpty() : "No command to redo";
		if (!aUndoneCommands.isEmpty()) {
			UndoableCommand command = aUndoneCommands.remove(aUndoneCommands.size() - 1);
			command.execute();
			aExecutedCommands.add(command);
		}
	}

	public boolean canUndo() {
		return !aExecutedCommands.isEmpty();
	}

	public boolean canRedo() {
		return !aUndoneCommands.isEmpty();
	}

	public int executedCount() {
		return aExecutedCommands.size();
	}

	public int undoneCount() {
		return aUndoneCommands.size();
	}
}
