package chapter6.exercise16;

import chapter6.common.Day;
import chapter6.common.Movie;
import chapter6.exercise15.Program;

/**
 * Exercise 16 Demo:
 * Demonstrates the CommandProcessor executing commands and undoing the last command,
 * strictly abiding by the Law of Demeter.
 */
public class Exercise16 {

	public static void main(String[] args) {
		System.out.println("=== EXERCISE 16 DEMO: CommandProcessor with Undo ===");
		Program program = new Program();
		CommandProcessor processor = new CommandProcessor();

		// Execute commands via the processor
		System.out.println("--- 1. Consuming Add Movie 1 ---");
		processor.consume(program.createAddCommand(new Movie("The Matrix", 1999, 136), Day.FRIDAY));
		System.out.println(program);

		System.out.println("--- 2. Consuming Add Movie 2 ---");
		processor.consume(program.createAddCommand(new Movie("The Godfather", 1972, 175), Day.SATURDAY));
		System.out.println(program);

		System.out.println("--- 3. Consuming Remove Friday show ---");
		processor.consume(program.createRemoveCommand(Day.FRIDAY));
		System.out.println(program);

		// Undo last commands via the processor
		System.out.println("=== UNDOING VIA PROCESSOR ===");

		System.out.println("--- Undo Last (Restores Friday show) ---");
		processor.undoLast();
		System.out.println(program);

		System.out.println("--- Undo Last (Removes Saturday show) ---");
		processor.undoLast();
		System.out.println(program);

		System.out.println("--- Undo Last (Removes Friday show) ---");
		processor.undoLast();
		System.out.println(program);

		System.out.println("Can undo further? " + processor.canUndo());
		System.out.println("Exercise 16 completed successfully!");
	}
}
