// Oct 16, 2015 9:48:09 AM
package harlequinmettle.investmentadviserengine.neuralnet;

import java.io.Serializable;

public class ArtificialNeuralNetConnection extends ArtificialNeuralNetConnectionModel implements Serializable {

	private static final long serialVersionUID = 9183327806552530385L;

	public ArtificialNeuralNetConnection(ArtificialNeuron fromNeuron, ArtificialNeuron toNeuron) {
		super(fromNeuron, toNeuron);
	}

	public ArtificialNeuralNetConnection(ArtificialNeuron fromNeuron, ArtificialNeuron toNeuron, float wt) {
		super(fromNeuron, toNeuron, wt);
	}

	public ArtificialNeuralNetConnection(ArtificialNeuron fromNeuron, ArtificialNeuron toNeuron, ArtificialNeuralNetWeight weight) {
		super(fromNeuron, toNeuron, weight);
	}

	public float getWeightedInput() {
		return this.fromNeuron.getEstablishedOutputValue() * weight.weight;
	}

}
