// Oct 21, 2015 8:08:08 AM
package harlequinmettle.investmentadviserengine.neuralnet.data;

import harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet.ArtificailNeuralNet;
import harlequinmettle.utils.reflection.RuntimeDetails;

public class DataSetNoisySin extends DataSet {
	// Oct 21, 2015 8:08:08 AM
	public DataSetNoisySin() {
		// Oct 16, 2015 10:39:47 AM
		buildNoisySinTrainingSet();
		if (ArtificailNeuralNet.debugObjectConstructionWithReflection)
			RuntimeDetails.getPrintClassInfo(this);
	}

	private void buildNoisySinTrainingSet() {
		// Oct 21, 2015 9:04:31 AM
		float start = -4;
		float end = 4;
		float pointsCount = 60;
		float increment = (end - start) / pointsCount;

		for (float f = start; f < end; f += increment) {
			// addNoisySinDataMappingToTrainingSet(f, (float) (Math.sin(f) +
			// Math.random() * 0.2));

			addNoisySinDataMappingToTrainingSet(f, (float) (4 * Math.random()), (float) (Math.sin(f) + Math.random() * 0.2));
		}
	}

	private void addNoisySinDataMappingToTrainingSet(float in1, float inNoise, float out) {
		float[] input = { in1, inNoise };
		float[] output = { out };
		inputs.add(input);
		targets.add(output);
		numberDataSets++;
	}

	private void addNoisySinDataMappingToTrainingSet(Float out, float... in) {
		float[] input = in;
		float[] output = { out };
		inputs.add(input);
		targets.add(output);
		numberDataSets++;
	}

	private void addNoisySinDataMappingToTrainingSet(float in1, float out) {
		float[] input = { in1 };
		float[] output = { out };
		inputs.add(input);
		targets.add(output);
		numberDataSets++;
	}
}
