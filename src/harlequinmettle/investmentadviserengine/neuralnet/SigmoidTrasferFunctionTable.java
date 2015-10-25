// Oct 25, 2015 8:56:43 AM
package harlequinmettle.investmentadviserengine.neuralnet;

import java.util.concurrent.ConcurrentSkipListMap;

public class SigmoidTrasferFunctionTable extends SigmoidTransferFunction {

	ConcurrentSkipListMap<Float, Float> sigmoidFunctionTable = new ConcurrentSkipListMap<Float, Float>();
	ConcurrentSkipListMap<Float, Float> sigmoidDerivativeFunctionTable = new ConcurrentSkipListMap<Float, Float>();

	// Oct 25, 2015 8:57:58 AM
	public SigmoidTrasferFunctionTable() {
		float min = -100;
		float max = 100;
		float tableSize = 5000;
		float increment = (max - min) / tableSize;
		for (float input = min; input < max; input += increment) {
			sigmoidFunctionTable.put(input, calculateSigmoidalOutput(input));
			sigmoidDerivativeFunctionTable.put(input, getDerivative(input));
		}
	}

	public float getOutput(float input) {
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
