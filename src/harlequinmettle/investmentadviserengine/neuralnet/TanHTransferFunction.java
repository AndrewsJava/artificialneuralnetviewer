// Oct 25, 2015 1:33:53 PM
package harlequinmettle.investmentadviserengine.neuralnet;

public class TanHTransferFunction {
	float tanh = Float.NaN;

	public float calculateSigmoidalOutput(float sum) {
		// Oct 25, 2015 1:34:17 PM
		tanh = (float) Math.tanh(sum);
		return tanh;
	}

	// Oct 25, 2015 1:33:53 PM

	public float getDerivative(float sum) {
		// Oct 25, 2015 1:34:23 PM
		float derivativeTanh = 1 - tanh * tanh;
		tanh = Float.NaN;
		return derivativeTanh;
	}
}
