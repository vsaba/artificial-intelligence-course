package ui;

import java.io.IOException;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main class which loads and initializes all necessary variables, then executes
 * the requested algorithm
 * 
 * @author Vito Sabalic
 *
 */
public class Solution {

	private static List<Clause> clauses = null;

	/**
	 * The main method of the class
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length < 2 || args.length > 3) {
			System.out.println("Expected at least 2 or at most 3 arguments");
			System.exit(0);
		}

		String algorithm = args[0];

		switch (algorithm) {
		case "resolution":

			if (args.length != 2) {
				System.out.println("Incorrect number of arguments, expected algorithm and path to clauses");
				System.exit(0);
			}

			clauses = parseClauses(args[1]);
			Clause lastClause = clauses.remove(clauses.size() - 1);

			ResolutionRefutation.instance.run(clauses, lastClause);

			break;
		case "cooking":
			if (args.length != 3) {
				System.out.println(
						"Incorrect number of arguments, expeceted algorithm, path to clauses and path to commands");
			}

			clauses = parseClauses(args[1]);
			List<String> commands = extractCommands(args[2]);

			for (String line : commands) {

				String command = line.substring(line.length() - 1);
				String clause = line.replace(command, "").trim();

				List<Literal> literals = parseLiterals(clause);

				Clause last = new Clause(literals, clauses.size() + 1);

				System.out.println("User's command: " + line);

				switch (command) {
				case "?":
					ResolutionRefutation.instance.run(clauses, last);
					break;
				case "+":
					clauses.add(last);
					System.out.println("Added " + clause);
					break;
				case "-":
					clauses.remove(last);
					System.out.println("Removed " + clause);
					break;
				}

				System.out.println();

			}
			break;

		default:
			System.out.println("Incorrect argument, expected \"resolution\" or \"cooking\"");
			break;
		}

	}

	/**
	 * Parses the clauses from a file located at the provided path
	 * @param path The provided path
	 * @return Returns a list of the parsed clauses
	 */
	private static List<Clause> parseClauses(String path) {

		List<Clause> clauses = new ArrayList<>();

		try (Scanner sc = new Scanner(Paths.get(path))) {

			while (sc.hasNextLine()) {
				String line = sc.nextLine();

				if (line.startsWith("#")) {
					continue;
				}

				List<Literal> literals = parseLiterals(line);

				clauses.add(new Clause(literals, clauses.size() + 1));

			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		return clauses;
	}

	/**
	 * Extracts the commands from the file located at the provided path, used in the cooking algorithm
	 * @param path The provided path
	 * @return Returns a list with the extracted commands
	 */
	private static List<String> extractCommands(String path) {

		List<String> commands = new ArrayList<>();

		try (Scanner sc = new Scanner(Paths.get(path))) {

			while (sc.hasNextLine()) {
				String line = sc.nextLine();

				if (line.startsWith("#")) {
					continue;
				}

				line = line.toLowerCase().trim();
				commands.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		return commands;
	}

	/**
	 * Parses the literals from the provided {@link String}
	 * @param line The provided string
	 * @return Returns a list of literals
	 */
	private static List<Literal> parseLiterals(String line) {

		List<Literal> literals = new ArrayList<>();
		line = line.toLowerCase().trim();

		String[] stringLiterals = line.split(" v ");

		for (String literal : stringLiterals) {
			boolean negated = false;
			if (literal.startsWith("~")) {
				negated = true;
				literal = literal.replaceFirst("~", "");
			}

			literals.add(new Literal(literal, negated));

		}

		return literals;
	}

}
