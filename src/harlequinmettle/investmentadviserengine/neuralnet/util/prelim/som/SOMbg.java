// Nov 15, 2015 9:06:37 AM
package harlequinmettle.investmentadviserengine.neuralnet.util.prelim.som;

import java.util.Arrays;

public class SOMbg {
	int numberTrainingVectors;
	int rawInputFeatureCount;
	int maxClassificationOptions = 16;
	float weights[][];
	final static int testingBasicDemoCategories[][] = new int[][] { //
	//
			{//
				// 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,//

					1, 1, 0 },//
			{//
				//
					0, -1, -1 },//
			{//
				//
					-1, 0, 0 },//
			{//
				//
					1, 0, 1 },//

	};
	final static int trainingBasicDemoCategories[][] = new int[][] { //
	//
			{//
				// 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,//

					1, 1, 1, },//
			{//
				//
					0, 0, -1 },//
			{//
				//
					-1, 1, 0 },//
			{//
				//
					1, 0, 0 },//

	};

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

	protected void printWeights() {
		// Nov 13, 2015 8:41:48 AM
		for (int i = 0; i < maxClassificationOptions; i++) {
			System.out.println(i + " :      " + Arrays.toString(weights[i]));
		}

	}

	void randomWeights() {
		// Nov 13, 2015 8:41:48 AM
		weights = new float[maxClassificationOptions][rawInputFeatureCount];
		for (int i = 0; i < maxClassificationOptions; i++) {
			for (int j = 0; j < rawInputFeatureCount; j++) {
				weights[i][j] = (float) Math.random();
			}
		}

	}
}
