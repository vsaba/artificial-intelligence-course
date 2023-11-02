package ui;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * An implementation of a class which can execute an A* search algorithm
 * @author Vito Sabalic
 *
 */
public class AStar {

	private List<Node> solutions;
	private Node starterNode;
	private String heuristicsPath;

	/**
	 * A simple constructor
	 * @param solutions
	 * @param starterNode
	 * @param heruisticsPath
	 */
	public AStar(List<Node> solutions, Node starterNode, String heruisticsPath) {

		this.solutions = solutions;
		this.starterNode = starterNode;
		this.heuristicsPath = heruisticsPath;
	}

	/**
	 * Executes the algorithm
	 */
	public void start() {

		starterNode.setValue(0);

		Set<Node> visited = new HashSet<>();
		Queue<Node> unvisited = new PriorityQueue<>(new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {

				int i = Double.compare(o1.getValue() + o1.getHeuristic(), o2.getValue() + o2.getHeuristic());
				if (i == 0) {
					i = o1.getName().compareTo(o2.getName());
				}

				return i;
			}
		});

		unvisited.add(starterNode);

		while (!unvisited.isEmpty()) {

			Node currentNode = unvisited.poll();

			if (solutions.contains(currentNode)) {
				visited.add(solutions.get(solutions.indexOf(currentNode)));
				printSolution(true, currentNode, visited);
				return;
			}

			for (Map.Entry<Node, Double> entry : currentNode.getAdjacentNodes().entrySet()) {
				Node adjacent = entry.getKey();

				if (!visited.contains(adjacent)) {
					calculateMinimumDistance(adjacent, entry.getValue(), currentNode);
					unvisited.add(adjacent);
				}
			}

			visited.add(currentNode);
		}

	}

	/**
	 * Prints the solution found by the algorithm to the standard output
	 * @param b true if a solution is found, false otherwise
	 * @param currentNode The finishing node
	 * @param visited All visited nodes
	 */
	private void printSolution(boolean b, Node currentNode, Set<Node> visited) {
		System.out.println("# A-STAR " + heuristicsPath);
		if (!b) {
			System.out.println("[FOUND_SOLUTION]: no");
			return;
		}

		System.out.println("[FOUND_SOLUTION]: yes");
		System.out.println("[STATES_VISITED]: " + visited.size());

		List<Node> path = calculatePath(currentNode);
		System.out.println("[PATH_LENGTH]: " + path.size());

		System.out.println("[TOTAL_COST]: " + (double) currentNode.getValue());
		Collections.reverse(path);
		String s = new String();
		for (Node node : path) {

			if (path.indexOf(node) == path.size() - 1) {
				s += node.getName();
				break;
			}

			s += node.getName() + " => ";
		}

		System.out.println("[PATH]: " + s);

	}

	
	/**
	 * Calculates the path which was taken to arrive to the final node
	 * @param finalNode The final node
	 * @return Returns a list which represents a path of nodes
	 */
	private List<Node> calculatePath(Node finalNode) {
		List<Node> path = new ArrayList<>();

		path.add(finalNode);

		Node previousNode = finalNode.getVisitedFrom();

		while (previousNode != null) {
			path.add(previousNode);
			previousNode = previousNode.getVisitedFrom();
		}

		return path;
	}

	/**
	 * Calculates whether the cost of the transition from the current node to the adjacent node 
	 * is cheaper than the stored cost of the transition in the adjacent node
	 * @param adjacent The adjacent node
	 * @param value The cost of the transition
	 * @param currentNode The current node
	 */
	private void calculateMinimumDistance(Node adjacent, double value, Node currentNode) {

		if (currentNode.getValue() + value < adjacent.getValue()) {
			adjacent.setValue(currentNode.getValue() + value);
			adjacent.setVisitedFrom(currentNode);
		}

	}

}
