// Oct 16, 2015 10:39:02 AM
package harlequinmettle.investmentadviserengine.neuralnet.data;

import java.util.Arrays;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataSet {
	// TODO : DATA NORMALIZATION
	public int numberDataSets = 0;
	public int numberTestDataSets = 0;
	public CopyOnWriteArrayList<float[]> testingInputs = new CopyOnWriteArrayList<float[]>();
	public ConcurrentSkipListMap<Integer, float[]> testingOutputs = new ConcurrentSkipListMap<Integer, float[]>();

	public CopyOnWriteArrayList<float[]> trainingInputs = new CopyOnWriteArrayList<float[]>();
	public ConcurrentSkipListMap<Integer, float[]> trainingOutputs = new ConcurrentSkipListMap<Integer, float[]>();
	public CopyOnWriteArrayList<float[]> targets = new CopyOnWriteArrayList<float[]>();
	public float ssqError = Float.NaN;

	// Oct 21, 2015 9:34:20 AM
	@Override
	public String toString() {
		String response = "";
		for (int i = 0; i < numberDataSets; i++) {
			response += " {" + Arrays.toString(trainingInputs.get(i)) + " : " + Arrays.toString(targets.get(i)) + "} ";
		}
		return response;
	}

	protected void addTestInput(float... in) {

		testingInputs.add(in);
		numberTestDataSets++;
	}

	protected void addTargetOutputWithOptionalNumberInputs(Float out, float... in) {
		float[] input = in;
		float[] output = { out };
		trainingInputs.add(input);
		targets.add(output);
		numberDataSets++;
	}

}
