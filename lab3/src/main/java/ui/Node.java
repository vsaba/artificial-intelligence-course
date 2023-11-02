package ui;

import java.util.Map;

/**
 * Represents a node in the tree which is constructed in the ID3 algorithm
 * @author Vito Sabalic
 *
 */
public class Node {

	private boolean isLeaf;
	private String name;
	private Map<Value, Node> subtrees;
	private Value mostOccuringValue;

	/**
	 * A simple constructor
	 * @param name
	 * @param subtrees
	 * @param isLeaf
	 * @param mostOccuringValue
	 */
	public Node(String name, Map<Value, Node> subtrees, boolean isLeaf, Value mostOccuringValue) {
		this.name = name;
		this.subtrees = subtrees;
		this.isLeaf = isLeaf;
		this.setMostOccuringValue(mostOccuringValue);
	}

	/**
	 * A getter for the isLeaf flag
	 * @return Returns true if the node is a leaf, false otherwise
	 */
	public boolean isLeaf() {
		return isLeaf;
	}

	/**
	 * Sets the isLeaf flag to the provided value
	 * @param isLeaf The provided value
	 */
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	/**
	 * A getter for the Node name
	 * @return Returns the node name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the node name to the provided value
	 * @param name The provided value
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * A getter for the Node subtrees
	 * @return Returns the node subtrees
	 */
	public Map<Value, Node> getSubtrees() {
		return subtrees;
	}

	/**
	 * Sets the node subtrees to the provided subtrees
	 * @param name The provided subtrees
	 */
	public void setSubtrees(Map<Value, Node> subtrees) {
		this.subtrees = subtrees;
	}

	/**
	 * A getter for the most occurring class value in the Node
	 * @return Returns the most occurring class value in the Node
	 */
	public Value getMostOccuringValue() {
		return mostOccuringValue;
	}

	/**
	 * Sets the most occurring class value to the provided value
	 * @param name The provided class value
	 */
	public void setMostOccuringValue(Value mostOccuringValue) {
		this.mostOccuringValue = mostOccuringValue;
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
		Node other = (Node) obj;
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
