package chapter6.exercise18;

import chapter6.common.Concert;
import chapter6.common.Day;
import chapter6.common.Movie;

/**
 * Exercise 18 Demo:
 * Demonstrates the encapsulated Program interface:
 * Client code calls add(), remove(), clear(), undoLast(), redoLast() directly on Program.
 * Client does not manage commands or stacks directly.
 */
public class Exercise18 {

	public static void main(String[] args) {
		System.out.println("=== EXERCISE 18 DEMO: Encapsulated Program with Undo/Redo ===");

		// Client injects a CommandProcessor implementation (or uses default constructor)
		Program program = new Program(new BasicCommandProcessor());

		System.out.println("--- 1. Calling program.add(Movie 1, Monday) ---");
		program.add(new Movie("The Dark Knight", 2008, 152), Day.MONDAY);
		System.out.println(program);

		System.out.println("--- 2. Calling program.add(Concert 1, Friday) ---");
		program.add(new Concert("Live at Wembley", "Queen", 120), Day.FRIDAY);
		System.out.println(program);

		System.out.println("--- 3. Calling program.remove(Monday) ---");
		program.remove(Day.MONDAY);
		System.out.println(program);

		System.out.println("--- 4. Calling program.clear() ---");
		program.clear();
		System.out.println(program);

		System.out.println("=== TESTING UNDO DIRECTLY ON PROGRAM ===");
		System.out.println("--- Undo Clear ---");
		program.undoLast();
		System.out.println(program);

		System.out.println("--- Undo Remove Monday ---");
		program.undoLast();
		System.out.println(program);

		System.out.println("=== TESTING REDO DIRECTLY ON PROGRAM ===");
		System.out.println("--- Redo Remove Monday ---");
		program.redoLast();
		System.out.println(program);

		System.out.println("--- Redo Clear ---");
		program.redoLast();
		System.out.println(program);

		System.out.println("Exercise 18 completed successfully!");
	}
}
