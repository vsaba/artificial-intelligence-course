package ui;

import java.util.List;

/**
 * Represents a Value in the ID3 algorithm. Values are actually values mapped to {@link Attribute} class
 * @author Vito Sabalic
 *
 */
public class Value {

	private String name;
	private double entropy;
	private List<Integer> distribution;

	/**
	 * A simple constructor
	 * @param name
	 */
	public Value(String name) {
		this.name = name;
		this.entropy = Double.MAX_VALUE;
		this.distribution = null;
	}

	/**
	 * A getter for the value name
	 * @return Returns the value name
	 */
	public String getName() {
		return name;
	}

	/**
	 * A getter for the value entropy
	 * @return Returns the value entropy
	 */
	public double getEntropy() {
		return entropy;
	}

	/**
	 * Sets the value entropy to the provided entropy
	 * @param entropy The provided entropy
	 */
	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	/**
	 * A getter for the values class value distribution
	 * @return Returns the values class value distribution
	 */
	public List<Integer> getDistribution() {
		return distribution;
	}

	/**
	 * Sets the values class value distribution to the provided distribution
	 * @param distribution The provided distribution
	 */
	public void setDistribution(List<Integer> distribution) {
		this.distribution = distribution;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Value other = (Value) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

}