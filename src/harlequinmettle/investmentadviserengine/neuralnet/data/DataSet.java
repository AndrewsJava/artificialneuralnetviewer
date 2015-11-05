// Oct 16, 2015 10:39:02 AM
package harlequinmettle.investmentadviserengine.neuralnet.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
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

	public ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<Float>> inputNormalizationSets = new ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<Float>>();
	public ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<Float>> targetNormalizationSets = new ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<Float>>();

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
		addToNormalizationSets(in, inputNormalizationSets);
	}

	private void addToNormalizationSets(float[] data, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<Float>> normalizationSets) {
		// Nov 5, 2015 11:45:47 AM
		for (int i = 0; i < data.length; i++) {
			if (normalizationSets.containsKey(i)) {
				normalizationSets.get(i).add(data[i]);
			} else {
				CopyOnWriteArrayList<Float> normalizationSet = new CopyOnWriteArrayList<Float>();
				normalizationSet.add(data[i]);
				normalizationSets.put(i, normalizationSet);
			}
		}
	}

	protected void addTargetOutputWithOptionalNumberInputs(float[] out, float[] in) {
		trainingInputs.add(in);
		targets.add(out);
		numberDataSets++;
		addToNormalizationSets(in, inputNormalizationSets);
		addToNormalizationSets(out, targetNormalizationSets);
	}

	protected void addTargetOutputWithOptionalNumberInputs(Float out, float... in) {
		float[] input = in;
		float[] output = { out };
		trainingInputs.add(input);
		targets.add(output);
		numberDataSets++;
	}

	public void normalizeInputs() {

	}

	public void normalizeTargets() {

	}

	public static void normalize(ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<Float>> dataDistributions,
			CopyOnWriteArrayList<float[]> dataToNormalize) {

		float low = -1;// a
		float high = 1;// b

		for (Entry<Integer, CopyOnWriteArrayList<Float>> ent : dataDistributions.entrySet()) {
			int featureIndex = ent.getKey();
			CopyOnWriteArrayList<Float> fullFeatureDataDistribution = ent.getValue();
			float min = Collections.min(fullFeatureDataDistribution);
			float max = Collections.max(fullFeatureDataDistribution);
			for (float[] originalInput : dataToNormalize) {
				float originalFeautreInput = originalInput[featureIndex];
				float normalizedInput = low + ((originalFeautreInput - min) * (high - low) / (max - min));
				originalInput[featureIndex] = normalizedInput;
			}
		}
	}
}
