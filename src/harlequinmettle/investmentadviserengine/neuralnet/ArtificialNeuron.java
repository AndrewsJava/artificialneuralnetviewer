// Oct 16, 2015 9:45:32 AM
package harlequinmettle.investmentadviserengine.neuralnet;

import harlequinmettle.investmentadviserengine.neuralnet.transferfunction.TanHTransferFunction;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

// Oct 16, 2015 9:45:32 AM
public class ArtificialNeuron implements Serializable {
	String artificialNeuralNetComponentLabel = "ArtificialNeuron_";
	static AtomicInteger neuronCounter = new AtomicInteger(100000);
	private static final long serialVersionUID = -5609553171004488643L;
	volatile public static float learningRate = 0.1F;
	public static final int INPUT_NEURON_BUILDER_ID = 1005002;
	public static final int BIAS_NEURON_BUILDER_ID = 2002008;
	public boolean isBiasNeuron = false;
	public boolean isInputNeuron = false;
	// includes BIAS CONNECTION
	public CopyOnWriteArrayList<ArtificialNeuralNetConnection> inputConnections = new CopyOnWriteArrayList<ArtificialNeuralNetConnection>();
	public CopyOnWriteArrayList<ArtificialNeuralNetConnection> outputConnections = new CopyOnWriteArrayList<ArtificialNeuralNetConnection>();
	// public ArrayList<ArtificialNeuralNetConnection> biasConnections = new
	// ArrayList<ArtificialNeuralNetConnection>();
	private ArtificialNeuralNetWeight biasNeuronWeight;
	// private SigmoidTransferFunction sigmoidTransferFunction = new
	// SigmoidTransferFunction();
	private TanHTransferFunction neuronTransferFunction = new TanHTransferFunction();
	// private TanHTransferFunction neuronTransferFunction = new
	// TanHTransferFunctionTable();

	private float input = Float.NaN;
	private float output = Float.NaN;
	private float error = Float.NaN;
	private float derivative = Float.NaN;

	private static final float one = 1f;

	@Override
	public String toString() {

		return "  {(" + inputConnections.size() + ") " + artificialNeuralNetComponentLabel + " (" + outputConnections.size() + ")} ";
	}

	public float getError() {
		if (error != error)
			return Float.POSITIVE_INFINITY;
		return error;
	}

	// public void setError(float error) {
	// this.error = error;
	// }

	public ArtificialNeuron() {
		artificialNeuralNetComponentLabel += neuronCounter.addAndGet(1);
		learningRate = 0.1F;
	}

	// Oct 17, 2015 12:48:16 PM
	public ArtificialNeuron(int neuronBuildType) {
		learningRate = 0.1F;

		if (neuronBuildType == BIAS_NEURON_BUILDER_ID)
			buildNeuronAsBias();
		if (neuronBuildType == INPUT_NEURON_BUILDER_ID)
			buildNeuronAsInput();
		artificialNeuralNetComponentLabel += neuronCounter.addAndGet(1);

	}

	private void buildNeuronAsInput() {
		// Oct 25, 2015 11:45:47 AM
		isInputNeuron = true;
		artificialNeuralNetComponentLabel += "input_";
	}

	private void buildNeuronAsBias() {
		// Oct 18, 2015 9:57:24 AM
		output = one;
		isBiasNeuron = true;
		this.biasNeuronWeight = new ArtificialNeuralNetWeight();

		artificialNeuralNetComponentLabel += "bias_";
	}

	// Oct 17, 2015 9:57:09 AM
	public float getEstablishedOutputValue() {
		return output;
	}

	// Oct 17, 2015 10:54:18 AM
	public float establishNeuronOutputFromConnections() {

		if (isInputNeuron) {
			output = input;// move to initialization
			return output;
		}

		if (isBiasNeuron) {
			derivative = 1;// FIND OUT WHAT DERIVATIVE IS FOR BIAS NEURON
			return one * biasNeuronWeight.weight;
		}

		float sum = 0;

		for (ArtificialNeuralNetConnection connection : inputConnections) {
			sum += connection.getWeightedInput();
		}

		output = neuronTransferFunction.calculateOutput(sum);
		derivative = neuronTransferFunction.getDerivative(sum);

		return output;
	}

	// Oct 17, 2015 10:54:18 AM
	public void setInput(float input) {
		this.input = input;

	}

	// Oct 18, 2015 12:20:05 PM
	public void establishOutputNeuronError(float target) {
		error = derivative * (target - output);
		// error = derivative * (output - target);
	}

	// Oct 18, 2015 12:26:29 PM
	public void establishWeightChangesByErrorBackpropagation() {
		for (ArtificialNeuralNetConnection connection : inputConnections) {
			float incommingSignal = connection.fromNeuron.getEstablishedOutputValue();
			float weightChange = incommingSignal * error * learningRate;
			connection.weight.setWeightChange(weightChange);
		}
	}

	// ErrorB = OutputB (1-OutputB)(TargetB – OutputB)
	// ErrorA = hiddenOut (1 - hiddenOut)(ERRORoutB Weight_HiddenConnectOUTB +
	// ERRORoutC W_hiddenConnectOUTC)
	// Oct 18, 2015 2:12:13 PM
	public void establishHiddenNeuronError() {
		float differenceExtrapolation = 0f;
		for (ArtificialNeuralNetConnection connection : outputConnections) {
			differenceExtrapolation += connection.toNeuron.error * connection.weight.weight;
		}
		error = derivative * differenceExtrapolation;
	}

}
