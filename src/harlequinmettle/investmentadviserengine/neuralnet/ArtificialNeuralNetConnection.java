// Oct 16, 2015 9:48:09 AM
package harlequinmettle.investmentadviserengine.neuralnet;

import harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet.ArtificailNeuralNet;
import harlequinmettle.utils.reflection.RuntimeDetails;

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
		if (ArtificailNeuralNet.debugObjectConstructionWithReflection)
			RuntimeDetails.getPrintClassInfo(this);
	}

	public ArtificialNeuralNetConnection(ArtificialNeuron fromNeuron, ArtificialNeuron toNeuron, float wt) {
		super(fromNeuron, toNeuron, wt);
	}

	public ArtificialNeuralNetConnection(ArtificialNeuron fromNeuron, ArtificialNeuron toNeuron, ArtificialNeuralNetWeight weight) {
		super(fromNeuron, toNeuron, weight);
	}

	public float getWeightedInput() {
		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
		return this.fromNeuron.getEstablishedOutputValue() * weight.weight;
	}

}
