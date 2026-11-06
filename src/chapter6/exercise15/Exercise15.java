package chapter6.exercise15;

import chapter6.common.Day;
import chapter6.common.Movie;
import chapter6.common.UndoableCommand;

/**
 * Exercise 15 Demo:
 * Adds two movies to a program, removes one, then clears the program.
 * Undoes all executed commands in reverse order and verifies state recovery.
 */
public class Exercise15 {

	public static void main(String[] args) {
		System.out.println("=== EXERCISE 15 DEMO: Undoable Commands ===");
		Program program = new Program();

		// 1. Create and execute commands
		UndoableCommand add1 = program.createAddCommand(new Movie("Inception", 2010, 148), Day.MONDAY);
		UndoableCommand add2 = program.createAddCommand(new Movie("Interstellar", 2014, 169), Day.WEDNESDAY);
		UndoableCommand remove1 = program.createRemoveCommand(Day.MONDAY);
		UndoableCommand clearCmd = program.createClearCommand();

		System.out.println("--- 1. Executing Add Movie 1 (Monday) ---");
		add1.execute();
		System.out.println(program);

		System.out.println("--- 2. Executing Add Movie 2 (Wednesday) ---");
		add2.execute();
		System.out.println(program);

		System.out.println("--- 3. Executing Remove Monday show ---");
		remove1.execute();
		System.out.println(program);

		System.out.println("--- 4. Executing Clear ---");
		clearCmd.execute();
		System.out.println(program);

		// 2. Undo all commands in reverse order
		System.out.println("=== UNDOING COMMANDS ===");

		System.out.println("--- Undo Clear ---");
		clearCmd.undo();
		System.out.println(program);

		System.out.println("--- Undo Remove Monday ---");
		remove1.undo();
		System.out.println(program);

		System.out.println("--- Undo Add Wednesday ---");
		add2.undo();
		System.out.println(program);

		System.out.println("--- Undo Add Monday ---");
		add1.undo();
		System.out.println(program);

		System.out.println("Exercise 15 completed successfully!");
	}
}
