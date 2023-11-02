package ui;

/**
 * Represents a literal in the proposition logic
 * 
 * @author Vito Sabalic
 *
 */
public class Literal {

	private String name;
	private boolean negated;

	/**
	 * A simple constructor that assigns the name of the literal and the negated flag
	 * @param name
	 * @param negated
	 */
	public Literal(String name, boolean negated) {
		this.name = name;
		this.negated = negated;
	}

	/**
	 * @return Returns true if the literal is negate, false otherwise
	 */
	public boolean isNegated() {
		return negated;
	}

	/**
	 * Negates the literal
	 */
	public void negate() {
		this.negated = !negated;
	}

	/**
	 * A getter for the literal name
	 * @return Returns the literal name
	 */
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (negated ? 1231 : 1237);
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
		Literal other = (Literal) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (negated != other.negated)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return (negated ? "~" : "") + name;
	}

}
