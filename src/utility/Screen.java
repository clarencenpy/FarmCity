package utility;

import org.fusesource.jansi.*;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

/**
 * Wrapper class that provides methods for formatting output to the console.
 * Can be used in place of System.out.println()
 * Options can be set via configuration.txt
 */
public class Screen {
	static {
		AnsiConsole.systemInstall();
	}

	private static boolean clearScreen = Env.properties.getProperty("clearScreen").equals("true");
	private static boolean color = Env.properties.getProperty("color").equals("true");

	/**
	 * Clears the screen by printing lines corresponding to the console height. If clearScreen option is off, println instead.
	 */
	public static void clear() {
		if (clearScreen) {
			System.out.println(ansi().eraseScreen());
		} else {
			System.out.println("\n");
		}
	}

	/**
	 * Prints a string in green if color=true
	 * @param s the string to be printed
	 */
	public static void green(String s) {
		if (color) {
			System.out.print(ansi().fg(GREEN).a(s).reset());
		} else {
			System.out.print(s);
		}
	}

	/**
	 * Prints a string in red if color=true
	 * @param s the string to be printed
	 */
	public static void redln(String s) {
		if (color) {
			System.out.println(ansi().fg(RED).a(s).reset());
		} else {
			System.out.println(s);
		}
	}

	/**
	 * Prints a string in green if color=true
	 * @param s the string to be printed
	 */
	public static void greenln(String s) {
		if (color) {
			System.out.println(ansi().fg(GREEN).a(s).reset());
		} else {
			System.out.println(s);
		}
	}

	/**
	 * Prints a string in yellow if color=true
	 * @param s the string to be printed
	 */
	public static void yellowln(String s) {
		if (color) {
			System.out.println(ansi().fg(YELLOW).a(s).reset());
		} else {
			System.out.println(s);
		}
	}

	/**
	 * Prints a string in cyan if color=true
	 * @param s the string to be printed
	 */
	public static void cyanln(String s) {
		if (color) {
			System.out.println(ansi().fg(CYAN).a(s).reset());
		} else {
			System.out.println(s);
		}
	}

}