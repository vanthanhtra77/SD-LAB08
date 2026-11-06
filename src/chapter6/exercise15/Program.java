package chapter6.exercise15;

import java.util.EnumMap;
import chapter6.common.Day;
import chapter6.common.NullShow;
import chapter6.common.Show;
import chapter6.common.UndoableCommand;

/**
 * Exercise 15: Program class with factory methods returning UndoableCommand instances.
 * Each command captures the necessary state before execution to enable complete undo.
 */
public class Program {

	private final EnumMap<Day, Show> aShows = new EnumMap<>(Day.class);

	public Program() {
		clearInternal();
	}

	/**
	 * Factory method: creates an UndoableCommand that adds a show to a specified day.
	 * If a show was already present on that day, undoing restores that previous show.
	 */
	public UndoableCommand createAddCommand(Show pShow, Day pDay) {
		assert pShow != null && pDay != null;
		return new UndoableCommand() {
			private Show previousShow;

			@Override
			public void execute() {
				previousShow = aShows.get(pDay);
				addInternal(pShow, pDay);
			}

			@Override
			public void undo() {
				if (previousShow != null && !previousShow.equals(NullShow.getInstance())) {
					addInternal(previousShow, pDay);
				} else {
					removeInternal(pDay);
				}
			}

			@Override
			public String toString() {
				return "Add [" + pShow.description() + "] on " + pDay;
			}
		};
	}

	/**
	 * Factory method: creates an UndoableCommand that removes a show from a specified day.
	 * Undoing adds the removed show back.
	 */
	public UndoableCommand createRemoveCommand(Day pDay) {
		assert pDay != null;
		return new UndoableCommand() {
			private Show removedShow;

			@Override
			public void execute() {
				removedShow = aShows.get(pDay);
				removeInternal(pDay);
			}

			@Override
			public void undo() {
				if (removedShow != null && !removedShow.equals(NullShow.getInstance())) {
					addInternal(removedShow, pDay);
				}
			}

			@Override
			public String toString() {
				return "Remove show on " + pDay;
			}
		};
	}

	/**
	 * Factory method: creates an UndoableCommand that clears all shows.
	 * Undoing restores the full schedule prior to clear.
	 */
	public UndoableCommand createClearCommand() {
		return new UndoableCommand() {
			private EnumMap<Day, Show> previousShows;

			@Override
			public void execute() {
				previousShows = new EnumMap<>(aShows);
				clearInternal();
			}

			@Override
			public void undo() {
				if (previousShows != null) {
					aShows.putAll(previousShows);
				}
			}

			@Override
			public String toString() {
				return "Clear all shows";
			}
		};
	}

	// Internal state-modifying methods kept private to enforce command pattern via client
	private void addInternal(Show pShow, Day pDay) {
		aShows.put(pDay, pShow);
	}

	private void removeInternal(Day pDay) {
		aShows.put(pDay, NullShow.getInstance());
	}

	private void clearInternal() {
		for (Day day : Day.values()) {
			aShows.put(day, NullShow.getInstance());
		}
	}

	public Show get(Day pDay) {
		assert pDay != null;
		return aShows.get(pDay);
	}

	public boolean hasShow(Day pDay) {
		return !get(pDay).equals(NullShow.getInstance());
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
