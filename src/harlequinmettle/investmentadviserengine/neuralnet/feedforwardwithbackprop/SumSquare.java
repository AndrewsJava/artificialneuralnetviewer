// Oct 19, 2015 11:58:53 AM
package harlequinmettle.investmentadviserengine.neuralnet.feedforwardwithbackprop;

import java.io.Serializable;
import java.util.Collection;

public class SumSquare implements Serializable {

	private static final long serialVersionUID = 2512147865632939704L;

	public float calculateSumSquare(Collection<float[]> errorValues) {
		float sumsq = 0;
		for (float[] outputErrors : errorValues)
			for (float f : outputErrors) {
				sumsq += f * f;
			}
		return sumsq / 2;
	}

}
