// Oct 21, 2015 8:08:08 AM
package harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet;

import harlequinmettle.investmentadviserengine.neuralnet.data.DataSet;

public class DataSetNoisySin extends DataSet {
	// Oct 21, 2015 8:08:08 AM
	public DataSetNoisySin() {
		// Oct 16, 2015 10:39:47 AM
		buildNoisySinTrainingSet();
	}

	private void buildNoisySinTrainingSet() {
		// Oct 21, 2015 9:04:31 AM
		float start = -3;
		float end = 3;
		float pointsCount = 30;
		float increment = (end - start) / pointsCount;

		for (float f = start; f < end; f += increment) {
			addNoisySinDataMappingToTrainingSet(f, (float) (Math.sin(f) + Math.random() * 0.1));
		}
	}

	private void addNoisySinDataMappingToTrainingSet(float in1, float out) {
		float[] input = { in1 };
		float[] output = { out };
		inputs.add(input);
		targets.add(output);
		numberDataSets++;
	}
}
