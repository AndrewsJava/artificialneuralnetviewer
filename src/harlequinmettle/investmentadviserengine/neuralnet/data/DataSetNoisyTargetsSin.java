// Oct 21, 2015 8:08:08 AM
package harlequinmettle.investmentadviserengine.neuralnet.data;


public class DataSetNoisyTargetsSin extends DataSet {
	// Oct 21, 2015 8:08:08 AM

	float start = -4;
	float end = 4;
	float pointsCount = 60;

	public DataSetNoisyTargetsSin() {
		// Oct 16, 2015 10:39:47 AM
		buildNoisySinTargetTrainingSet();
	}

	private void buildNoisySinTargetTrainingSet() {
		// Oct 21, 2015 9:04:31 AM
		float increment = (end - start) / pointsCount;

		for (float f = start; f < end; f += increment) {
			// addNoisySinDataMappingToTrainingSet(f, (float) (Math.sin(f) +
			// Math.random() * 0.2));

			addTargetOutputWithOptionalNumberInputs((float) (Math.sin(f) + Math.random() * 0.2), f);
		}
	}

}
