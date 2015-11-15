// Oct 17, 2015 11:00:58 AM
package harlequinmettle.investmentadviserengine.neuralnet.transferfunction;

import harlequinmettle.investmentadviserengine.neuralnet.util.TransferFunction;

import java.io.Serializable;

public class SigmoidTransferFunction implements Serializable, TransferFunction {

	private static final long serialVersionUID = 310723662430627233L;

	// Oct 17, 2015 11:02:19 AM
	public float calculateOutput(float value) {

		float result = (float) (1 / (1 + Math.exp(-value)));
		return result;
	}

	public float getDerivative(float x) {

		float derivative = x * (1 - x) + 0.1f;
		return derivative;
	}
}
