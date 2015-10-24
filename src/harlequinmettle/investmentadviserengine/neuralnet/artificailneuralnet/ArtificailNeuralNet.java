// Oct 15, 2015 10:39:33 AM
package harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet;

import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetLayer;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuron;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSet;
import harlequinmettle.utils.reflection.RuntimeDetails;

import java.io.Serializable;

public class ArtificailNeuralNet extends ArtificailNeuralNetFramework implements Serializable {

	private static final long serialVersionUID = -490681777238384428L;
	public static boolean debugMethodsWithReflection = false;
	public static boolean debugObjectConstructionWithReflection = true;
	protected boolean overrideOutput = true;
	MinError minError = new MinError();

	// Oct 17, 2015 10:24:51 AM
	public ArtificailNeuralNet(DataSet data) {
		super(data);
	}

	public ArtificailNeuralNet(DataSet data, int... hiddenLayerNeuronCounts) {
		super(data, hiddenLayerNeuronCounts);
	}

	// Oct 19, 2015 9:57:50 AM
	protected void feedforward(float[] inputPattern) {

		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
		setInputs(inputPattern);
		establishOutput();

	}

	private void setInputs(float[] input) {
		// Oct 17, 2015 10:51:43 AM
		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
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
	private float[] establishOutput() {
		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();

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
