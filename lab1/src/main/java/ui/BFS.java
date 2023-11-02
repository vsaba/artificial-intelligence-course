package ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * An implementation of a class which can execute a BFS search algorithm
 * @author Vito Sabalic
 *
 */
public class BFS {

	private List<Node> solutions;
	private Node starterNode;

	/**
	 * A simple constructor
	 * @param solutions
	 * @param starterNode
	 */
	public BFS(List<Node> solutions, Node starterNode) {

		this.solutions = solutions;
		this.starterNode = starterNode;

	}

	/**
	 * Executes the algorithm
	 */
	public void start() {

		Queue<Node> queue = new LinkedBlockingQueue<>();
		Set<Node> visited = new HashSet<>();
		Node currentNode;

		queue.add(starterNode);

		while (!queue.isEmpty()) {

			currentNode = queue.poll();

			if (visited.contains(currentNode)) {
				continue;
			}

			if (solutions.contains(currentNode)) {
				visited.add(solutions.get(solutions.indexOf(currentNode)));
				printSolution(true, currentNode, visited);
				return;
			}

			visited.add(currentNode);
			List<Node> neighbours = new ArrayList<>(currentNode.getAdjacentNodes().keySet());

			neighbours.removeAll(visited);

			Collections.sort(neighbours, new Comparator<Node>() {
				@Override
				public int compare(Node o1, Node o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});

			for (Node n : neighbours) {
				n.setVisitedFrom(currentNode);
			}
			queue.addAll(neighbours);

		}

		printSolution(false, null, visited);
	}

	/**
	 * Prints the solution found by the algorithm to the standard output
	 * @param b true if a solution is found, false otherwise
	 * @param currentNode The finishing node
	 * @param visited All visited nodes
	 */
	private void printSolution(boolean b, Node finalNode, Set<Node> visited) {
		System.out.println("# BFS");
		if (!b) {
			System.out.println("[FOUND_SOLUTION]: no");
			return;
		}

		System.out.println("[FOUND_SOLUTION]: yes");
		System.out.println("[STATES_VISITED]: " + visited.size());

		List<Node> path = calculatePath(finalNode);
		System.out.println("[PATH_LENGTH]: " + path.size());

		System.out.println("[TOTAL_COST]: " + calculateCost(path));
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
	 * Calculates the total cost of the transitions 
	 * @param path The transition path
	 * @return Returns the total cost
	 */
	private double calculateCost(List<Node> path) {

		double cost = 0;

		for (Node n : path) {
			Node previousNode = n.getVisitedFrom();

			if (previousNode == null) {
				break;
			}

			Map<Node, Double> adjacent = previousNode.getAdjacentNodes();

			cost += adjacent.get(n);
		}

		return cost;
	}

}
