// Oct 17, 2015 11:00:58 AM
package harlequinmettle.investmentadviserengine.neuralnet;

import java.io.Serializable;

public class SigmoidTransferFunction implements Serializable {

	private static final long serialVersionUID = 310723662430627233L;

	// float forwardPassNeuronResultCache = Float.NaN;

	// float slope;

	// Oct 17, 2015 11:02:19 AM
	public float calculateSigmoidalOutput(float value) {
		if (value > 100) {
			return 1.0f;
		} else if (value < -100) {
			return 0.0f;
		}
		// float den = (float) (1 + Math.exp(-this.slope * value));
		float denominator = (float) (1 + Math.exp(-value));
		float forwardPassNeuronResultCache = (1 / denominator) - 0.5f;

		return forwardPassNeuronResultCache;
	}

	public float getDerivative(float output) {
		// float derivative = this.slope * this.output * (1 - this.output) +
		// 0.1f;
		float derivative = output * (1 - output) + 0.1f;
		return derivative;
	}
}
