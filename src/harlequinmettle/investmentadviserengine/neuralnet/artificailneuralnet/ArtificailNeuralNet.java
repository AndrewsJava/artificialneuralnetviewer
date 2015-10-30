// Oct 15, 2015 10:39:33 AM
package harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet;

import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetConnection;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetLayer;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuron;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSet;

import java.io.Serializable;

public class ArtificailNeuralNet extends ArtificailNeuralNetFramework implements Serializable {

	private static final long serialVersionUID = -490681777238384428L;
	// public static boolean debugMethodsWithReflection = false;
	// public static boolean debugObjectConstructionWithReflection = false;
	protected static boolean overrideOutput = false;
	MinError minError = new MinError();

	@Override
	public String toString() {

		String net = "-----------------------------ARTIFICIAL NEURAL NET ------------------------------" + System.lineSeparator();
		net += "                                                           ArtificailNeuralNet :" + System.lineSeparator();
		net += "[Input Layer] : " + System.lineSeparator();
		net += inputLayer.toString() + System.lineSeparator();
		int counter = 1;
		for (ArtificialNeuralNetLayer hidden : hiddenLayers) {
			net += "[Hidden Layer] : " + counter++ + System.lineSeparator();
			net += hidden.toString() + System.lineSeparator();

		}
		net += "[Output Layer] : " + System.lineSeparator();
		net += outputLayer.toString() + System.lineSeparator();
		net += biasNeuron.toString() + System.lineSeparator();
		for (ArtificialNeuralNetConnection connection : biasNeuron.outputConnections)
			net += "     " + connection.toString() + System.lineSeparator();
		net += "---------------------------------------------------------------------------------" + System.lineSeparator();
		return net;
	}

	// Oct 17, 2015 10:24:51 AM
	public ArtificailNeuralNet(DataSet data) {
		super(data);
	}

	public ArtificailNeuralNet(DataSet data, int... hiddenLayerNeuronCounts) {
		super(data, hiddenLayerNeuronCounts);
	}

	// Oct 19, 2015 9:57:50 AM
	public void feedforward(float[] inputPattern) {

		setInputs(inputPattern);
		establishOutput();

	}

	private void setInputs(float[] input) {
		if (input.length != inputLayer.neuronsInLayer.size()) {
			System.out.println("Input vector size does not match network input dimension!");
			return;
		}

		int i = 0;
		for (ArtificialNeuron neuron : inputLayer.neuronsInLayer) {
			neuron.setInput(input[i]);
			i++;
		}
	}

	// Oct 17, 2015 11:57:49 AM
	public float[] establishOutput() {

		float[] output = new float[outputLayer.neuronsInLayer.size()];
		for (ArtificialNeuron neuron : inputLayer.neuronsInLayer) {
			neuron.establishNeuronOutputFromConnections();
		}
		for (ArtificialNeuralNetLayer hiddenLayer : hiddenLayers) {
			for (ArtificialNeuron neuron : hiddenLayer.neuronsInLayer) {
				neuron.establishNeuronOutputFromConnections();
			}
		}
		int i = 0;
		for (ArtificialNeuron neuron : outputLayer.neuronsInLayer) {
			output[i] = neuron.establishNeuronOutputFromConnections();
			i++;
		}
		return output;
	}

}
