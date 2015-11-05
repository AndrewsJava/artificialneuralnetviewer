// Oct 16, 2015 10:39:02 AM
package harlequinmettle.investmentadviserengine.neuralnet.data;

import harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet.MinMax;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataSet implements Serializable {

	private static final long serialVersionUID = -317418868477704016L;
	// TODO : DATA NORMALIZATION
	public int numberDataSets = 0;
	public int numberTestDataSets = 0;
	public CopyOnWriteArrayList<float[]> testingInputs = new CopyOnWriteArrayList<float[]>();
	public ConcurrentSkipListMap<Integer, float[]> testingOutputs = new ConcurrentSkipListMap<Integer, float[]>();

	public CopyOnWriteArrayList<float[]> trainingInputs = new CopyOnWriteArrayList<float[]>();
	public ConcurrentSkipListMap<Integer, float[]> trainingOutputs = new ConcurrentSkipListMap<Integer, float[]>();
	public CopyOnWriteArrayList<float[]> targets = new CopyOnWriteArrayList<float[]>();
	public float ssqError = Float.NaN;
	public float avgError = Float.NaN;

	public ConcurrentSkipListMap<Integer, MinMax> inputNormalizationMinMax = new ConcurrentSkipListMap<Integer, MinMax>();
	public ConcurrentSkipListMap<Integer, MinMax> targetNormalizationMinMax = new ConcurrentSkipListMap<Integer, MinMax>();

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
		calculateForNormalization(in, inputNormalizationMinMax);
	}

	private void calculateForNormalization(float[] data, ConcurrentSkipListMap<Integer, MinMax> normalizationSets) {
		// Nov 5, 2015 11:45:47 AM
		for (int i = 0; i < data.length; i++) {
			if (normalizationSets.containsKey(i)) {
				normalizationSets.get(i).isSetMinMax(data[i]);
			} else {
				MinMax normalizationMimMax = new MinMax();
				normalizationMimMax.isSetMinMax(data[i]);
				normalizationSets.put(i, normalizationMimMax);
			}
		}
	}

	protected void addTargetWithInputs(float[] out, float[] in) {
		trainingInputs.add(in);
		targets.add(out);
		numberDataSets++;
		calculateForNormalization(in, inputNormalizationMinMax);
		calculateForNormalization(out, targetNormalizationMinMax);
	}

	public void normalizeInputs() {
		normalize(inputNormalizationMinMax, trainingInputs);
	}

	public void normalizeTargets() {
		normalize(targetNormalizationMinMax, targets);

	}

	public static void normalize(ConcurrentSkipListMap<Integer, MinMax> dataDistributions, CopyOnWriteArrayList<float[]> dataToNormalize) {

		float low = -1;// a
		float high = 1;// b

		for (Entry<Integer, MinMax> ent : dataDistributions.entrySet()) {
			int featureIndex = ent.getKey();
			float min = ent.getValue().min;
			float max = ent.getValue().max;
			for (float[] originalInput : dataToNormalize) {
				float originalFeautreInput = originalInput[featureIndex];
				float normalizedInput = low + ((originalFeautreInput - min) * (high - low) / (max - min));
				originalInput[featureIndex] = normalizedInput;
			}
		}
	}
}
