// Oct 25, 2015 1:33:53 PM
package harlequinmettle.investmentadviserengine.neuralnet.transferfunction;

import harlequinmettle.investmentadviserengine.neuralnet.TransferFunction;

import java.io.Serializable;

public class TanHTransferFunction implements Serializable, TransferFunction {

	private static final long serialVersionUID = -468693869937826047L;
	private float lastInput = Float.NaN;
	private float tanhCache = Float.NaN;

	@Override
	public float calculateOutput(float sum) {

		lastInput = sum;
		tanhCache = (float) Math.tanh(sum);
		return tanhCache;
	}

	@Override
	public float getDerivative(float sum) {
		if (sum != lastInput)
			tanhCache = (float) Math.tanh(sum);
		float derivativeTanh = 1 - tanhCache * tanhCache;
		if (derivativeTanh == 0)
			derivativeTanh += 0.05f;
		tanhCache = Float.NaN;
		return derivativeTanh;
	}
}
