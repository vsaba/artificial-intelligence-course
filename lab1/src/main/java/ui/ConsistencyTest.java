package ui;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation which checks whether the provided heuristic is consistent
 * @author Vito Sabalic
 *
 */
public class ConsistencyTest {

	private List<Node> nodes;
	private String path;
	private Set<Node> visited;
	private boolean consistent;

	/**
	 * A simple constructor
	 * @param nodes
	 * @param path
	 */
	public ConsistencyTest(List<Node> nodes, String path) {
		this.nodes = nodes;
		this.path = path;
		this.visited = new HashSet<>();
		this.consistent = true;
	}

	/**
	 * Executes the algorithm
	 */
	public void start() {

		System.out.println("# HEURISTIC-CONSISTENT " + path);

		for (Node currentNode : nodes) {

			Map<Node, Double> adjacents = currentNode.getAdjacentNodes();

			for (Map.Entry<Node, Double> entry : adjacents.entrySet()) {

				if (visited.contains(entry.getKey())) {
					continue;
				}

				if (currentNode.getHeuristic() <= entry.getKey().getHeuristic() + entry.getValue()) {
					System.out.println("[CONDITION]: [OK] h(" + currentNode.getName() + ") <= h("
							+ entry.getKey().getName() + ") + c: " + currentNode.getHeuristic() + " <= "
							+ entry.getKey().getHeuristic() + " + " + entry.getValue());
				} else {
					consistent = false;
					System.out.println("[CONDITION]: [ERR] h(" + currentNode.getName() + ") <= h("
							+ entry.getKey().getName() + ") + c: " + currentNode.getHeuristic() + " <= "
							+ entry.getKey().getHeuristic() + " + " + entry.getValue());

				}
			}

		}

		System.out.println("[CONCLUSION]: " + (consistent ? "Heuristic is consistent." : "Heuristic is not consistent."));
	}

}
