// Nov 5, 2015 8:42:04 AM
package harlequinmettle.investmentadviserengine.neuralnet;

import java.util.Random;

public class Global {
	// Nov 5, 2015 8:42:04 AM
	public static final Random random = new Random();
	private static final float ILR = 0.26F;
	private static final float ILRrf = 0.05F;
	volatile public static float learningRate = 0.25F;
	private static float inputLearningRateReductionFactor = 0.5F;
	private static float learningRateIncreaseFactor = 1.12F;
	private static float learningRateReductionFactor = (float) Math.tanh(inputLearningRateReductionFactor);
	private static float learningRateReductionReductionFactor = 1.0000005F;

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
		inputLearningRateReductionFactor = ILRrf;
		learningRateReductionFactor = (float) Math.tanh(inputLearningRateReductionFactor);
	}

	public static void reduceLearningRate() {
		learningRate *= learningRateReductionFactor;
		inputLearningRateReductionFactor *= learningRateReductionReductionFactor;
		learningRateReductionFactor = (float) Math.tanh(inputLearningRateReductionFactor);
		System.out.println("learningRate: " + learningRate);
		System.out.println("inputLearningRateReductionFactor: " + inputLearningRateReductionFactor);
		System.out.println("learningRateReductionFactor: " + learningRateReductionFactor);
		System.out.println();
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
