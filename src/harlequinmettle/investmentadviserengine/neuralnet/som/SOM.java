// Nov 11, 2015 10:26:53 AM
package harlequinmettle.investmentadviserengine.neuralnet.som;

import java.util.Arrays;

public class SOM {

	final static int trainingClearCategorie[][] = new int[][] { //
	//
			{//
				// 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,//

					1, 1, 0, 0,//
					1, 1, 0, 0,//
					0, 0, 0, 0,//
					0, 0, 0, 0 //
			},//
			{//
				//
					0, 0, 1, 1,//
					0, 0, 1, 1, //
					0, 0, 0, 0,//
					0, 0, 0, 0 //
			},//
			{//
				//
					0, 0, 0, 0,//
					0, 0, 0, 0, //
					1, 1, 0, 0,//
					1, 1, 0, 0,//
			},//
			{//
				//
					0, 0, 0, 0,//
					0, 0, 0, 0, //
					0, 0, 1, 1,//
					0, 0, 1, 1, //
			},//

	};

	public static void main(String[] args) {

		final int Pattern[][] = new int[][] { //
		//
				{ 1, 0, 0, 0 },//
				{ 0, 0, 0, 1 }, //
				{ 0, 1, 0, 0 }, //
				{ 0, 0, 1, 0 } };

		final int Tests[][] = new int[][] { //
		//
				{ 1, 0, 0, 1 }, //
				{ 0, 1, 1, 0 }, //
				{ 1, 0, 1, 0 },//
				{ 0, 1, 0, 1 } };

		SOM som = new SOM(trainingClearCategorie, trainingClearCategorie);
		som.Train();
		som.Test();

		System.out.println("Iterations: " + som.mIterations + "\n");
	}

	private int mVectors;
	private int mVecLen;
	private int maxClusters = 4;
	private double minAlpha = 0.01;
	private double mAlpha = 0.96;
	private double decayRate = 0.999;
	private int mIterations;
	private double euclidianDistances[];

	private int pattern[][];
	private int tests[][];
	private double weights[][] = new double[][] { { 0.2, 0.6, 0.5, 0.9 }, { 0.8, 0.4, 0.7, 0.3 } };

	public SOM(int[][] pattern, int[][] tests) {
		this.pattern = pattern;
		this.tests = tests;
		mVectors = pattern.length;
		mVecLen = pattern[0].length;
		randomWeights();
		mIterations = 0;
		euclidianDistances = new double[maxClusters];

	}

	private void randomWeights() {
		// Nov 13, 2015 8:41:48 AM
		weights = new double[maxClusters][mVecLen];
		for (int i = 0; i < maxClusters; i++) {
			for (int j = 0; j < mVecLen; j++) {
				weights[i][j] = Math.random();
			}
		}

	}

	public void Train() {

		int i;
		int VecNum;
		int DMin;

		while (mAlpha > minAlpha) {
			mIterations += 1;

			for (VecNum = 0; VecNum < mVectors; VecNum++) {
				// Compute input.
				computeEuclideanDistances(VecNum);

				// See which is smaller, D(0) or D(1)?
				DMin = indexOfMinDistance(euclidianDistances);
				// System.out.println("Closest is D(" + DMin + ")");

				// Update the weights on the winning unit.
				for (i = 0; i < mVectors; i++) {
					weights[DMin][i] = weights[DMin][i] + (mAlpha * (pattern[VecNum][i] - weights[DMin][i]));
					// System.out.println(" w(" + i + ")= " + w[DMin][i]);
				}

			}

			// Reduce the learning rate.
			mAlpha = decayRate * mAlpha;

		}
		return;
	}

	private int indexOfMinDistance(double[] dist) {
		// Nov 13, 2015 8:38:03 AM
		int index = 0;
		double min = Double.POSITIVE_INFINITY;
		for (int i = 0; i < dist.length; i++) {
			if (dist[i] < min) {
				min = dist[i];
				index = i;
			}
		}
		return index;
	}

	public void Test() {
		int i, j;
		int VecNum;
		int DMin;
		java.text.DecimalFormat dfm = new java.text.DecimalFormat("###.000");

		// Print clusters created.
		System.out.println("Clusters for training input:");

		for (VecNum = 0; VecNum < mVectors; VecNum++) {
			// Compute input.
			computeEuclideanDistances(VecNum);

			// See which is smaller, D(0) or D(1)?
			DMin = indexOfMinDistance(euclidianDistances);

			System.out.print("\nVector ( ");
			for (i = 0; i < mVectors; i++) {
				System.out.print(pattern[VecNum][i] + ", ");
			} // i
			System.out.println(") fits into category " + DMin);
		} // VecNum

		// Print weight matrix.
		System.out.println();
		for (i = 0; i < maxClusters; i++) {
			System.out.println("Weights for Node " + i + " connections:");
			for (j = 0; j < mVecLen; j++) {
				System.out.print(dfm.format(weights[i][j]) + ", ");
			} // j
			System.out.println();
		} // i

		// Print post-training tests.
		System.out.println("Categorized test input:");
		for (VecNum = 0; VecNum < mVectors; VecNum++) {
			// Compute input.
			computeEuclideanDistances(VecNum);

			// See which is smaller, D(0) or D(1)?
			DMin = indexOfMinDistance(euclidianDistances);

			System.out.print("\nVector ( ");
			for (i = 0; i < mVectors; i++) {
				System.out.print(tests[VecNum][i] + ", ");
			} // i
			System.out.println(") fits into category " + DMin);

		} // VecNum
		return;
	}

	private void computeEuclideanDistances(int testInputIndex) {
		int i, j;
		Arrays.fill(euclidianDistances, 0);
		for (i = 0; i < maxClusters; i++) {
			for (j = 0; j < mVectors; j++) {
				euclidianDistances[i] += Math.pow((weights[i][j] - tests[testInputIndex][j]), 2);
				// System.out.println("D= " + D[i]);
			}
		}
		return;
	}

}
