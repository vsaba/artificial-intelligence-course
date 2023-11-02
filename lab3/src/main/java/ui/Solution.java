package ui;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The class which contains the main method of the project
 * @author Vito Sabalic
 *
 */
public class Solution {

	public static void main(String[] args) throws IOException {

		if (args.length < 2 || args.length > 3) {
			System.out.println(
					"Expected at least 2 arguments, path to train dataset and path path to test dataset, or 3 arguments, tree depth additionally");
			System.exit(0);
		}
		Path train_dataset = Paths.get(args[0]);
		Path test_dataset = Paths.get(args[1]);

		int depth = Integer.MAX_VALUE;

		if (args.length == 3) {
			depth = Integer.parseInt(args[2]);
		}

		ID3 algorithm = new ID3(depth);
		algorithm.fit(train_dataset);
		algorithm.predict(test_dataset);

	}
}
