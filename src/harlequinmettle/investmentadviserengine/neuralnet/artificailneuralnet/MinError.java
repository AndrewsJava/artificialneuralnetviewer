// Oct 19, 2015 12:12:12 PM
package harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet;

import java.io.Serializable;

public class MinError implements Serializable {

	private static final long serialVersionUID = -525294527284300012L;
	// Oct 19, 2015 12:12:12 PM
	private float minError = Float.MAX_VALUE;
	public boolean wasLastCheckMinError = false;

	public void checkMinError(float f) {
		// Oct 19, 2015 12:11:17 PM
		if (f < minError) {
			wasLastCheckMinError = true;
			minError = f;
		} else {
			wasLastCheckMinError = false;
		}
	}
}
