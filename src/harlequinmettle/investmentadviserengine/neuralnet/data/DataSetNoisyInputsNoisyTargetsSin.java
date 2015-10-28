// Oct 27, 2015 11:51:45 AM
package harlequinmettle.investmentadviserengine.neuralnet.data;

public class DataSetNoisyInputsNoisyTargetsSin extends DataSet {

	float start = -4;
	float end = 4;
	float pointsCount = 30;

	public DataSetNoisyInputsNoisyTargetsSin() {
		// Oct 27, 2015 11:54:13 AM
		buildNoisySinTargetTrainingSetWithNoiseInputs();
	}

	// Oct 27, 2015 11:51:45 AM
	private void buildNoisySinTargetTrainingSetWithNoiseInputs() {
		float increment = (end - start) / pointsCount;

		for (float f = start; f < end; f += increment) {

			Float targetOut = new Float((float) (Math.sin(f) + Math.random() * 0.2));
			addTargetOutputWithOptionalNumberInputs(targetOut, (float) Math.cos(f), f, f, (float) (Math.random()), (float) (Math.random()),
					(float) (Math.random()), (float) (Math.random()), (float) (Math.random()));
		}
	}
}
