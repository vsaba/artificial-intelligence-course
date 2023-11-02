package ui;

import java.util.HashMap;
import java.util.Map;

/**
 * A class which represents a single node in a graph
 * @author Vito Sabalic
 *
 */
public class Node {

	private String name;
	private Map<Node, Double> adjacentNodes;
	private double heuristic;
	private Node visitedFrom;
	private double value;

	/**
	 * A complex constructor
	 * @param name
	 * @param adjacentNodes
	 * @param heuristic
	 */
	public Node(String name, Map<Node, Double> adjacentNodes, double heuristic) {
		this.name = name;
		this.adjacentNodes = adjacentNodes;
		this.heuristic = heuristic;
		this.visitedFrom = null;
		this.value = Double.MAX_VALUE;
	}

	/**
	 * A simple constructor
	 * @param name
	 */
	public Node(String name) {
		this.name = name;
		this.heuristic = 0;
		this.adjacentNodes = new HashMap<>();
		this.value = Double.MAX_VALUE;
		this.visitedFrom = null;
	}

	/**
	 * A getter for the cost of the transition that was taken to get to the node
	 * @return Returns the cost
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Sets the cost of transition
	 * @param value The cost
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * A getter for the name
	 * @return Returns the cost
	 */
	public String getName() {
		return name;
	}

	/**
	 * A getter for the node from which this node was accessed from
	 * @return Returns the previous node
	 */
	public Node getVisitedFrom() {
		return visitedFrom;
	}

	/**
	 * Sets the previous node
	 * @param visitedFrom The previous node
	 */
	public void setVisitedFrom(Node visitedFrom) {
		this.visitedFrom = visitedFrom;
	}

	/**
	 * A getter for all adjacent nodes of this current node
	 * @return The map of all adjacent nodes
	 */
	public Map<Node, Double> getAdjacentNodes() {
		return adjacentNodes;
	}

	/**
	 * A getter for the heuristic of the current node
	 * @return The heuristic
	 */
	public double getHeuristic() {
		return heuristic;
	}

	/**
	 * Sets the adjacent nodes
	 * @param adjacentNodes the adjacent nodes
	 */
	public void setAdjacentNodes(Map<Node, Double> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}

	/**
	 * Sets the heuristic to the provided value
	 * @param heuristic The provided heuristic
	 */
	public void setHeuristic(double heuristic) {
		this.heuristic = heuristic;
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
		return "Node [name=" + name + "]";
	}

}
