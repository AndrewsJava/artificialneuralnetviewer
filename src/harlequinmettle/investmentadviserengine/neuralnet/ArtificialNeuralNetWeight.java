// Oct 17, 2015 9:00:41 AM
package harlequinmettle.investmentadviserengine.neuralnet;

import harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet.ArtificailNeuralNet;
import harlequinmettle.utils.reflection.RuntimeDetails;

import java.io.Serializable;

public class ArtificialNeuralNetWeight implements Serializable {
	// Oct 17, 2015 9:00:41 AM

	private static final long serialVersionUID = -4968994540746410966L;

	public float weight;

	public float weightChange;

	public ArtificialNeuralNetWeight() {
		randomizeToPositive();
		this.weightChange = 0;
		if (ArtificailNeuralNet.debugObjectConstructionWithReflection)
			RuntimeDetails.getPrintClassInfo(this);
	}

	public ArtificialNeuralNetWeight(float value) {
		this.weight = value;
		if (ArtificailNeuralNet.debugObjectConstructionWithReflection)
			RuntimeDetails.getPrintClassInfo(this);
	}

	public void setWeightChange(float change) {
		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
		weightChange = change;
	}

	public void applyWeightChange() {
		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
		weight += weightChange;
		weightChange = 0;
	}

	public void randomizeToPositive() {
		weight = (float) (Math.random());
	}

	public void randomize() {
		weight = (float) (2 * Math.random() - 1);
	}

	public void randomize(float min, float max) {
		weight = (float) (min + Math.random() * (max - min));
	}
}
