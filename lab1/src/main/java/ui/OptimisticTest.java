package ui;

import java.util.Comparator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * An implementation which checks whether the provided heuristic is optimistic
 * @author Vito Sabalic
 *
 */
public class OptimisticTest {

	private List<Node> nodes;
	private List<Node> endNodes;
	private boolean optimistic;
	private String path;

	/**
	 * A simple constructor
	 * @param nodes
	 * @param endNodes
	 * @param path
	 */
	public OptimisticTest(List<Node> nodes, List<Node> endNodes, String path) {
		this.nodes = nodes;
		this.endNodes = endNodes;
		this.optimistic = true;
		this.path = path;
	}

	/**
	 * Executes the algorithm
	 */
	public void start() {

		System.out.println("# HEURISTIC-OPTIMISTIC " + path);

		l: for (Node starterNode : nodes) {
			starterNode.setValue(0);

			Set<Node> visited = new HashSet<>();
			Queue<Node> unvisited = new PriorityQueue<>(new Comparator<Node>() {
				@Override
				public int compare(Node o1, Node o2) {

					int i = Double.compare(o1.getValue(), o2.getValue());
					if (i == 0) {
						i = o1.getName().compareTo(o2.getName());
					}

					return i;
				}
			});

			unvisited.add(starterNode);

			while (!unvisited.isEmpty()) {

				Node currentNode = unvisited.poll();

				if (endNodes.contains(currentNode)) {
					visited.add(currentNode);

					if (starterNode.getHeuristic() <= currentNode.getValue()) {
						System.out.println("[CONDITION]: [OK] h(" + starterNode.getName() + ") <= h*: "
								+ starterNode.getHeuristic() + " <= " + currentNode.getValue());
					} else {
						optimistic = false;
						System.out.println("[CONDITION]: [ERR] h(" + starterNode.getName() + ") <= h*: "
								+ starterNode.getHeuristic() + " <= " + currentNode.getValue());
					}

					nodes.forEach(n -> {
						n.setValue(Integer.MAX_VALUE);
						n.setVisitedFrom(null);
					});

					continue l;

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

		System.out.println("[CONCLUSION]: " + (optimistic ? "Heuristic is optimistic." : "Heuristic is not optimistic."));

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
