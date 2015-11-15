// Nov 11, 2015 11:04:01 AM
package harlequinmettle.investmentadviserengine.neuralnet.util.prelim.som;

public class SOM2 {
	private static final int MAX_CLUSTERS = 5;
	private static final int VEC_LEN = 7;
	private static final int INPUT_PATTERNS = 7;
	private static final int INPUT_TESTS = 6;
	private static final double DECAY_RATE = 0.96; // About 100 iterations.
	private static final double MIN_ALPHA = 0.01;
	private static final double RADIUS_REDUCTION_POINT = 0.023; // Last 20% of
																// iterations.

	private static double alpha = 0.6;
	private static double d[] = new double[MAX_CLUSTERS];

	// Weight matrix with randomly chosen values between 0.0 and 1.0
	private static double w[][] = { { 0.2, 0.6, 0.5, 0.9, 0.4, 0.2, 0.8 }, { 0.9, 0.3, 0.6, 0.4, 0.5, 0.6, 0.3 },
			{ 0.8, 0.5, 0.7, 0.2, 0.6, 0.9, 0.5 }, { 0.6, 0.4, 0.2, 0.3, 0.7, 0.2, 0.4 }, { 0.8, 0.9, 0.7, 0.9, 0.3, 0.2, 0.5 } };

	private static int pattern[][] = { { 1, 1, 1, 0, 0, 0, 0 }, { 0, 0, 0, 0, 1, 1, 1 }, { 0, 0, 1, 1, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 }, { 1, 0, 1, 0, 1, 0, 1 } };

	private static int tests[][] = { { 1, 1, 1, 1, 0, 0, 0 }, { 0, 1, 1, 0, 1, 1, 1 }, { 0, 1, 0, 1, 0, 1, 0 }, { 0, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 0, 0 }, { 0, 0, 0, 1, 1, 1, 1 } };

	private static void training() {
		int iterations = 0;
		boolean reductionFlag = false;
		int reductionPoint = 0;
		int dMin = 0;

		while (alpha > MIN_ALPHA) {
			iterations += 1;

			for (int vecNum = 0; vecNum <= (INPUT_PATTERNS - 1); vecNum++) {
				// Compute input for all nodes.
				computeInput(pattern, vecNum);

				// See which is smaller?
				dMin = minimum(d);

				// Update the weights on the winning unit.
				updateWeights(vecNum, dMin);

			} // VecNum

			// Reduce the learning rate.
			alpha = DECAY_RATE * alpha;

			// Reduce radius at specified point.
			if (alpha < RADIUS_REDUCTION_POINT) {
				if (reductionFlag == false) {
					reductionFlag = true;
					reductionPoint = iterations;
				}
			}
		}

		System.out.println("Iterations: " + iterations);

		System.out.println("Neighborhood radius reduced after " + reductionPoint + " iterations.");

		return;
	}

	private static void computeInput(int[][] vectorArray, int vectorNumber) {
		clearArray(d);

		for (int i = 0; i <= (MAX_CLUSTERS - 1); i++) {
			for (int j = 0; j <= (VEC_LEN - 1); j++) {
				d[i] += Math.pow((w[i][j] - vectorArray[vectorNumber][j]), 2);
			} // j
		} // i
		return;
	}

	private static void updateWeights(int vectorNumber, int dMin) {
		for (int i = 0; i <= (VEC_LEN - 1); i++) {
			// Update the winner.
			w[dMin][i] = w[dMin][i] + (alpha * (pattern[vectorNumber][i] - w[dMin][i]));

			// Only include neighbors before radius reduction point is reached.
			if (alpha > RADIUS_REDUCTION_POINT) {
				if ((dMin > 0) && (dMin < (MAX_CLUSTERS - 1))) {
					// Update neighbor to the left...
					w[dMin - 1][i] = w[dMin - 1][i] + (alpha * (pattern[vectorNumber][i] - w[dMin - 1][i]));
					// and update neighbor to the right.
					w[dMin + 1][i] = w[dMin + 1][i] + (alpha * (pattern[vectorNumber][i] - w[dMin + 1][i]));
				} else {
					if (dMin == 0) {
						// Update neighbor to the right.
						w[dMin + 1][i] = w[dMin + 1][i] + (alpha * (pattern[vectorNumber][i] - w[dMin + 1][i]));
					} else {
						// Update neighbor to the left.
						w[dMin - 1][i] = w[dMin - 1][i] + (alpha * (pattern[vectorNumber][i] - w[dMin - 1][i]));
					}
				}
			}
		} // i
		return;
	}

	private static void clearArray(double[] nodeArray) {
		for (int i = 0; i <= (MAX_CLUSTERS - 1); i++) {
			nodeArray[i] = 0.0;
		} // i
		return;
	}

	private static int minimum(double[] nodeArray) {
		int winner = 0;
		boolean foundNewWinner = false;
		boolean done = false;

		while (!done) {
			foundNewWinner = false;
			for (int i = 0; i <= (MAX_CLUSTERS - 1); i++) {
				if (i != winner) { // Avoid self-comparison.
					if (nodeArray[i] < nodeArray[winner]) {
						winner = i;
						foundNewWinner = true;
					}
				}
			} // i

			if (foundNewWinner == false) {
				done = true;
			}
		}
		return winner;
	}

	private static void printResults() {
		int dMin = 0;

		// Print clusters created.
		System.out.println("Clusters for training input:");
		for (int vecNum = 0; vecNum <= (INPUT_PATTERNS - 1); vecNum++) {
			// Compute input.
			computeInput(pattern, vecNum);

			// See which is smaller.
			dMin = minimum(d);

			System.out.print("Vector (");
			for (int i = 0; i <= (VEC_LEN - 1); i++) {
				System.out.print(pattern[vecNum][i] + ", ");
			} // i
			System.out.print(") fits into category " + dMin + "\n");

		} // VecNum

		// Print weight matrix.
		System.out.println("------------------------------------------------------------------------");
		for (int i = 0; i <= (MAX_CLUSTERS - 1); i++) {
			System.out.println("Weights for Node " + i + " connections:");
			System.out.print("     ");
			for (int j = 0; j <= (VEC_LEN - 1); j++) {
				String temp = String.format("%.3f", w[i][j]);
				System.out.print(temp + ", ");
			} // j
			System.out.print("\n");
		} // i

		// Print post-training tests.
		System.out.println("------------------------------------------------------------------------");
		System.out.println("Categorized test input:");
		for (int vecNum = 0; vecNum <= (INPUT_TESTS - 1); vecNum++) {
			// Compute input for all nodes.
			computeInput(tests, vecNum);

			// See which is smaller.
			dMin = minimum(d);

			System.out.print("Vector (");
			for (int i = 0; i <= (VEC_LEN - 1); i++) {
				System.out.print(tests[vecNum][i] + ", ");
			} // i
			System.out.print(") fits into category " + dMin + "\n");

		} // VecNum
		return;
	}

	public static void main(String[] args) {
		training();
		printResults();
	}
}
