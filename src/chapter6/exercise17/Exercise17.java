package chapter6.exercise17;

import chapter6.common.Concert;
import chapter6.common.Day;
import chapter6.common.Movie;
import chapter6.exercise15.Program;

/**
 * Exercise 17 Demo:
 * Demonstrates undoing and redoing commands using CommandProcessor.
 */
public class Exercise17 {

	public static void main(String[] args) {
		System.out.println("=== EXERCISE 17 DEMO: CommandProcessor with Redo ===");
		Program program = new Program();
		CommandProcessor processor = new CommandProcessor();

		// 1. Execute sequence of commands
		System.out.println("--- 1. Add Movie on Tuesday ---");
		processor.consume(program.createAddCommand(new Movie("Pulp Fiction", 1994, 154), Day.TUESDAY));
		System.out.println(program);

		System.out.println("--- 2. Add Concert on Thursday ---");
		processor.consume(program.createAddCommand(new Concert("Symphony No. 9", "Beethoven", 70), Day.THURSDAY));
		System.out.println(program);

		// 2. Undo
		System.out.println("=== UNDO OPERATIONS ===");
		System.out.println("--- Undo Thursday Concert ---");
		processor.undoLast();
		System.out.println(program);

		System.out.println("--- Undo Tuesday Movie ---");
		processor.undoLast();
		System.out.println(program);

		// 3. Redo
		System.out.println("=== REDO OPERATIONS ===");
		System.out.println("--- Redo Tuesday Movie ---");
		processor.redoLast();
		System.out.println(program);

		System.out.println("--- Redo Thursday Concert ---");
		processor.redoLast();
		System.out.println(program);

		System.out.println("Can redo further? " + processor.canRedo());
		System.out.println("Exercise 17 completed successfully!");
	}
}
