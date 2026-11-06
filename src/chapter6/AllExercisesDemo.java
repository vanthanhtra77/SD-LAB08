package chapter6;

import chapter6.exercise15.Exercise15;
import chapter6.exercise16.Exercise16;
import chapter6.exercise17.Exercise17;
import chapter6.exercise18.Exercise18;

/**
 * Runs all exercise demos sequentially (15 through 18).
 */
public class AllExercisesDemo {

	public static void main(String[] args) {
		System.out.println("############################################################");
		System.out.println("#  DesignBook Chapter 6 - Exercises 15 to 18 Demonstrations #");
		System.out.println("############################################################\n");

		Exercise15.main(args);
		System.out.println("\n------------------------------------------------------------\n");

		Exercise16.main(args);
		System.out.println("\n------------------------------------------------------------\n");

		Exercise17.main(args);
		System.out.println("\n------------------------------------------------------------\n");

		Exercise18.main(args);
		System.out.println("\n============================================================");
		System.out.println(" All demonstrations executed successfully!");
		System.out.println("============================================================");
	}
}
