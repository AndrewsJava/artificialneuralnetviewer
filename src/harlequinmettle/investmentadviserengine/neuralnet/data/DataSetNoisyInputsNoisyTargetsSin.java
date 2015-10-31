// Oct 27, 2015 11:51:45 AM
package harlequinmettle.investmentadviserengine.neuralnet.data;

public class DataSetNoisyInputsNoisyTargetsSin extends DataSet {

	float start = -4;
	float end = 4;
	float pointsCount = 30;

	public DataSetNoisyInputsNoisyTargetsSin(float start, float end, float pointsCount) {
		// Oct 27, 2015 11:54:13 AM
		buildNoisySinTargetTrainingSetWithNoiseInputs(start, end, pointsCount);
		buildNoisySinTestingSet(start, end, pointsCount);
	}

	public DataSetNoisyInputsNoisyTargetsSin() {
		// Oct 27, 2015 11:54:13 AM
		buildNoisySinTargetTrainingSetWithNoiseInputs(start, end, pointsCount);
		buildNoisySinTestingSet(start, end, pointsCount);
	}

	private void buildNoisySinTestingSet(float start, float end, float pointsCount) {
		// extend the range and double the points count for test set
		float range = end - start;
		start = start - 0.45f * range;
		end = end + 0.45f * range;
		pointsCount *= 80f;
		float increment = (end - start) / pointsCount;

		for (float f = start; f < end; f += increment) {
			float[] inputPattern = generateInputPattern(f);
			addTestInput(inputPattern);
		}
	}

	private float[] generateInputPattern(float f) {
		// Oct 31, 2015 9:40:05 AM
		float[] input = { f, (float) (Math.random()) };
		return input;
	}

	// Oct 27, 2015 11:51:45 AM
	private void buildNoisySinTargetTrainingSetWithNoiseInputs(float start, float end, float pointsCount) {
		float increment = (end - start) / pointsCount;

		float magnitude = 1;
		for (float f = start; f < end; f += increment) {
			float noise = (float) (-magnitude + 2 * Math.random() * magnitude);
			Float targetOut = new Float((float) (Math.sin(f) + noise));
			float[] inputPattern = generateInputPattern(f);
			addTargetOutputWithOptionalNumberInputs(targetOut, inputPattern);
		}
	}
}
