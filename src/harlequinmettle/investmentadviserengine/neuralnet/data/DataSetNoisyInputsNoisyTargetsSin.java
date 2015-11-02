// Oct 27, 2015 11:51:45 AM
package harlequinmettle.investmentadviserengine.neuralnet.data;

public class DataSetNoisyInputsNoisyTargetsSin extends DataSet {

	float start = -4;
	float end = 4;
	float pointsCount = 30;
	private float sinNormalizationFactor = Float.NaN;
	private float sinNormalizationOffset = Float.NaN;

	public DataSetNoisyInputsNoisyTargetsSin(float start, float end, float pointsCount) {
		// Oct 27, 2015 11:54:13 AM
		this.start = start;
		this.end = end;
		this.pointsCount = pointsCount;
		normalizeInput();
		buildNoisySinTargetTrainingSetWithNoiseInputs();
		buildNoisySinTestingSet();
	}

	private void normalizeInput() {
		// Nov 2, 2015 8:38:25 AM
		sinNormalizationFactor = 0.5f * (end - start);
		sinNormalizationOffset = 0;// start * 0.5f;
		start = -1;
		end = 1;
	}

	public DataSetNoisyInputsNoisyTargetsSin() {
		// Oct 27, 2015 11:54:13 AM
		normalizeInput();
		buildNoisySinTargetTrainingSetWithNoiseInputs();
		buildNoisySinTestingSet();
	}

	private void buildNoisySinTestingSet() {
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
		float[] input = { f, (float) (2.2 * Math.random()) };
		return input;
	}

	// Oct 27, 2015 11:51:45 AM
	private void buildNoisySinTargetTrainingSetWithNoiseInputs() {
		float increment = (end - start) / pointsCount;

		for (float f = start; f < end; f += increment) {
			addPointsVeryNearForTest(f, increment);
			Float targetOut = getTargetOutput(f);
			float[] inputPattern = generateInputPattern(f);
			addTargetOutputWithOptionalNumberInputs(targetOut, inputPattern);
		}
	}

	private Float getTargetOutput(float f) {
		// Nov 2, 2015 8:48:43 AM
		float magnitude = 0.01f;
		float noise = (float) (-magnitude + 2 * Math.random() * magnitude);
		Float targetOut = new Float((float) (Math.sin(sinNormalizationFactor * f + sinNormalizationOffset) + noise));
		return targetOut;
	}

	// Nov 1, 2015 7:39:20 AM
	private void addPointsVeryNearForTest(float f, float increment) {
		float pointCount = 20;
		float interval = 0.48e-6f;// Float.MIN_VALUE * 1e12f;
		float start = f - pointCount / 2 * interval;
		float end = f + pointCount / 2 * interval;

		for (float near = start; near < end; near += interval) {

			float[] inputPattern = generateInputPattern(near);
			addTestInput(inputPattern);

		}

	}

	// Nov 1, 2015 7:39:20 AM
	private void addPointsVeryNearForTesta(float f, float increment) {
		float pointCount = 20;
		float start = f - increment / 10000;
		float end = f + increment / 10000;
		float range = end - start;
		float interval = range / pointCount;
		for (float near = start; near < end; near += interval) {

			float[] inputPattern = generateInputPattern(near);
			addTestInput(inputPattern);

		}

	}
}
