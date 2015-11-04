// Oct 16, 2015 9:48:09 AM
package harlequinmettle.investmentadviserengine.neuralnet;

import java.io.Serializable;

public class ArtificialNeuralNetConnection extends ArtificialNeuralNetConnectionModel implements Serializable {

	// Oct 25, 2015 12:03:44 PM
	@Override
	public String toString() {
		return "     connection " + fromNeuron + " ------> " + "(" + weight.weight + ") " + "------->" + toNeuron;
	}

	private static final long serialVersionUID = 9183327806552530385L;

	public ArtificialNeuralNetConnection(ArtificialNeuron fromNeuron, ArtificialNeuron toNeuron) {
		super(fromNeuron, toNeuron);
	}

	public ArtificialNeuralNetConnection(ArtificialNeuron fromNeuron, ArtificialNeuron toNeuron, float wt) {
		super(fromNeuron, toNeuron, wt);
	}

	public ArtificialNeuralNetConnection(ArtificialNeuron fromNeuron, ArtificialNeuron toNeuron, int incommingLayerSize) {
		super(fromNeuron, toNeuron, incommingLayerSize);
	}

	public float getWeightedInput() {
		return this.fromNeuron.getEstablishedOutputValue() * weight.weight;
	}

}
