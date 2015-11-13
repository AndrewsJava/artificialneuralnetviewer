// Nov 11, 2015 10:26:53 AM
package harlequinmettle.investmentadviserengine.neuralnet.som;

import java.util.Arrays;

public class SOM {

	final static int trainingFourCategories[][] = new int[][] { //
	//
			{//
				// 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,//

					1, 1, 1, 1,//
					0, 0, 0, 0,//
					0, 0, 0, 0,//
					0, 0, 0, 0 //
			},//
			{//
				//
					0, 0, 0, 0,//
					1, 1, 1, 1,//
					0, 0, 0, 0,//
					0, 0, 0, 0 //
			},//
			{//
				//
					0, 0, 0, 0,//
					0, 0, 0, 0, //
					1, 1, 1, 1,//
					0, 0, 0, 0, //
			},//
			{//
				//
					0, 0, 0, 0,//
					0, 0, 0, 0, //
					0, 0, 0, 0, //
					1, 1, 1, 1,//
			},//

	};

	final static int testingFourCategories[][] = new int[][] { //
	//
			{//
				// 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,//

					1, 0, 0, 0,//
					0, 0, 0, 0,//
					0, 0, 0, 0,//
					0, 0, 0, 0 //
			},//
			{//
				//
					0, 0, 0, 0,//
					0, 0, 1, 0, //
					0, 0, 0, 0,//
					0, 0, 0, 0 //
			},//
			{//
				//
					0, 0, 0, 0,//
					0, 0, 0, 0, //
					0, 0, 1, 0,//
					0, 0, 0, 0,//
			},//
			{//
				//
					0, 0, 0, 0,//
					0, 0, 0, 0, //
					0, 0, 0, 0,//
					0, 0, 0, 1, //
			},//

	};

	public static void main(String[] args) {

		SOM som = new SOM(trainingFourCategories, testingFourCategories);
		som.doTrainingIterations();

		System.out.println(" ");
		System.out.println("Clusters for training input:");
		som.Test(som.trainingInputSet);

		System.out.println("Categorized test input:");
		som.Test(som.testingInputSet);

		System.out.println("Iterations: " + som.mIterations + "\n");
	}

	private int numberTrainingVectors;
	private int rawInputFeatureCount;
	private int maxClassificationOptions = 4;
	private float minAlpha = 0.001f;
	private float mAlpha = 0.99996f;
	private float decayRate = 0.99999f;
	private int mIterations;
	private float euclidianDistances[];

	private int trainingInputSet[][];
	private int testingInputSet[][];
	private float weights[][];

	public SOM(int[][] train, int[][] tests) {
		this.trainingInputSet = train;
		this.testingInputSet = tests;
		numberTrainingVectors = train.length;
		rawInputFeatureCount = train[0].length;
		randomWeights();
		mIterations = 0;
		euclidianDistances = new float[maxClassificationOptions];

	}

	private void randomWeights() {
		// Nov 13, 2015 8:41:48 AM
		weights = new float[maxClassificationOptions][rawInputFeatureCount];
		for (int i = 0; i < maxClassificationOptions; i++) {
			for (int j = 0; j < rawInputFeatureCount; j++) {
				weights[i][j] = (float) Math.random();
			}
		}

	}

	private void printWeights() {
		// Nov 13, 2015 8:41:48 AM
		for (int i = 0; i < maxClassificationOptions; i++) {
			System.out.println(i + " :      " + Arrays.toString(weights[i]));
		}

	}

	public void doTrainingIterations() {

		while (mAlpha > minAlpha) {
			mIterations++;

			for (int trainingVectorIndex = 0; trainingVectorIndex < numberTrainingVectors; trainingVectorIndex++) {
				// Compute input.
				computeEuclideanDistances(trainingInputSet[trainingVectorIndex]);

				// See which is smaller, D(0) or D(1)?
				int DMin = indexOfMinDistance(euclidianDistances);
				// System.out.println("Closest is D(" + DMin + ")");

				// Update the weights on the winning unit.
				for (int i = 0; i < numberTrainingVectors; i++) {
					weights[DMin][i] = weights[DMin][i] + (mAlpha * (trainingInputSet[trainingVectorIndex][i] - weights[DMin][i]));

				}

			}

			// Reduce the learning rate.
			mAlpha = decayRate * mAlpha;

		}
		return;
	}

	private int indexOfMinDistance(float[] dist) {
		// Nov 13, 2015 8:38:03 AM
		int index = 0;
		float min = Float.POSITIVE_INFINITY;
		for (int i = 0; i < dist.length; i++) {
			if (dist[i] < min) {
				min = dist[i];
				index = i;
			}
		}
		return index;
	}

	public void Test(int[][] dataTest) {
		java.text.DecimalFormat dfm = new java.text.DecimalFormat("###.0");

		for (int testIndex = 0; testIndex < numberTrainingVectors; testIndex++) {
			// Compute input.
			computeEuclideanDistances(dataTest[testIndex]);

			// See which is smaller, D(0) or D(1)?
			int DMin = indexOfMinDistance(euclidianDistances);

			System.out.print("\nVector ( ");
			System.out.print(Arrays.toString(dataTest[testIndex]));

			System.out.println(") fits into category " + DMin);
		} // VecNum

		// Print weight matrix.
		System.out.println();
		for (int i = 0; i < maxClassificationOptions; i++) {
			System.out.println("Weights for Node " + i + " connections:");
			for (int j = 0; j < rawInputFeatureCount; j++) {
				System.out.print(dfm.format(weights[i][j]) + ", ");
			} // j
			System.out.println();
		} // i
	}

	private void computeEuclideanDistances(int[] testInput) {

		Arrays.fill(euclidianDistances, 0);
		for (int i = 0; i < maxClassificationOptions; i++) {
			for (int j = 0; j < rawInputFeatureCount; j++) {
				euclidianDistances[i] += Math.pow((weights[i][j] - testInput[j]), 2);
				// System.out.println("D= " + D[i]);
			}
		}
		return;
	}

}
