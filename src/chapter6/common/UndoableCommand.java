package chapter6.common;

/**
 * Command interface supporting undo operations.
 */
public interface UndoableCommand extends Command {
	/**
	 * Reverts the effect of the execute method.
	 */
	void undo();
}
