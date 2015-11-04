// Oct 25, 2015 8:56:43 AM
package harlequinmettle.investmentadviserengine.neuralnet.transferfunction;

import java.io.Serializable;
import java.util.concurrent.ConcurrentSkipListMap;

public class SigmoidTrasferFunctionTable implements Serializable {

	private static final long serialVersionUID = -6752691464343565057L;
	private static final ConcurrentSkipListMap<Float, Float> sigmoidFunctionTable = new ConcurrentSkipListMap<Float, Float>();
	private static final ConcurrentSkipListMap<Float, Float> sigmoidDerivativeFunctionTable = new ConcurrentSkipListMap<Float, Float>();
	static {
		SigmoidTransferFunction fn = new SigmoidTransferFunction();
		float min = -100;
		float max = 100;
		float tableSize = 5000;
		float increment = (max - min) / tableSize;
		for (float input = min; input < max; input += increment) {
			sigmoidFunctionTable.put(input, fn.calculateOutput(input));
			sigmoidDerivativeFunctionTable.put(input, fn.getDerivative(input));
		}
	}

	// Oct 25, 2015 8:57:58 AM
	public SigmoidTrasferFunctionTable() {
	}

	public float getOutput(float input) {
		if (input > 100) {
			return 1.0f;
		} else if (input < -100) {
			return 0.0f;
		}
		Float over = sigmoidFunctionTable.ceilingKey(input);
		Float under = sigmoidFunctionTable.floorKey(input);
		if (over == null && under == null)
			return Float.NaN;
		if (over == null)
			return under;
		if (under == null)
			return over;
		return (under + over) / 2;
	}

	public float getDerivative(float input) {

		Float over = sigmoidDerivativeFunctionTable.ceilingKey(input);
		Float under = sigmoidDerivativeFunctionTable.floorKey(input);
		if (over == null && under == null)
			return Float.NaN;
		if (over == null)
			return under;
		if (under == null)
			return over;
		return (under + over) / 2;
	}

}
