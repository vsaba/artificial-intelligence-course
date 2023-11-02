package ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class which implements the resolution refutation algorithm.
 * This class utilizes the singleton design pattern.
 * @author Vito Sabalic
 *
 */
public class ResolutionRefutation {

	private Set<Clause> clauses;
	private String solution;

	/**
	 * The only isntance of this class
	 */
	public static ResolutionRefutation instance = new ResolutionRefutation();

	/**
	 * The constructor of this class
	 */
	private ResolutionRefutation() {
		clauses = new HashSet<>();
		solution = new String();
	}

	/**
	 * Launches the resolution refutation algorithm
	 * @param privateClauses The provided clauses
	 * @param lastClause The final clause
	 */
	public void run(List<Clause> privateClauses, Clause lastClause) {

		clauses.clear();
		solution = "";

		List<Clause> sosClauses = lastClause.negate();
		List<Clause> parentClauses = new ArrayList<>(privateClauses);
		parentClauses.addAll(sosClauses);
		List<Clause> newClauses = new ArrayList<>();

		ResolutionUtil.factorization(parentClauses);
		ResolutionUtil.deleteStrategy(parentClauses);

		for (int i = 0; i < parentClauses.size(); i++) {
			Clause toBePrintedClause = parentClauses.get(i);
			toBePrintedClause.setId(i + 1);
			solution += toBePrintedClause.getId() + ". " + toBePrintedClause + "\r\n";
		}

		solution += "===============\r\n";

		clauses.addAll(parentClauses);

		while (true) {

			Set<Clause> resolvents = new HashSet<>();

			l: for (int i = sosClauses.size() - 1; i >= 0; i--) {

				for (Clause clause : clauses) {
					resolvents = ResolutionUtil.resolveClauses(sosClauses.get(i), clause, clauses.size() + 1);

					if (resolvents.contains(Clause.NIL)) {
						newClauses.addAll(resolvents);

						for (Clause toBePrintedClause : resolvents) {
							int[] parentIds = toBePrintedClause.getParentIds();
							solution += toBePrintedClause.getId() + ". " + toBePrintedClause + " (" + parentIds[0]
									+ ", " + parentIds[1] + ")\r\n";
						}

						solution += "===============";
						System.out.println(solution);
						lastClause.negate();
						System.out.println("[CONCLUSION]: " + lastClause + " is true");
						return;
					}

					sosClauses.addAll(resolvents);
					newClauses.addAll(resolvents);

					if (!resolvents.isEmpty()) {
						for (Clause toBePrintedClause : resolvents) {
							int[] parentIds = toBePrintedClause.getParentIds();
							solution += toBePrintedClause.getId() + ". " + toBePrintedClause + " (" + parentIds[0]
									+ ", " + parentIds[1] + ")\r\n";
						}
						break l;
					}
				}
			}

			if (clauses.containsAll(newClauses)) {
				lastClause.negate();
				System.out.println("[CONCLUSION]: " + lastClause + " is unknown");
				return;
			}

			ResolutionUtil.factorization(sosClauses);
			ResolutionUtil.deleteStrategy(sosClauses);
			ResolutionUtil.factorization(newClauses);
			ResolutionUtil.deleteStrategy(newClauses);

			clauses.addAll(newClauses);

		}

	}

}
