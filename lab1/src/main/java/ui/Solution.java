package ui;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main class which initalizes all necessary variables and lists, then calls and executes the provided algorithm
 * @author Vito Sabalic
 *
 */
public class Solution {
	private static Map<String, Node> nodes;

	public static void main(String[] args) {

		String algorithm = new String();
		String path = new String();
		String pathToHeuristic = new String();

		for (int i = 0; i < args.length; i++) {

			switch (args[i]) {
			case "--alg":
				algorithm = args[++i];
				break;
			case "--ss":
				path = args[++i];
				break;
			case "--h":
				pathToHeuristic = args[++i];
				break;
			case "--check-optimistic":
				algorithm = "optimistic";
				break;
			case "--check-consistent":
				algorithm = "consistent";
				break;
			default:
				System.out.println("Incorrect arguments");
				break;
			}

		}

		List<Node> nodes = extractNodes(path, pathToHeuristic);
		Collections.sort(nodes, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		Node startingNode = extractStarter(path);
		List<Node> destinationNodes = extractDestinationNodes(path);

		switch (algorithm) {
		case "bfs":
			new BFS(destinationNodes, startingNode).start();
			break;
		case "ucs":
			new UCS(destinationNodes, startingNode).start();
			break;
		case "astar":
			new AStar(destinationNodes, startingNode, pathToHeuristic).start();
			break;
		case "optimistic":
			new OptimisticTest(nodes, destinationNodes, pathToHeuristic).start();
			break;
		case "consistent":
			new ConsistencyTest(nodes, pathToHeuristic).start();
			break;
		default:
			break;
		}

	}

	/**
	 * Extracts the destination nodes from the file on the provided path
	 * @param path The provided path
	 * @return A {@link List} of destination nodes
	 */
	private static List<Node> extractDestinationNodes(String path) {

		List<Node> destinationNodes = new ArrayList<>();
		int i = 0;
		try (Scanner sc = new Scanner(Paths.get(path))) {

			while (sc.hasNextLine()) {

				String s = sc.nextLine();

				if (s.startsWith("#")) {
					continue;
				}

				if (i == 0) {
					i++;
					continue;
				}

				String[] pom = s.split(" ");

				for (String str : pom) {
					destinationNodes.add(nodes.get(str));
				}

				break;

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return destinationNodes;
	}

	/**
	 * Extracts the starter node from the file on the provided path
	 * @param path The provided path of the file
	 * @return The starter node
	 */
	private static Node extractStarter(String path) {
		Node starter = null;

		try (Scanner sc = new Scanner(Paths.get(path))) {

			while (sc.hasNextLine()) {
				String s = sc.nextLine();

				if (s.startsWith("#")) {
					continue;
				}

				starter = nodes.get(s);
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return starter;
	}

	/**
	 * Extracts all nodes from the provided path, initializes all adjacent nodes and heuristics
	 * @param path The path of the file which contains adjacent nodes
	 * @param heuristicPath The path of the file which contains heuristics for each node
	 * @return A list of all nodes
	 */
	private static List<Node> extractNodes(String path, String heuristicPath) {

		nodes = new HashMap<>();
		int i = 0;
		try (Scanner sc = new Scanner(Paths.get(path))) {
			while (sc.hasNextLine()) {
				String s = sc.nextLine();

				if (s.startsWith("#")) {
					continue;
				}

				if (i == 0 || i == 1) {
					i++;
					continue;
				}

				String[] pom = s.split(":");

				Node n = null;

				if (!nodes.containsKey(pom[0])) {
					n = new Node(pom[0]);
					nodes.put(pom[0], n);
				} else {
					n = nodes.get(pom[0]);
				}

				Map<Node, Double> pomMap = new HashMap<>();
				if (pom.length > 1) {
					String[] pom2 = pom[1].trim().split(" ");
					for (String str : pom2) {
						String[] pom3 = str.split(",");

						Node adjacentNode = null;
						if (nodes.containsKey(pom3[0])) {
							adjacentNode = nodes.get(pom3[0]);
						} else {
							adjacentNode = new Node(pom3[0]);
							nodes.put(pom3[0], adjacentNode);
						}
						pomMap.put(adjacentNode, Double.parseDouble(pom3[1]));
					}
				}

				n.setAdjacentNodes(pomMap);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!heuristicPath.isEmpty()) {
			try (Scanner sc = new Scanner(Paths.get(heuristicPath))) {
				while (sc.hasNextLine()) {
					String s = sc.nextLine();

					String[] pom = s.split(":");

					Node n = nodes.get(pom[0]);
					n.setHeuristic(Double.parseDouble(pom[1]));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return new LinkedList<>(nodes.values());
	}

}
