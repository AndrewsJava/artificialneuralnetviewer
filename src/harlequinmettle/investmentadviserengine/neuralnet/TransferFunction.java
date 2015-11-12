// Nov 12, 2015 11:25:55 AM
package harlequinmettle.investmentadviserengine.neuralnet;

public interface TransferFunction {

	public float calculateOutput(float sum);

	public float getDerivative(float sum);
}
