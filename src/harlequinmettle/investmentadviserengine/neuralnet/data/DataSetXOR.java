// Oct 16, 2015 10:51:12 AM
package harlequinmettle.investmentadviserengine.neuralnet.data;

public class DataSetXOR extends DataSet {
	// Oct 16, 2015 10:51:12 AM

	public DataSetXOR() {
		// Oct 16, 2015 10:39:47 AM
		buildXORTrainingSet();
	}

	// Oct 16, 2015 10:42:00 AM
	private void buildXORTrainingSet() {
		addXORDataMappingToTrainingSet(0, 0, 0);
		addXORDataMappingToTrainingSet(0, 1, 1);
		addXORDataMappingToTrainingSet(1, 0, 1);
		addXORDataMappingToTrainingSet(1, 1, 0);
	}

	// Oct 16, 2015 10:48:20 AM
	private void addXORDataMappingToTrainingSet(int in1, int in2, int out) {
		float[] input = { in1, in2 };
		float[] output = { out };
		trainingInputs.add(input);
		targets.add(output);
		numberDataSets++;
	}
}
