// Nov 5, 2015 8:42:04 AM
package harlequinmettle.investmentadviserengine.neuralnet;

import java.util.Random;

public class Global {
	// Nov 5, 2015 8:42:04 AM
	public static final Random random = new Random();
	private static final float ILR = 0.251F;
	volatile public static float learningRate = 0.1F;
	private static float learningRateReductionFactor = 0.9995F;
	private static float learningRateIncreaseFactor = 1.02F;

	private static final float IM = 0.9F;
	volatile public static float momentum = 0.9F;
	private static float momentumReductionFactor = 0.9999F;
	private static float momentumIncreaseFactor = 1.02F;

	public static float random() {
		return random.nextFloat();
	}

	public static void resetGlobals() {
		learningRate = ILR;
		momentum = IM;
	}

	public static void reduceLearningRate() {
		learningRate *= learningRateReductionFactor;
	}

	public static void boostLearningRate() {
		learningRate *= learningRateIncreaseFactor;
	}

	public static void reduceMomentum() {
		momentum *= momentumReductionFactor;
	}

	public static void boostMomentum() {
		momentum *= momentumIncreaseFactor;
	}

}
