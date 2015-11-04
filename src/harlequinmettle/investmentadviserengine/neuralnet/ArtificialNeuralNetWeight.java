// Oct 17, 2015 9:00:41 AM
package harlequinmettle.investmentadviserengine.neuralnet;

import java.io.Serializable;

public class ArtificialNeuralNetWeight implements Serializable {
	// Oct 17, 2015 9:00:41 AM

	private static final long serialVersionUID = -4968994540746410966L;

	public float weight;

	public float weightChange;

	public static float momentum = 0.9f;

	public float lastWeightChange = 0;

	public ArtificialNeuralNetWeight() {
		// randomizeToPositive();
		randomize();
		this.weightChange = 0;
	}

	public ArtificialNeuralNetWeight(float value) {
		this.weight = value;
	}

	public void setWeightChange(float change) {

		weightChange = change;
	}

	public void applyWeightChange() {

		weight += weightChange;
		weight += momentum * lastWeightChange;
	}

	public void revertWeightChange() {

		weight -= weightChange;
		weight -= momentum * lastWeightChange;
		weightChange = lastWeightChange;
		// lastWeightChange = savedLastWeightChange
	}

	public void commitWeightChange() {

		lastWeightChange = weightChange;
		weightChange = 0;
	}

	public void randomize() {
		weight = (float) (2 * Math.random() - 1) * 0.5f;
	}

	public void randomize(float min, float max) {
		weight = (float) (min + Math.random() * (max - min));
	}
}
