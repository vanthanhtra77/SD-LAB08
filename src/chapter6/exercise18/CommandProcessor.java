package chapter6.exercise18;

import chapter6.common.UndoableCommand;

/**
 * Exercise 18: Interface for command processing abstraction.
 * Decouples the Program class from specific command history management implementations.
 */
public interface CommandProcessor {

	/**
	 * Consumes and executes an undoable command.
	 */
	void consume(UndoableCommand pCommand);

	/**
	 * Undoes the last executed command.
	 */
	void undoLast();

	/**
	 * Redoes the last undone command.
	 */
	void redoLast();

	/**
	 * @return true if an undo operation is available.
	 */
	boolean canUndo();

	/**
	 * @return true if a redo operation is available.
	 */
	boolean canRedo();
}
