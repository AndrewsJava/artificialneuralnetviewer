// Oct 17, 2015 11:01:18 AM
package harlequinmettle.investmentadviserengine.neuralnet;

public interface TransferFunction {
	// Oct 17, 2015 11:01:18 AM
	public float calculateOutput(float value);

	public float getDerivative();
}
