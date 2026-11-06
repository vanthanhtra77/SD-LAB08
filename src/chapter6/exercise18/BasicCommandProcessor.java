package chapter6.exercise18;

import java.util.ArrayList;
import java.util.List;
import chapter6.common.UndoableCommand;

/**
 * Default implementation of CommandProcessor with undo and redo stacks.
 */
public class BasicCommandProcessor implements CommandProcessor {

	private final List<UndoableCommand> aExecutedCommands = new ArrayList<>();
	private final List<UndoableCommand> aUndoneCommands = new ArrayList<>();

	@Override
	public void consume(UndoableCommand pCommand) {
		assert pCommand != null;
		pCommand.execute();
		aExecutedCommands.add(pCommand);
		aUndoneCommands.clear();
	}

	@Override
	public void undoLast() {
		assert !aExecutedCommands.isEmpty() : "No command to undo";
		if (!aExecutedCommands.isEmpty()) {
			UndoableCommand command = aExecutedCommands.remove(aExecutedCommands.size() - 1);
			command.undo();
			aUndoneCommands.add(command);
		}
	}

	@Override
	public void redoLast() {
		assert !aUndoneCommands.isEmpty() : "No command to redo";
		if (!aUndoneCommands.isEmpty()) {
			UndoableCommand command = aUndoneCommands.remove(aUndoneCommands.size() - 1);
			command.execute();
			aExecutedCommands.add(command);
		}
	}

	@Override
	public boolean canUndo() {
		return !aExecutedCommands.isEmpty();
	}

	@Override
	public boolean canRedo() {
		return !aUndoneCommands.isEmpty();
	}
}
