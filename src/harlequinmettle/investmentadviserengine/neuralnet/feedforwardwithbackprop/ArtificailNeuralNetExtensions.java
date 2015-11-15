// Nov 4, 2015 10:01:33 AM
package harlequinmettle.investmentadviserengine.neuralnet.feedforwardwithbackprop;

import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetConnection;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetLayer;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSet;

import java.util.Arrays;

public class ArtificailNeuralNetExtensions extends ArtificailNeuralNet {

	// Nov 4, 2015 10:01:46 AM
	public ArtificailNeuralNetExtensions(DataSet data) {
		super(data);

	}

	// Nov 4, 2015 10:03:17 AM
	public ArtificailNeuralNetExtensions(DataSet data, int[] hiddenLayerNeuronCounts) {
		super(data, hiddenLayerNeuronCounts);

	}

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

	// Oct 19, 2015 1:11:58 PM
	protected void display(float error) {
		if (overrideOutput) {
			return;
		}
		for (int i = 0; i < dataSet.numberDataSets; i++) {
			float[] inputPattern = dataSet.trainingInputs.get(i);
			float[] targetOutput = dataSet.targets.get(i);
			float[] actualOutput = dataSet.trainingOutputs.get(i);
			String STRING_NUMBER_FORMAT = "%1$-15s|  %2$-10.2f\n ";

			System.out.println("input: " + Arrays.toString(inputPattern));
			for (float f : targetOutput)
				System.out.format(STRING_NUMBER_FORMAT, "target:                                    ", f);
			for (float f : actualOutput)
				System.out.format(STRING_NUMBER_FORMAT, "actual:                                    ", f);
		}
		System.out.println("AVG ERROR: " + error);

		System.out.println("\n----------------\n ");

	}
}
