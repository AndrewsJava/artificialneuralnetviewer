// Oct 17, 2015 11:00:58 AM
package harlequinmettle.investmentadviserengine.neuralnet.transferfunction;

import harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet.ArtificailNeuralNet;
import harlequinmettle.utils.reflection.RuntimeDetails;

import java.io.Serializable;

public class SigmoidTransferFunction implements Serializable {

	private static final long serialVersionUID = 310723662430627233L;

	// Oct 17, 2015 11:02:19 AM
	public float calculateSigmoidalOutput(float value) {
		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
		// if (value > 100) {
		// return 1.0f;
		// } else if (value < -100) {
		// return 0.0f;
		// }
		// float den = (float) (1 + Math.exp(-this.slope * value));
		// float denominator = (float) (1 + Math.exp(-value));
		// float forwardPassNeuronResultCache = (1 / denominator) - 0.5f;
		// float result = (float) (1 / (1 + Math.exp(-value))) - 0.5f;
		float result = (float) (1 / (1 + Math.exp(-value)));
		return result;
	}

	public float getDerivative(float x) {
		// float derivative = this.slope * this.output * (1 - this.output) +
		// 0.1f;
		float derivative = x * (1 - x) + 0.1f;
		return derivative;
	}
}
