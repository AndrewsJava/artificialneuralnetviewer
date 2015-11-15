// Nov 11, 2015 10:26:53 AM
package harlequinmettle.investmentadviserengine.neuralnet.util.prelim.som;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap;

public class SOM extends SOMbg {

	public static void main(String[] args) {

		SOM som = new SOM(trainingBasicDemoCategories, testingBasicDemoCategories);
		som.doTrainingIterations();

		System.out.println(" ");
		System.out.println("Clusters for training input:");
		som.Test(som.trainingInputSet);

		System.out.println("Categorized test input:");
		som.Test(som.testingInputSet);

		System.out.println("Iterations: " + som.mIterations + "\n");
	}

	private float minAlpha = 0.001f;
	private float mAlpha = 0.99996f;
	private float decayRate = 0.99999f;
	private int mIterations;
	private float euclidianDistances[];

	private int trainingInputSet[][];
	private int testingInputSet[][];
	TreeMap<Double, Integer> distanceMap = new TreeMap<Double, Integer>();

	public SOM(int[][] train, int[][] tests) {
		this.trainingInputSet = train;
		this.testingInputSet = tests;
		numberTrainingVectors = train.length;
		rawInputFeatureCount = train[0].length;
		randomWeights();
		mIterations = 0;
		euclidianDistances = new float[maxClassificationOptions];

	}

	public void doTrainingIterations() {

		while (mAlpha > minAlpha) {
			mIterations++;

			for (int trainingVectorIndex = 0; trainingVectorIndex < numberTrainingVectors; trainingVectorIndex++) {

				computeEuclideanDistances(trainingInputSet[trainingVectorIndex]);

				// int DMin = indexOfMinDistance(euclidianDistances);
				int farther = 0;
				for (Entry<Double, Integer> ent : distanceMap.entrySet()) {
					farther++;
					int DMin = ent.getValue();
					float distanceFactor = (float) Math.pow(Math.E, -(farther / 3));
					for (int i = 0; i < numberTrainingVectors; i++) {
						weights[i][DMin] = weights[i][DMin] + distanceFactor
								* (mAlpha * (trainingInputSet[trainingVectorIndex][DMin] - weights[i][DMin]));
					}
				}
				mAlpha = decayRate * mAlpha;
			}
		}
	}

	private void computeEuclideanDistances(int[] testInput) {
		distanceMap.clear();
		Arrays.fill(euclidianDistances, 0);
		for (int i = 0; i < maxClassificationOptions; i++) {
			for (int j = 0; j < rawInputFeatureCount; j++) {
				euclidianDistances[i] += Math.pow((weights[i][j] - testInput[j]), 2);
			}
			distanceMap.put(euclidianDistances[i] + Math.random() / 100000, i);
		}
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

}
