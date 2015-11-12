// Oct 30, 2015 9:09:14 AM
package harlequinmettle.investmentadviserengine.neuralnet.transferfunction;

import harlequinmettle.investmentadviserengine.neuralnet.TransferFunction;

import java.io.Serializable;

public class ArctanTransferFunction implements Serializable, TransferFunction {

	private static final long serialVersionUID = 2810048426071297534L;
	// Oct 30, 2015 9:09:14 AM
	private float lastInput = Float.NaN;
	private float atanCache = Float.NaN;

	public float calculateOutput(float sum) {
		lastInput = sum;
		atanCache = (float) Math.atan(sum);
		return atanCache;
	}

	public float getDerivative(float sum) {
		if (sum != lastInput)
			atanCache = (float) Math.atan(sum);
		float derivativeAtan = 1 / (1 + sum * sum);
		if (derivativeAtan == 0)
			derivativeAtan += 0.05f;
		atanCache = Float.NaN;
		return derivativeAtan;
	}

}
