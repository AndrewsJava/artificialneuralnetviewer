// Oct 19, 2015 11:58:53 AM
package harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet;

import java.io.Serializable;
import java.util.Collection;

public class SumSquare implements Serializable {

	private static final long serialVersionUID = 2512147865632939704L;

	public float calculateSumSquare(Collection<float[]> values) {
		// System.out.println("calculating sum square......\n");
		// Oct 19, 2015 11:59:32 AM
		float sumsq = 0;
		for (float[] fa : values)
			for (float f : fa) {
				sumsq += f * f;
				// System.out.println("f=" + f + "    f*f=" + (f * f) +
				// "     sum=" + sumsq);
			}
		return sumsq / 2;
	}
	// Oct 19, 2015 11:58:53 AM

}
