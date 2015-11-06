// Oct 27, 2015 11:51:45 AM
package harlequinmettle.investmentadviserengine.neuralnet.data;

import harlequinmettle.investmentadviserengine.neuralnet.Global;

import java.io.Serializable;

public class DataSetNoisyInputsNoisyTargetsSin extends DataSet implements Serializable {

	private static final long serialVersionUID = -2795220404710467533L;
	float start = -4;
	float end = 4;
	float pointsCount = 30;
	private float sinNormalizationFactor = 1;
	private float sinNormalizationOffset = 0;

	public DataSetNoisyInputsNoisyTargetsSin(float start, float end, float pointsCount) {
		this.start = start;
		this.end = end;
		this.pointsCount = pointsCount;
		constructDataSet();
	}

	public DataSetNoisyInputsNoisyTargetsSin() {
		constructDataSet();
	}

	private void constructDataSet() {
		// normalizeInput();
		buildNoisySinTargetTrainingSetWithNoiseInputs();
		buildNoisySinTestingSet();
		normalizeInputs();
		normalizeTargets();
		normalizeTesting();
	}

	// Oct 27, 2015 11:51:45 AM
	private void buildNoisySinTargetTrainingSetWithNoiseInputs() {
		float increment = (end - start) / pointsCount;

		for (float f = start; f < end; f += increment) {
			// addPointsVeryNearForTest(f, increment);
			float targetOut = getTargetOutput(f);
			float[] t = { 5 * targetOut };
			float[] inputPattern = generateInputPattern(f);
			addTargetWithInputs(inputPattern, t);
		}
	}

	// Nov 2, 2015 8:48:43 AM
	private float getTargetOutput(float f) {
		float magnitude = 0.01f;
		float noise = (float) (-magnitude + 2 * Global.random() * magnitude);
		float targetOut = ((float) (Math.sin(sinNormalizationFactor * f + sinNormalizationOffset) + noise));
		return targetOut;
	}

	// Oct 31, 2015 9:40:05 AM
	private float[] generateInputPattern(float f) {
		float[] input = { f + (float) (f + 0.052 * Global.random()) };
		// float[] input = { f, (float) (2.0 * Global.random()) };
		// float[] input = { f };
		return input;
	}

	private void buildNoisySinTestingSet() {
		float start, end, pointsCount;
		float range = this.end - this.start;
		start = this.start - 0.15f * range;
		end = this.end + 0.15f * range;
		pointsCount = this.pointsCount * 20f;
		float increment = (end - start) / pointsCount;

		for (float f = start; f < end; f += increment) {
			float[] inputPattern = generateInputPattern(f);
			addTestInput(inputPattern);
		}
	}

	// Nov 1, 2015 7:39:20 AM
	private void addPointsVeryNearForTest(float f, float increment) {
		float pointCount = 20;
		float interval = 0.48e-6f;
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
