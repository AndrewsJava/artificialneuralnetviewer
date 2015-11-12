// Oct 27, 2015 11:15:15 AM
package harlequinmettle.investmentadviserengine.neuralnet.transferfunction;

import java.io.Serializable;
import java.util.concurrent.ConcurrentSkipListMap;

public class TanHTransferFunctionTable implements Serializable {
	// Oct 27, 2015 11:15:15 AM

	private static final long serialVersionUID = -3245545101423112337L;
	private static final ConcurrentSkipListMap<Float, Float> tanhFunctionTable = new ConcurrentSkipListMap<Float, Float>();
	private static final ConcurrentSkipListMap<Float, Float> tanhDerivativeFunctionTable = new ConcurrentSkipListMap<Float, Float>();

	static {
		TanHTransferFunction fn = new TanHTransferFunction();
		float min = -100;
		float max = 100;
		float tableSize = 500000;
		float increment = (max - min) / tableSize;
		for (float input = min; input < max; input += increment) {
			tanhFunctionTable.put(input, fn.calculateOutput(input));
			tanhDerivativeFunctionTable.put(input, fn.getDerivative(input));
		}
	}

	// Oct 25, 2015 8:57:58 AM
	public TanHTransferFunctionTable() {

	}

	public float calculateOutput(float input) {
		if (input > 100) {
			return 1.0f;
		} else if (input < -100) {
			return -1.0f;
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
		if (under == null)
			return over;
		if (over == null)
			return under;
		return (under + over) / 2;
	}

}
