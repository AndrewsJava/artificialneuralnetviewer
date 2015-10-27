// Oct 27, 2015 11:15:15 AM
package harlequinmettle.investmentadviserengine.neuralnet.transferfunction;

import java.util.concurrent.ConcurrentSkipListMap;

public class TanHTransferFunctionTable extends TanHTransferFunction {
	// Oct 27, 2015 11:15:15 AM

	private static final ConcurrentSkipListMap<Float, Float> tanhFunctionTable = new ConcurrentSkipListMap<Float, Float>();
	private static final ConcurrentSkipListMap<Float, Float> tanhDerivativeFunctionTable = new ConcurrentSkipListMap<Float, Float>();

	// Oct 25, 2015 8:57:58 AM
	public TanHTransferFunctionTable() {
		float min = -100;
		float max = 100;
		float tableSize = 5000;
		float increment = (max - min) / tableSize;
		for (float input = min; input < max; input += increment) {
			tanhFunctionTable.put(input, calculateOutput(input));
			tanhDerivativeFunctionTable.put(input, super.getDerivative(input));
		}
	}

	public float getOutput(float input) {
		if (input > 100) {
			return 1.0f;
		} else if (input < -100) {
			return 0.0f;
		}
		Float over = tanhFunctionTable.ceilingKey(input);
		Float under = tanhFunctionTable.floorKey(input);
		if (over == null && under == null)
			return Float.NaN;
		if (over == null)
			return under;
		if (under == null)
			return over;
		return (under + over) / 2;
	}

	public float getDerivative(float input) {

		Float over = tanhDerivativeFunctionTable.ceilingKey(input);
		Float under = tanhDerivativeFunctionTable.floorKey(input);
		if (over == null && under == null)
			return Float.NaN;
		if (over == null)
			return under;
		if (under == null)
			return over;
		return (under + over) / 2;
	}

}
