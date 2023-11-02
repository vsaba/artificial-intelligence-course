package ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A utility class used by the {@link ID3} class
 * 
 * @author Vito Sabalic
 *
 */
public class ID3Util {

	/**
	 * Checks whether the provided map is empty, this method determines that a map
	 * is empty if no mapped values exists (null doesn't count)
	 * 
	 * @param parentAttributeValues The provided map
	 * @return Returns true if it is empty, false otherwise
	 */
	public static boolean checkIfMapIsEmpty(Map<Attribute, List<Value>> parentAttributeValues) {
		for (Map.Entry<Attribute, List<Value>> entry : parentAttributeValues.entrySet()) {
			if (!entry.getValue().isEmpty()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Calculates the entropy of the attribute or value based on the provided
	 * distribution
	 * 
	 * @param distribution The provided distribution
	 * @return Returns the calculated entropy
	 */
	public static double calculateEntropy(List<Integer> distribution) {
		double totalValue = 0;
		for (Integer i : distribution) {
			totalValue += i;
		}

		double entropy = 0;

		for (Integer i : distribution) {
			entropy += ((double) i / totalValue) * log2((double) i / totalValue);
		}

		return entropy * -1;
	}

	/**
	 * Calculates the log2 of the provided value
	 * 
	 * @param value The provided value
	 * @return Returns the log2
	 */
	public static double log2(double value) {

		return Math.log(value) / Math.log(2);
	}

	/**
	 * Cuts form the provided map the values which are not 'aligned' with the
	 * provided value which is mapped under the provided attribute
	 * 
	 * @param examples The provided examples
	 * @param attribute The provided attribute
	 * @param value The provided value
	 * @return Returns a new, cut map
	 */
	public static Map<Attribute, List<Value>> cutExamples(Map<Attribute, List<Value>> examples, Attribute attribute,
			Value value) {

		Map<Attribute, List<Value>> cutExamples = new HashMap<>();
		List<Integer> indexesToBeAdded = new ArrayList<>();
		List<Value> values = examples.get(attribute);

		for (int i = 0; i < values.size(); i++) {
			if (values.get(i).equals(value)) {
				indexesToBeAdded.add(i);
			}
		}

		for (Map.Entry<Attribute, List<Value>> entry : examples.entrySet()) {
			List<Value> newValues = new ArrayList<>();
			List<Value> temp = entry.getValue();
			for (int i : indexesToBeAdded) {
				newValues.add(temp.get(i));
			}

			cutExamples.put(entry.getKey(), newValues);
		}

		return cutExamples;
	}

}
