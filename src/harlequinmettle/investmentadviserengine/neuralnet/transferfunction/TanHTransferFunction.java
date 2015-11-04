// Oct 25, 2015 1:33:53 PM
package harlequinmettle.investmentadviserengine.neuralnet.transferfunction;

import java.io.Serializable;

public class TanHTransferFunction implements Serializable {

	private static final long serialVersionUID = -468693869937826047L;
	private float lastInput = Float.NaN;
	private float tanhCache = Float.NaN;

	public float calculateOutput(float sum) {
		// Oct 25, 2015 1:34:17 PM
		lastInput = sum;
		tanhCache = (float) Math.tanh(sum);
		return tanhCache;
	}

	// Oct 25, 2015 1:33:53 PM

	public float getDerivative(float sum) {
		if (sum != lastInput)
			tanhCache = (float) Math.tanh(sum);
		// Oct 25, 2015 1:34:23 PM
		float derivativeTanh = 1 - tanhCache * tanhCache;
		if (derivativeTanh == 0)
			derivativeTanh += 0.05f;
		tanhCache = Float.NaN;
		return derivativeTanh;
	}
}
