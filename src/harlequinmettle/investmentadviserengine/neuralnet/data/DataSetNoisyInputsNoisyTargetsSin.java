// Oct 27, 2015 11:51:45 AM
package harlequinmettle.investmentadviserengine.neuralnet.data;

public class DataSetNoisyInputsNoisyTargetsSin extends DataSet {

	float start = -4;
	float end = 4;
	float pointsCount = 30;

	public DataSetNoisyInputsNoisyTargetsSin(float start, float end, float pointsCount) {
		// Oct 27, 2015 11:54:13 AM
		buildNoisySinTargetTrainingSetWithNoiseInputs(start, end, pointsCount);
	}

	public DataSetNoisyInputsNoisyTargetsSin() {
		// Oct 27, 2015 11:54:13 AM
		buildNoisySinTargetTrainingSetWithNoiseInputs(start, end, pointsCount);
		buildNoisySinTestingSet(start, end, pointsCount);
	}

	private void buildNoisySinTestingSet(float start, float end, float pointsCount) {
		// extend the range and double the points count for test set
		float range = end - start;
		start = start - 0.15f * range;
		end = end + 0.15f * range;
		pointsCount *= 2f;
		float increment = (end - start) / pointsCount;

		for (float f = start; f < end; f += increment) {

			addTestInput(f, (float) Math.cos(f), f, f, (float) (Math.random()), (float) (Math.random()), (float) (Math.random()),
					(float) (Math.random()), (float) (Math.random()));
		}
	}

	// Oct 27, 2015 11:51:45 AM
	private void buildNoisySinTargetTrainingSetWithNoiseInputs(float start, float end, float pointsCount) {
		float increment = (end - start) / pointsCount;

		for (float f = start; f < end; f += increment) {

			Float targetOut = new Float((float) (Math.sin(f) + Math.random() * 0.2));
			addTargetOutputWithOptionalNumberInputs(targetOut, f, (float) Math.cos(f), f, f, (float) (Math.random()), (float) (Math.random()),
					(float) (Math.random()), (float) (Math.random()), (float) (Math.random()));
		}
	}
}
