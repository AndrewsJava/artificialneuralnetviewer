// Oct 25, 2015 8:56:43 AM
package harlequinmettle.investmentadviserengine.neuralnet.transferfunction;

import java.util.concurrent.ConcurrentSkipListMap;

public class SigmoidTrasferFunctionTable extends SigmoidTransferFunction {

	private static final ConcurrentSkipListMap<Float, Float> sigmoidFunctionTable = new ConcurrentSkipListMap<Float, Float>();
	private static final ConcurrentSkipListMap<Float, Float> sigmoidDerivativeFunctionTable = new ConcurrentSkipListMap<Float, Float>();

	// Oct 25, 2015 8:57:58 AM
	public SigmoidTrasferFunctionTable() {
		float min = -100;
		float max = 100;
		float tableSize = 5000;
		float increment = (max - min) / tableSize;
		for (float input = min; input < max; input += increment) {
			sigmoidFunctionTable.put(input, calculateOutput(input));
			sigmoidDerivativeFunctionTable.put(input, super.getDerivative(input));
		}
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
