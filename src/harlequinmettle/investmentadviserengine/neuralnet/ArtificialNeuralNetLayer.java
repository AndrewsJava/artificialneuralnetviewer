// Oct 16, 2015 9:44:43 AM
package harlequinmettle.investmentadviserengine.neuralnet;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArtificialNeuralNetLayer implements Serializable {

	@Override
	public String toString() {
		String layer = " ArtificialNeuralNetLayer " + System.lineSeparator();
		for (ArtificialNeuron n : neuronsInLayer) {
			layer += "       " + n.artificialNeuralNetComponentLabel + System.lineSeparator();
			for (ArtificialNeuralNetConnection c : n.outputConnections) {
				layer += "           " + c.toString() + System.lineSeparator();
			}
		}
		return layer;
	}

	private static final long serialVersionUID = -5932295326600667274L;
	// Oct 16, 2015 9:44:43 AM
	public CopyOnWriteArrayList<ArtificialNeuron> neuronsInLayer = new CopyOnWriteArrayList<ArtificialNeuron>();

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
