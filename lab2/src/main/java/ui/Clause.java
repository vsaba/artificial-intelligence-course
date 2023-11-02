package ui;

import java.util.ArrayList;

import java.util.List;

/**
 * A class which represents a clause in the proposition logic
 * 
 * @author Vito Sabalic
 *
 */
public class Clause {

	/**
	 * Represents a NIL clause
	 */
	public static final Clause NIL = new Clause(null, 0);

	private List<Literal> literals;
	private int id;
	private int[] parentIds;

	/**
	 * A more detailed constructor, assigns the parentIds of the clause
	 * 
	 * @param literals  The literals of the clause
	 * @param id        The id of the clause
	 * @param parentIds The parent ids of the clause
	 */
	public Clause(List<Literal> literals, int id, int[] parentIds) {
		this(literals, id);

		if (parentIds.length != 2) {
			throw new IllegalArgumentException("Exactly 2 parent ids required");
		}
		this.parentIds = parentIds;
	}

	/**
	 * A simple constructor
	 * 
	 * @param literals The literals of the clause
	 * @param id       The id of the clause
	 */
	public Clause(List<Literal> literals, int id) {
		this.literals = literals;
		this.id = id;
		this.parentIds = new int[2];
	}

	/**
	 * Negates the entire clause
	 * 
	 * @return Returns a list of clauses
	 */
	public List<Clause> negate() {

		List<Clause> newClauses = new ArrayList<>();

		for (Literal literal : literals) {
			literal.negate();
			List<Literal> newSet = new ArrayList<>();
			newSet.add(literal);
			Clause newClause = new Clause(newSet, this.id++);
			newClauses.add(newClause);
		}

		return newClauses;

	}

	/**
	 * Inserts a literal into the clause
	 * 
	 * @param literal The literal to be inserted
	 */
	public void insertLiteral(Literal literal) {
		literals.add(literal);
	}

	/**
	 * Removes the literal from the clause
	 * 
	 * @param literal The literal from
	 */
	public void removeLiteral(Literal literal) {
		literals.remove(literal);
	}

	/**
	 * A getter for the literals of the clause
	 * 
	 * @return Returns the literals
	 */
	public List<Literal> getLiterals() {
		return literals;
	}

	public void setLiterals(List<Literal> literals) {
		this.literals = literals;
	}

	public int getId() {
		return id;
	}

	/**
	 * Sets the id of the clause
	 * @param id The id to be set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * A getter for the parent ids
	 * @return Returns the parent ids
	 */
	public int[] getParentIds() {
		return parentIds;
	}

	/**
	 * A setter for the parent ids
	 * @param id1 The id of the first parent
	 * @param id2 The id of the second parent
	 */
	public void setParentIds(int id1, int id2) {
		this.parentIds[0] = id1;
		this.parentIds[1] = id2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((literals == null) ? 0 : literals.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Clause other = (Clause) obj;
		if (literals == null) {
			if (other.literals != null)
				return false;
		} else if (!literals.equals(other.literals))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String s = new String();
		int index = 0;

		if (this == NIL) {
			return "NIL";
		}

		for (Literal literal : literals) {
			if (index == literals.size() - 1) {
				s += literal;
				break;
			}

			s += literal + " v ";
			index++;
		}

		return s;
	}

}
