package ui;

import java.util.List;

/**
 * Represents an attribute in the ID3 algorithm
 * 
 * @author Vito Sabalic
 *
 */
public class Attribute {

	private String name;
	private List<Value> values;
	private double entropy;
	private List<Integer> distribution;

	/**
	 * A simple constructor
	 * 
	 * @param name
	 */
	public Attribute(String name) {
		this.name = name;
		this.values = null;
		this.entropy = Double.MAX_VALUE;
		this.distribution = null;
	}

	/**
	 * A getter for the attribute name
	 * 
	 * @return Returns the attribute name
	 */
	public String getName() {
		return name;
	}

	/**
	 * A getter for the attribute values
	 * 
	 * @return Returns the attribute values
	 */
	public List<Value> getValues() {
		return values;
	}

	/**
	 * Sets the attribute values to the provided values
	 * 
	 * @param values The provided values
	 */
	public void setValues(List<Value> values) {
		this.values = values;
	}

	/**
	 * A getter for the attribute entropy
	 * 
	 * @return Returns the attribute entropy
	 */
	public double getEntropy() {
		return entropy;
	}

	/**
	 * Sets the attribute entropy to the provided entropy
	 * 
	 * @param values The provided entropy
	 */
	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	/**
	 * A getter for the attribute distribution of class values
	 * 
	 * @return Returns the attribute distribution of class values
	 */
	public List<Integer> getDistribution() {
		return distribution;
	}

	/**
	 * Sets the attribute distribution of class values to the provided distribution
	 * 
	 * @param values The provided distribution
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
		Attribute other = (Attribute) obj;
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
