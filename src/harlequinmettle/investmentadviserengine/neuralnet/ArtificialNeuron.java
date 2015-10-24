// Oct 16, 2015 9:45:32 AM
package harlequinmettle.investmentadviserengine.neuralnet;

import harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet.ArtificailNeuralNet;
import harlequinmettle.utils.reflection.RuntimeDetails;

import java.io.Serializable;
import java.util.ArrayList;

// Oct 16, 2015 9:45:32 AM
public class ArtificialNeuron implements Serializable {

	private static final long serialVersionUID = -5609553171004488643L;
	public static float learningRate = 0.1F;
	public static final int INPUT_NEURON_BUILDER_ID = 1005002;
	public static final int BIAS_NEURON_BUILDER_ID = 2002008;
	public boolean isBiasNeuron = false;
	public boolean isInputNeuron = false;
	// includes BIAS CONNECTION
	public ArrayList<ArtificialNeuralNetConnection> inputConnections = new ArrayList<ArtificialNeuralNetConnection>();
	public ArrayList<ArtificialNeuralNetConnection> outputConnections = new ArrayList<ArtificialNeuralNetConnection>();
	// public ArrayList<ArtificialNeuralNetConnection> biasConnections = new
	// ArrayList<ArtificialNeuralNetConnection>();
	private ArtificialNeuralNetWeight biasNeuronWeight;
	private SigmoidTransferFunction sigmoidTransferFunction = new SigmoidTransferFunction();

	private float input = Float.NaN;
	private float output = Float.NaN;
	private float error = Float.NaN;
	private float derivative = Float.NaN;

	private static final float one = 1f;

	public float getError() {
		if (error != error)
			return Float.POSITIVE_INFINITY;
		return error;
	}

	public void setError(float error) {
		this.error = error;
	}

	public ArtificialNeuron() {
	}

	// Oct 17, 2015 12:48:16 PM
	public ArtificialNeuron(int neuronBuildType) {
		if (neuronBuildType == BIAS_NEURON_BUILDER_ID)
			buildNeuronAsBias();
		if (neuronBuildType == INPUT_NEURON_BUILDER_ID)
			isInputNeuron = true;
		if (ArtificailNeuralNet.debugObjectConstructionWithReflection)
			RuntimeDetails.getPrintClassInfo(this);
	}

	private void buildNeuronAsBias() {
		// Oct 18, 2015 9:57:24 AM
		output = one;
		isBiasNeuron = true;
		this.biasNeuronWeight = new ArtificialNeuralNetWeight();
	}

	// Oct 17, 2015 9:57:09 AM
	public float getEstablishedOutputValue() {
		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
		return output;
	}

	// Oct 17, 2015 10:54:18 AM
	public float establishNeuronOutputFromConnections() {

		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
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

		output = sigmoidTransferFunction.calculateSigmoidalOutput(sum);
		derivative = sigmoidTransferFunction.getDerivative(output);
		return output;
	}

	// Oct 17, 2015 10:54:18 AM
	public void setInput(float input) {
		this.input = input;

	}

	// Oct 18, 2015 12:20:05 PM
	public void establishOutputNeuronError(float target) {
		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
		error = derivative * (target - output);
		error = derivative * (output - target);
	}

	// Oct 18, 2015 12:26:29 PM
	public void establishWeightChangesByErrorBackpropagation() {
		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
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
		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
		float differenceExtrapolation = 0f;
		for (ArtificialNeuralNetConnection connection : outputConnections) {
			differenceExtrapolation += connection.toNeuron.error * connection.weight.weight;
		}
		error = derivative * differenceExtrapolation;
	}

	// Oct 18, 2015 2:15:45 PM
	public void establishWeightChangesForHiddenLayerConnections() {

	}
}
