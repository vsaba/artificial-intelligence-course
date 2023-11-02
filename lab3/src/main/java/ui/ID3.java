package ui;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class which represents the ID3 machine learning algorithm
 * 
 * @author Vito Sabalic
 *
 */
public class ID3 {

	private Attribute classAttribute;
	private Node rootNode;
	private int depth;

	/**
	 * Constructor which accepts hyperparameter: depth
	 * 
	 * @param depth The hyperparameter
	 */
	public ID3(int depth) {
		this.depth = depth;
		this.classAttribute = null;
		this.rootNode = null;
	}

	/**
	 * The fit function of the machine learning algorithm. Trains the algorithm
	 * based on the provided dataset
	 * 
	 * @param train_dataset The provided dataset
	 */
	public void fit(Path train_dataset) {

		Map<Attribute, List<Value>> attributeValues = parseDataset(train_dataset);

		List<Attribute> attributes = new ArrayList<>(attributeValues.keySet());
		attributes.remove(classAttribute);
		List<Value> classValues = attributeValues.get(classAttribute);

		this.rootNode = id3Algorithm(attributeValues, attributeValues, attributes, classValues, depth);

		System.out.println("[BRANCHES]:");
		printTree(rootNode, 1, "");

		return;
	}

	/**
	 * The predict method of the machine learning algorithm, computes a result based
	 * on the provided dataset and the previous training
	 * 
	 * @param test_dataset The provided dataset
	 */
	public void predict(Path test_dataset) {

		Map<Attribute, List<Value>> attributeValues = parseDataset(test_dataset);

		String solutionToBePrinted = "[PREDICTIONS]: ";

		List<Value> predictedValues = new ArrayList<>();
		List<Value> trueValues = attributeValues.get(classAttribute);

		for (int i = 0; i < trueValues.size(); i++) {
			Map<String, Value> mapRow = new HashMap<>();

			for (Map.Entry<Attribute, List<Value>> entry : attributeValues.entrySet()) {
				mapRow.put(entry.getKey().getName(), entry.getValue().get(i));
			}

			Value v = iterateTree(mapRow);
			predictedValues.add(v);
			solutionToBePrinted += v + " ";

		}

		System.out.println(solutionToBePrinted.substring(0, solutionToBePrinted.length() - 1));

		double correct = 0;
		double total = trueValues.size();

		if (predictedValues.size() != trueValues.size()) {
			System.out.println("The sizes of the value lists should match! Ending program");
			return;
		}

		for (int i = 0; i < trueValues.size(); i++) {
			if (trueValues.get(i).equals(predictedValues.get(i))) {
				correct++;
			}
		}

		System.out.println("[ACCURACY]: " + String.format("%.5f", correct / total).replace(",", "."));

		Comparator<Value> comparator = new Comparator<Value>() {
			@Override
			public int compare(Value o1, Value o2) {
				return o1.getName().compareTo(o2.getName());
			}
		};

		Set<Value> trueValuesSet = new TreeSet<>(comparator);
		trueValuesSet.addAll(trueValues);

		Set<Value> predictedValuesSet = new TreeSet<>(comparator);
		predictedValuesSet.addAll(predictedValues);

		List<Value> matrixPredictedValues = new ArrayList<>(predictedValuesSet);
		List<Value> matrixTrueValues = new ArrayList<>(trueValuesSet);

		int setSize = trueValuesSet.size();
		int[][] confusionMatrix = new int[setSize][setSize];
		for (int i = 0; i < trueValues.size(); i++) {

			int predictedIndex = matrixPredictedValues.indexOf(predictedValues.get(i));
			int trueIndex = matrixTrueValues.indexOf(trueValues.get(i));

			confusionMatrix[trueIndex][predictedIndex] += 1;
		}

		System.out.println("[CONFUSION_MATRIX]:");

		for (int i = 0; i < setSize; i++) {
			for (int j = 0; j < setSize; j++) {
				System.out.print(confusionMatrix[i][j] + " ");
			}

			if (i != setSize - 1) {
				System.out.println();
			}
		}

		return;
	}

	/**
	 * Iterates through the tree which is constructed in the fit method of the model
	 * 
	 * @param mapRow A row which represents a situation
	 * @return Returns the final class value of the tree
	 */
	private Value iterateTree(Map<String, Value> mapRow) {

		Node currentNode = rootNode;

		while (!currentNode.isLeaf()) {
			String nodeName = currentNode.getName();
			Value valueForNodeName = mapRow.get(nodeName);

			Node temp = currentNode.getSubtrees().get(valueForNodeName);

			if (temp == null) {
				return currentNode.getMostOccuringValue();
			}

			currentNode = temp;
		}

		return new Value(currentNode.getName());
	}

	/**
	 * Prints the tree constructed in the fit method of the model
	 * 
	 * @param n     The starting node
	 * @param depth The depth of the tree
	 * @param s     The string to be outputed
	 */
	private void printTree(Node n, int depth, String s) {

		if (n.isLeaf()) {
			System.out.println(s + n.getName());
			return;
		}

		for (Map.Entry<Value, Node> entry : n.getSubtrees().entrySet()) {

			String toBePrinted = s + depth + ":" + n.getName() + "=" + entry.getKey().getName() + " ";
			printTree(entry.getValue(), depth + 1, toBePrinted);
		}

	}

	/**
	 * Executes the ID3 algorithm
	 * 
	 * @param attributeValues       The provided attribute : value pairs
	 * @param parentAttributeValues The provided parent attribute : value pairs
	 * @param attributes            The provided attributes
	 * @param classValues           The provided class variables
	 * @param depth                 The depth of the tree construction
	 * @return Returns the root node of the constructed tree
	 */
	private Node id3Algorithm(Map<Attribute, List<Value>> attributeValues,
			Map<Attribute, List<Value>> parentAttributeValues, List<Attribute> attributes, List<Value> classValues,
			int depth) {

		Value v = null;

		if (ID3Util.checkIfMapIsEmpty(attributeValues)) {
			v = argmax(parentAttributeValues);
			return new Node(v.getName(), null, true, null);
		}

		v = argmax(attributeValues);

		int attributeValuesLength = attributeValues.get(classAttribute).size();
		int vValuesLength = Collections.frequency(attributeValues.get(classAttribute), v);


		if (attributes.isEmpty() || attributeValuesLength == vValuesLength || depth <= 0) {
			return new Node(v.getName(), null, true, null);
		}

		double maxIG = -1;
		Attribute x = null;
		int attributeIndex = -1;

		for (int i = 0; i < attributes.size(); i++) {

			Attribute a = attributes.get(i);

			double ig = calculateIG(attributeValues, a);

			System.out.print("IG(" + a.getName() + ")=" + String.format("%.5f", ig).replace(",", ".") + " ");

			if (ig == maxIG) {
				if (a.getName().compareTo(x.getName()) < 0) {
					x = a;
					attributeIndex = i;
				}
				continue;
			}

			if (ig > maxIG) {
				maxIG = ig;
				x = a;
				attributeIndex = i;
			}

		}
		System.out.println();

		Map<Value, Node> subtrees = new HashMap<>();
		List<Attribute> temp = new ArrayList<>(attributes);
		temp.remove(attributeIndex);

		for (Value value : x.getValues()) {
			Node n = id3Algorithm(ID3Util.cutExamples(attributeValues, x, value), attributeValues, temp,
					classValues, depth - 1);

			subtrees.put(value, n);

		}

		return new Node(x.getName(), subtrees, false, v);
	}

	/**
	 * Parses the dataset located at the provided path
	 * 
	 * @param pathToDataset The provided path
	 * @return Returns the map whose keys are the parsed attributes and whose values
	 *         are the parsed values of the specific attribute
	 */
	private Map<Attribute, List<Value>> parseDataset(Path pathToDataset) {
		List<String> fileLines = null;
		try {
			fileLines = Files.readAllLines(pathToDataset);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<Attribute, List<Value>> mappedLines = new HashMap<>();

		String firstLine = fileLines.get(0);

		List<String> temp = Arrays.asList(firstLine.split(","));

		List<Attribute> keys = new ArrayList<>();

		temp.forEach(s -> keys.add(new Attribute(s)));

		this.classAttribute = keys.get(keys.size() - 1);

		for (int i = 1; i < fileLines.size(); i++) {

			String[] pom = fileLines.get(i).split(",");

			for (int j = 0; j < keys.size(); j++) {

				List<Value> values = mappedLines.get(keys.get(j));

				if (values == null) {
					values = new ArrayList<>();
				}
				values.add(new Value(pom[j]));
				mappedLines.put(keys.get(j), values);
			}
		}

		for (Map.Entry<Attribute, List<Value>> entry : mappedLines.entrySet()) {
			entry.getKey().setValues(new ArrayList<>(new HashSet<>(entry.getValue())));
		}

		return mappedLines;
	}

	/**
	 * Finds the class value which has the most occurrences in the provided map
	 * @param examples The provided map
	 * @return Returns the most occurring value
	 */
	private Value argmax(Map<Attribute, List<Value>> examples) {

		List<Value> finalValues = examples.get(classAttribute);
		Set<Value> pom = new HashSet<>(finalValues);
		Map<Value, Integer> pom1 = new HashMap<>();

		pom.forEach(v -> pom1.put(v, 0));

		for (Value value : finalValues) {
			pom1.put(value, pom1.get(value) + 1);
		}

		Value toBeReturned = null;
		int max = -1;

		for (Map.Entry<Value, Integer> entry : pom1.entrySet()) {

			if (entry.getValue() == max && toBeReturned != null) {
				if (entry.getKey().getName().compareTo(toBeReturned.getName()) < 0) {
					toBeReturned = entry.getKey();
				}
				continue;
			}

			if (entry.getValue() > max) {
				max = entry.getValue();
				toBeReturned = entry.getKey();

			}
		}

		return toBeReturned;
	}

	/**
	 * Calculates the information gain of the provided attribute and the provided examples
	 * @param examples The provided examples
	 * @param attribute The provided attribute
	 * @return Returns the calculated information gain
	 */
	private double calculateIG(Map<Attribute, List<Value>> examples, Attribute attribute) {

		List<Integer> distribution = calculateDistribution(examples);
		attribute.setEntropy(ID3Util.calculateEntropy(distribution));
		attribute.setDistribution(distribution);

		double ig = attribute.getEntropy();
		int attributeSum = 0;

		for (int i : distribution) {
			attributeSum += i;
		}

		for (Value value : attribute.getValues()) {
			Map<Attribute, List<Value>> cut = ID3Util.cutExamples(examples, attribute, value);
			List<Integer> valueDistribution = calculateDistribution(cut);
			value.setEntropy(ID3Util.calculateEntropy(valueDistribution));
			value.setDistribution(valueDistribution);

			double sum = 0;
			for (int i : valueDistribution) {
				sum += i;
			}

			ig -= (sum / (double) attributeSum) * value.getEntropy();
		}

		return ig;
	}

	/**
	 * Calculates the distribution of the class variables in the provided examples
	 * @param examples The provided examples
	 * @return Returns a list of the occurrence distribution
	 */
	private List<Integer> calculateDistribution(Map<Attribute, List<Value>> examples) {
		List<Value> classValues = examples.get(classAttribute);
		Map<Value, Integer> numberOfClassValues = new HashMap<>();
		classValues.forEach(v -> numberOfClassValues.put(v, 0));

		for (Value value : classValues) {
			numberOfClassValues.put(value, numberOfClassValues.get(value) + 1);

		}

		List<Integer> distribution = new ArrayList<>();

		for (Map.Entry<Value, Integer> entry : numberOfClassValues.entrySet()) {
			distribution.add(entry.getValue());
		}

		return distribution;
	}

}
