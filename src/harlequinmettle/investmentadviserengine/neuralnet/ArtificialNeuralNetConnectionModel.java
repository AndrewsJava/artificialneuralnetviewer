// Oct 17, 2015 9:38:04 AM
package harlequinmettle.investmentadviserengine.neuralnet;

import java.io.Serializable;

public class ArtificialNeuralNetConnectionModel implements Serializable {

	private static final long serialVersionUID = -7417911492853808175L;

	// Oct 17, 2015 9:38:04 AM
	public ArtificialNeuralNetConnectionModel(ArtificialNeuron fromNeuron, ArtificialNeuron toNeuron) {
		this.fromNeuron = fromNeuron;
		this.toNeuron = toNeuron;
		this.weight = new ArtificialNeuralNetWeight();
	}

	public ArtificialNeuralNetConnectionModel(ArtificialNeuron fromNeuron, ArtificialNeuron toNeuron, float wt) {
		this.fromNeuron = fromNeuron;
		this.toNeuron = toNeuron;
		this.weight = new ArtificialNeuralNetWeight(wt);
	}

	public ArtificialNeuralNetConnectionModel(ArtificialNeuron fromNeuron, ArtificialNeuron toNeuron, ArtificialNeuralNetWeight weight) {
		this.fromNeuron = fromNeuron;
		this.toNeuron = toNeuron;
		this.weight = weight;
	}

	public ArtificialNeuralNetWeight weight;
	public ArtificialNeuron fromNeuron;
	public ArtificialNeuron toNeuron;
}
