// Oct 16, 2015 9:44:43 AM
package harlequinmettle.investmentadviserengine.neuralnet;

import java.io.Serializable;
import java.util.ArrayList;

public class ArtificialNeuralNetLayer implements Serializable {

	private static final long serialVersionUID = -5932295326600667274L;
	// Oct 16, 2015 9:44:43 AM
	public ArrayList<ArtificialNeuron> neuronsInLayer = new ArrayList<ArtificialNeuron>();

	public ArtificialNeuralNetLayer(int initialHiddenLayerNeuronCount, boolean isInputNeuron) {
		for (int i = 0; i < initialHiddenLayerNeuronCount; i++)
			if (isInputNeuron)
				neuronsInLayer.add(new ArtificialNeuron(ArtificialNeuron.INPUT_NEURON_BUILDER_ID));
			else
				neuronsInLayer.add(new ArtificialNeuron());
	}

	public ArtificialNeuralNetLayer(int initialHiddenLayerNeuronCount) {
		// Oct 16, 2015 10:16:45 AM
		for (int i = 0; i < initialHiddenLayerNeuronCount; i++)

			neuronsInLayer.add(new ArtificialNeuron());
	}
}
