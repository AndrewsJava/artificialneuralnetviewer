// Oct 16, 2015 10:39:02 AM
package harlequinmettle.investmentadviserengine.neuralnet.data;

import harlequinmettle.investmentadviserengine.neuralnet.feedforwardwithbackprop.MinMax;

import java.io.Serializable;
import java.util.Arrays;
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
	public ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<Float>> inputNormalizationSets = new ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<Float>>();
	public DataNormalizer normalizer = new DataNormalizer();

	// public ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<Float>>
	// targetNormalizationSets = new ConcurrentSkipListMap<Integer,
	// CopyOnWriteArrayList<Float>>();

	protected void addTestInput(float... in) {

		testingInputs.add(in);
		numberTestDataSets++;
		// calculateForNormalization(in, inputNormalizationMinMax);
	}

	protected void addInputForSelfOrgainizingMap(float[] in) {
		trainingInputs.add(in);
		numberDataSets++;
		normalizer.calculateMinMaxForNormalization(in, inputNormalizationMinMax);
		addToNormalizationSets(in, inputNormalizationSets);
	}

	protected void addInputWithTargetOutput(float[] in, float[] out) {
		trainingInputs.add(in);
		targets.add(out);
		numberDataSets++;
		normalizer.calculateMinMaxForNormalization(in, inputNormalizationMinMax);
		normalizer.calculateMinMaxForNormalization(out, targetNormalizationMinMax);
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

	public void normalizeTesting() {
		normalizer.normalize(inputNormalizationMinMax, testingInputs);
	}

	public void normalizeInputs() {
		normalizer.normalize(inputNormalizationMinMax, trainingInputs);
		// / normalizeToStandardDeviation(inputNormalizationSets,
		// trainingInputs);
	}

	public void normalizeTargets() {

		normalizer.normalize(targetNormalizationMinMax, targets);

	}

	// Oct 21, 2015 9:34:20 AM
	@Override
	public String toString() {
		String response = "";
		for (int i = 0; i < numberDataSets; i++) {
			response += " {" + Arrays.toString(trainingInputs.get(i)) + " : " + Arrays.toString(targets.get(i)) + "} ";
		}
		return response;
	}

}
