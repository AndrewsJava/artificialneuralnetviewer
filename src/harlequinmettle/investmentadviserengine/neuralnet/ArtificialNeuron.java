// Oct 16, 2015 9:45:32 AM
package harlequinmettle.investmentadviserengine.neuralnet;

import java.io.Serializable;
import java.util.ArrayList;

// Oct 16, 2015 9:45:32 AM
public class ArtificialNeuron implements Serializable {

	private static final long serialVersionUID = -5609553171004488643L;
	public static float learningRate = 0.1F;
	public static final int INPUT_NEURON_BUILDER_ID = 1005002;
	public static final int BIAS_NEURON_BUILDER_ID = 2002008;
	public boolean isBiasNeuron = false;
	public ArrayList<ArtificialNeuralNetConnection> inputConnections = new ArrayList<ArtificialNeuralNetConnection>();
	public ArrayList<ArtificialNeuralNetConnection> outputConnections = new ArrayList<ArtificialNeuralNetConnection>();
	public ArrayList<ArtificialNeuralNetConnection> biasConnections = new ArrayList<ArtificialNeuralNetConnection>();
	private ArtificialNeuralNetWeight inputWeight;
	private SigmoidTransferFunction sigmoidTransferFunction = new SigmoidTransferFunction();

	private float input = Float.NaN;
	private float output = Float.NaN;
	private float error = Float.NaN;

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
			this.inputWeight = new ArtificialNeuralNetWeight();
	}

	private void buildNeuronAsBias() {
		// Oct 18, 2015 9:57:24 AM
		output = 1;
		isBiasNeuron = true;
	}

	// Oct 17, 2015 9:57:09 AM
	public float getEstablishedOutputValue() {
		return output;
	}

	// Oct 17, 2015 10:54:18 AM
	public float establishNeuronOutputFromConnections() {
		if (isInputNeuron()) {
			output = input;// sigmoidTransferFunction.calculateSigmoidalOutput(input
							// * inputWeight.weight);
			return output;
		}
		if (isBiasNeuron)
			return output * inputWeight.weight;
		float sum = 0;
		for (ArtificialNeuralNetConnection connection : inputConnections) {
			sum += connection.getWeightedInput();
		}
		output = sigmoidTransferFunction.calculateSigmoidalOutput(sum);
		return output;
	}

	// Oct 17, 2015 12:53:29 PM
	private boolean isInputNeuron() {
		return inputWeight != null;
	}

	// Oct 17, 2015 10:54:18 AM
	public void setInput(float input) {
		this.input = input;

	}

	// Oct 18, 2015 12:20:05 PM
	public void establishOutputNeuronError(float target) {
		// (target - output) * output * (1 - output)
		error = sigmoidTransferFunction.getDerivative() * (target - output);
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
		float deriv = sigmoidTransferFunction.getDerivative();
		float differenceExtrapolation = 0f;
		for (ArtificialNeuralNetConnection connection : outputConnections) {
			differenceExtrapolation += connection.toNeuron.error * connection.weight.weight;
		}
		error = deriv * differenceExtrapolation;
	}

	// Oct 18, 2015 2:15:45 PM
	public void establishWeightChangesForHiddenLayerConnections() {

	}
}
