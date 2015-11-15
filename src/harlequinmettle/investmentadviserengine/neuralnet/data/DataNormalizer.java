// Nov 15, 2015 11:02:23 AM
package harlequinmettle.investmentadviserengine.neuralnet.data;

import harlequinmettle.investmentadviserengine.neuralnet.feedforwardwithbackprop.MinMax;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataNormalizer {
	public void normalizeToStandardDeviation(ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<Float>> dataDistributions,
			Collection<float[]> dataToNormalize) {
		// TODO: ...
	}

	public void normalize(ConcurrentSkipListMap<Integer, MinMax> dataDistributions, CopyOnWriteArrayList<float[]> dataToNormalize) {

		System.out.println(dataDistributions);
		float low = -1;// a
		float high = 1;// b

		for (Entry<Integer, MinMax> ent : dataDistributions.entrySet()) {
			int featureIndex = ent.getKey();
			float min = ent.getValue().min;
			float max = ent.getValue().max;
			for (float[] originalInput : dataToNormalize) {
				float originalFeautreInput = originalInput[featureIndex];
				System.out.print(originalFeautreInput + " ---> ");
				float normalizedInput = low + ((originalFeautreInput - min) * (high - low) / (max - min));
				System.out.println(normalizedInput);
				originalInput[featureIndex] = normalizedInput;
			}
		}
	}

	public void calculateMinMaxForNormalization(float[] data, ConcurrentSkipListMap<Integer, MinMax> normalizationSets) {
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
}
