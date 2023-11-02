package ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A util class used in the resolution refutation algorithm
 * @author Vito Sabalic
 *
 */
public class ResolutionUtil {

	/**
	 * Factorizes the provided clauses
	 * @param clauses The provided clauses
	 */
	public static void factorization(List<Clause> clauses) {
		for (Clause clause : clauses) {

			List<Literal> clauseLiterals = clause.getLiterals();
			Set<Literal> toRemoveDuplicates = new LinkedHashSet<>();

			toRemoveDuplicates.addAll(clauseLiterals);
			clauseLiterals.clear();
			clauseLiterals.addAll(toRemoveDuplicates);

		}

	}

	/**
	 * Applies the delete strategy to the provided clauses
	 * @param clauses The provided clauses
	 */
	public static void deleteStrategy(List<Clause> clauses) {

		List<Clause> helperList = new ArrayList<>(clauses);

		for (int i = 0; i < helperList.size(); i++) {

			Clause clause = helperList.get(i);

			if (isTautology(clause)) {
				clauses.remove(clause);
				continue;
			}

			for (int j = 0; j < helperList.size(); j++) {
				if (i == j) {
					continue;
				}

				Clause secondClause = helperList.get(j);
				if (secondClause.getLiterals().containsAll(clause.getLiterals())) {
					clauses.remove(secondClause);
				}
			}
		}
	}

	/**
	 * Resolves the two provided clauses
	 * @param sosClause The strategy of support clause
	 * @param parentClause The parent clause
	 * @param lastId The id of the resolved clause
	 * @return Returns a set of clauses
	 */
	public static Set<Clause> resolveClauses(Clause sosClause, Clause parentClause, int lastId) {
		Set<Clause> resolvedClauses = new HashSet<>();

		List<Literal> sosClauseLiterals = sosClause.getLiterals();
		List<Literal> parentClauseLiterals = parentClause.getLiterals();

		for (Literal sosClauseLiteral : sosClauseLiterals) {
			for (Literal parentClauseLiteral : parentClauseLiterals) {

				if (sosClauseLiteral.getName().equals(parentClauseLiteral.getName())) {

					if (!sosClauseLiteral.equals(parentClauseLiteral)) {

						if (sosClauseLiterals.size() == parentClauseLiterals.size()) {
							Clause.NIL.setId(lastId);
							Clause.NIL.setParentIds(parentClause.getId(), sosClause.getId());
							resolvedClauses.add(Clause.NIL);
						} else {

							Set<Literal> newLiterals = new HashSet<>();

							newLiterals.addAll(sosClauseLiterals);
							newLiterals.addAll(parentClauseLiterals);
							newLiterals.remove(sosClauseLiteral);
							newLiterals.remove(parentClauseLiteral);

							resolvedClauses.add(new Clause(new ArrayList<>(newLiterals), lastId++,
									new int[] { parentClause.getId(), sosClause.getId() }));
						}
					}

				}

			}
		}

		return resolvedClauses;
	}

	/**
	 * A function which tests whether the clause is a tautology
	 * @param clause The clause which is tested
	 * @return Returns true if the clause is indeed a tautology, false otherwise
	 */
	public static boolean isTautology(Clause clause) {

		List<Literal> literals = clause.getLiterals();
		int literalSize = literals.size();
		for (int i = 0; i < literalSize; i++) {

			Literal literal = literals.get(i);
			for (int j = i + 1; j < literalSize; j++) {
				Literal otherLiteral = literals.get(j);

				if (literal.getName().equals(otherLiteral.getName())) {
					if ((literal.isNegated() && !otherLiteral.isNegated())
							|| (!literal.isNegated() && otherLiteral.isNegated())) {
						return true;
					}
				}
			}
		}

		return false;
	}

}
