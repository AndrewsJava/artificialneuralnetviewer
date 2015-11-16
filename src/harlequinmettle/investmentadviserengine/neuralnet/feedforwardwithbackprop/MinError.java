// Oct 19, 2015 12:12:12 PM
package harlequinmettle.investmentadviserengine.neuralnet.feedforwardwithbackprop;

import java.io.Serializable;

public class MinError implements Serializable {

	private static final long serialVersionUID = -525294527284300012L;
	// Oct 19, 2015 12:12:12 PM
	private float minError = Float.POSITIVE_INFINITY;

	public float getMinError() {
		return minError;
	}

	// Oct 19, 2015 12:11:17 PM
	public boolean isSetMinError(float f) {
		if (f < minError) {
			minError = f;
			return true;
		}
		return false;
	}
}
