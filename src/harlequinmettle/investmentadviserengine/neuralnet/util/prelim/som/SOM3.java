// Nov 11, 2015 11:29:07 AM
package harlequinmettle.investmentadviserengine.neuralnet.util.prelim.som;

import java.util.ArrayList;
import java.util.Random;

public class SOM3 {

	// If you look closely, you can see the letters made out of 1's
	private static int A1[] = { 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1,
			1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1 };

	private static int B1[] = { 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0,
			0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0 };

	private static int C1[] = { 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
			0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0 };

	private static int D1[] = { 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0,
			0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0 };

	private static int E1[] = { 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1,
			0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1 };

	private static int J1[] = { 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
			0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0 };

	private static int K1[] = { 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1,
			0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1 };

	private static int A2[] = { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0,
			0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0 };

	private static int B2[] = { 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0,
			0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0 };

	private static int C2[] = { 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
			0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0 };

	private static int D2[] = { 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0,
			0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0 };

	private static int E2[] = { 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0,
			0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1 };

	private static int J2[] = { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
			0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0 };

	private static int K2[] = { 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0,
			0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0 };

	private static int A3[] = { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1,
			1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1 };

	private static int B3[] = { 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0,
			0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0 };

	private static int C3[] = { 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
			0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0 };

	private static int D3[] = { 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0,
			0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0 };

	private static int E3[] = { 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0,
			0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1 };

	private static int J3[] = { 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
			0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0 };

	private static int K3[] = { 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1,
			0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1 };

	private static final int MAX_CLUSTERS = 25;
	private static final int INPUT_PATTERNS = 21;
	private static final int VEC_LEN = 63;
	private static final int VEC_XLEN = 5;
	private static final int VEC_YLEN = 5;
	private static final double DECAY_RATE = 0.96; // About 100 iterations.
	private static final double MIN_ALPHA = 0.01;
	private static final double RADIUS_REDUCTION_POINT = 0.023; // Last 20% of
																// iterations.

	private static double alpha = 0.6;
	private static double d[] = new double[MAX_CLUSTERS]; // Network nodes.

	// Weight matrix with randomly chosen values between 0.0 and 1.0
	private static double w[][] = new double[MAX_CLUSTERS][VEC_LEN];

	private static ArrayList<int[]> pattern = null;
	private static String names[] = null;

	private static void initialize() {
		pattern = new ArrayList<int[]>();
		pattern.add(A1);
		pattern.add(B1);
		pattern.add(C1);
		pattern.add(D1);
		pattern.add(E1);
		pattern.add(J1);
		pattern.add(K1);
		pattern.add(A2);
		pattern.add(B2);
		pattern.add(C2);
		pattern.add(D2);
		pattern.add(E2);
		pattern.add(J2);
		pattern.add(K2);
		pattern.add(A3);
		pattern.add(B3);
		pattern.add(C3);
		pattern.add(D3);
		pattern.add(E3);
		pattern.add(J3);
		pattern.add(K3);

		names = new String[] { "A1", "B1", "C1", "D1", "E1", "J1", "K1", "A2", "B2", "C2", "D2", "E2", "J2", "K2", "A3", "B3", "C3", "D3", "E3",
				"J3", "K3" };

		for (int i = 0; i < MAX_CLUSTERS; i++) {
			for (int j = 0; j < VEC_LEN; j++) {
				w[i][j] = new Random().nextDouble();
			}
		}

		return;
	}

	private static void training() {
		int iterations = 0;
		boolean reductionFlag = false;
		int reductionPoint = 0;
		int dMin = 0;

		while (alpha > MIN_ALPHA) {
			iterations += 1;

			for (int vecNum = 0; vecNum <= (INPUT_PATTERNS - 1); vecNum++) {
				// Compute input for all nodes.
				computeInput(pattern.get(vecNum));

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

	private static void computeInput(int[] vectorArray) {
		clearArray(d);

		for (int i = 0; i <= (MAX_CLUSTERS - 1); i++) {
			for (int j = 0; j <= (VEC_LEN - 1); j++) {
				d[i] += Math.pow((w[i][j] - vectorArray[j]), 2);
			} // j
		} // i
		return;
	}

	private static void updateWeights(int vectorNumber, int dMin) {
		int y = 0;
		int PointA = 0;
		int PointB = 0;
		boolean done = false;

		for (int i = 0; i < VEC_LEN; i++) {
			// Only include neighbors before radius reduction point is reached.
			if (alpha > RADIUS_REDUCTION_POINT) {
				y = 1;
				while (!done) {
					if (y == 1) { // Top row of 3.
						if (dMin > VEC_XLEN - 1) {
							PointA = dMin - VEC_XLEN - 1;
							PointB = dMin - VEC_XLEN + 1;
						} else {
							y = 2;
						}
					}
					if (y == 2) { // Middle row of 3.
						PointA = dMin - 1;
						// DMin is like an anchor position right between these
						// two.
						PointB = dMin + 1;
					}
					if (y == 3) { // Bottom row of 3.
						if (dMin < (VEC_XLEN * (VEC_YLEN - 1))) {
							PointA = dMin + VEC_XLEN - 1;
							PointB = dMin + VEC_XLEN + 1;
						} else {
							done = true;
						}
					}

					if (!done) {
						for (int DIndex = PointA; DIndex < PointB; DIndex++) {
							// Check if anchor is at left side.
							if (dMin % VEC_XLEN == 0) {
								// Check if anchor is at top.
								if (DIndex > PointA) {
									w[DIndex][i] = w[DIndex][i] + (alpha * (pattern.get(vectorNumber)[i] - w[DIndex][i]));
								}
								// Check if anchor is at right side.
							} else if ((dMin + 1) % VEC_XLEN == 0) {
								// Check if anchor is at top.
								if (DIndex < PointB) {
									w[DIndex][i] = w[DIndex][i] + (alpha * (pattern.get(vectorNumber)[i] - w[DIndex][i]));
								}
								// Otherwise, anchor is not at either side.
							} else {
								w[DIndex][i] = w[DIndex][i] + (alpha * (pattern.get(vectorNumber)[i] - w[DIndex][i]));
							}
						} // DIndex
					}

					if (y == 3) {
						done = true;
					}
					y += 1; // prepare to start the next row.

				}
			} else if (alpha <= RADIUS_REDUCTION_POINT) {
				// Update only the winner.
				w[dMin][i] = w[dMin][i] + (alpha * (pattern.get(vectorNumber)[i] - w[dMin][i]));
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
		// int i = 0;
		// int j = 0;
		int dMin = 0;

		// Print clusters created.
		System.out.println("Clusters for training input:");
		for (int vecNum = 0; vecNum < INPUT_PATTERNS; vecNum++) {
			// Compute input.
			computeInput(pattern.get(vecNum));

			// See which is smaller.
			dMin = minimum(d);

			System.out.print("\nVector (");
			System.out.print("Pattern " + vecNum + ", " + names[vecNum]);
			System.out.print(") fits into category " + dMin + "\n");

		} // VecNum

		// The weight matrix is HUGE, and I'd found the output easier to read
		// just by commenting out that part...
		// Print weight matrix.
		// System.out.println();
		// for(int i = 0; i < MAX_CLUSTERS - 1;i++)
		// {
		// System.out.println("Weights for Node " + i + " connections:");
		// for(int j = 0; j < VEC_LEN - 1; j++)
		// {
		// String temp = String.format("%.3f", w[i][j]);
		// System.out.print(temp + ", ");
		// } // j
		// System.out.println("\n");
		// } // i

	}

	public static void main(String[] args) {
		initialize();
		training();
		printResults();
		return;
	}
}
