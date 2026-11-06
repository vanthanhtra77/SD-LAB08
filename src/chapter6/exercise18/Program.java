package chapter6.exercise18;

import java.util.EnumMap;
import chapter6.common.Day;
import chapter6.common.NullShow;
import chapter6.common.Show;
import chapter6.common.UndoableCommand;

/**
 * Exercise 18: Program class encapsulating CommandProcessor operations.
 *
 * Design Solution to Avoid God Class:
 * Instead of embedding command stack management into Program, Program aggregates
 * an instance of CommandProcessor (Dependency Injection via constructor).
 *
 * Program focuses purely on managing weekly shows (Domain model) and delegates
 * the execution, storage, undo, and redo of commands to the CommandProcessor.
 * All command factory methods are kept private.
 */
public class Program {

	private final EnumMap<Day, Show> aShows = new EnumMap<>(Day.class);
	private final CommandProcessor aProcessor;

	/**
	 * Constructor with Dependency Injection for CommandProcessor.
	 *
	 * @param pProcessor The command processor strategy to delegate to.
	 */
	public Program(CommandProcessor pProcessor) {
		assert pProcessor != null : "CommandProcessor must not be null";
		aProcessor = pProcessor;
		clearSchedule();
	}

	/**
	 * Default constructor using BasicCommandProcessor.
	 */
	public Program() {
		this(new BasicCommandProcessor());
	}

	// ---------------- Public Facade API for Clients ----------------

	/**
	 * Adds a show on the specified day. Automatically creates and consumes an UndoableCommand.
	 */
	public void add(Show pShow, Day pDay) {
		assert pShow != null && pDay != null;
		aProcessor.consume(createAddCommand(pShow, pDay));
	}

	/**
	 * Removes any show from the specified day. Automatically creates and consumes an UndoableCommand.
	 */
	public void remove(Day pDay) {
		assert pDay != null;
		aProcessor.consume(createRemoveCommand(pDay));
	}

	/**
	 * Clears the entire program. Automatically creates and consumes an UndoableCommand.
	 */
	public void clear() {
		aProcessor.consume(createClearCommand());
	}

	/**
	 * Undoes the last action performed on this program.
	 */
	public void undoLast() {
		aProcessor.undoLast();
	}

	/**
	 * Redoes the last undone action on this program.
	 */
	public void redoLast() {
		aProcessor.redoLast();
	}

	public boolean canUndo() {
		return aProcessor.canUndo();
	}

	public boolean canRedo() {
		return aProcessor.canRedo();
	}

	public Show get(Day pDay) {
		assert pDay != null;
		return aShows.get(pDay);
	}

	// ---------------- Private Command Factory Methods ----------------

	private UndoableCommand createAddCommand(Show pShow, Day pDay) {
		return new UndoableCommand() {
			private Show previousShow;

			@Override
			public void execute() {
				previousShow = aShows.get(pDay);
				aShows.put(pDay, pShow);
			}

			@Override
			public void undo() {
				if (previousShow != null && !previousShow.equals(NullShow.getInstance())) {
					aShows.put(pDay, previousShow);
				} else {
					aShows.put(pDay, NullShow.getInstance());
				}
			}

			@Override
			public String toString() {
				return "Add [" + pShow.description() + "] on " + pDay;
			}
		};
	}

	private UndoableCommand createRemoveCommand(Day pDay) {
		return new UndoableCommand() {
			private Show removedShow;

			@Override
			public void execute() {
				removedShow = aShows.get(pDay);
				aShows.put(pDay, NullShow.getInstance());
			}

			@Override
			public void undo() {
				if (removedShow != null && !removedShow.equals(NullShow.getInstance())) {
					aShows.put(pDay, removedShow);
				}
			}

			@Override
			public String toString() {
				return "Remove show on " + pDay;
			}
		};
	}

	private UndoableCommand createClearCommand() {
		return new UndoableCommand() {
			private EnumMap<Day, Show> snapshot;

			@Override
			public void execute() {
				snapshot = new EnumMap<>(aShows);
				clearSchedule();
			}

			@Override
			public void undo() {
				if (snapshot != null) {
					aShows.putAll(snapshot);
				}
			}

			@Override
			public String toString() {
				return "Clear program";
			}
		};
	}

	private void clearSchedule() {
		for (Day day : Day.values()) {
			aShows.put(day, NullShow.getInstance());
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Day day : Day.values()) {
			Show show = aShows.get(day);
			sb.append(String.format("%-10s : %s%n", day.name(), show.description()));
		}
		return sb.toString();
	}
}
