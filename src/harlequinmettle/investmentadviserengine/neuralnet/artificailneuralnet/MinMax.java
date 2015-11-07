// Nov 5, 2015 12:19:02 PM
package harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet;

import java.io.Serializable;

public class MinMax implements Serializable {

	private static final long serialVersionUID = -8421985547852849976L;
	public float min = Float.POSITIVE_INFINITY;
	public float max = Float.NEGATIVE_INFINITY;

	@Override
	public String toString() {
		return " [" + min + "," + max + "] ";
	}

	public void isSetMinMax(float f) {
		if (f < min) {
			min = f;
		}
		if (f > max) {
			max = f;
		}
	}

}
