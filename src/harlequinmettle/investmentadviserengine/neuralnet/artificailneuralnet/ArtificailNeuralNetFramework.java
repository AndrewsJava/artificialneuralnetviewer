// Oct 17, 2015 10:19:37 AM
package harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet;

import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetConnection;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetLayer;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuron;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSet;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArtificailNeuralNetFramework implements Serializable {
	// Oct 17, 2015 10:19:37 AM

	private static final long serialVersionUID = 1224431842177441869L;
	ArtificialNeuron biasNeuron;
	ArtificialNeuralNetLayer inputLayer;
	ArtificialNeuralNetLayer outputLayer;
	CopyOnWriteArrayList<ArtificialNeuralNetLayer> hiddenLayers = new CopyOnWriteArrayList<ArtificialNeuralNetLayer>();
	public static final boolean MAKE_INPUT_LAYER = true;
	protected static int defaultHiddenLayerNeuronCount = 4;
	protected DataSet dataSet;

	// protected TrainingAlgorithm trainingMechanism;

	// Oct 16, 2015 11:07:13 AM
	public ArtificailNeuralNetFramework(DataSet dataSet) {
		this(dataSet, defaultHiddenLayerNeuronCount);
	}

	public ArtificailNeuralNetFramework(DataSet dataSet, int... hiddenLayerNeuronCounts) {

		this.dataSet = dataSet;

		if (dataSet != null && dataSet.numberDataSets > 0)
			createInputOutputLayers();

		if (hiddenLayerNeuronCounts != null && hiddenLayerNeuronCounts.length > 0)
			buildHiddenLayers(hiddenLayerNeuronCounts);

		buildAllNeuralNetConnections();

		biasNeuron = new ArtificialNeuron(ArtificialNeuron.BIAS_NEURON_BUILDER_ID);

		buildBiasNeuronConnectionsToAllNeurons();

	}

	// Oct 18, 2015 10:05:24 AM
	private void buildBiasNeuronConnectionsToAllNeurons() {
		for (ArtificialNeuralNetLayer layer : hiddenLayers)
			connectBiasToNeuronsInLayer(layer);
		connectBiasToNeuronsInLayer(outputLayer);
	}

	private void connectBiasToNeuronsInLayer(ArtificialNeuralNetLayer layer) {
		// Oct 18, 2015 10:17:59 AM

		for (ArtificialNeuron toNeuron : layer.neuronsInLayer) {

			ArtificialNeuralNetConnection connection = new ArtificialNeuralNetConnection(biasNeuron, toNeuron);

			biasNeuron.outputConnections.add(connection);
			toNeuron.inputConnections.add(connection);
		}
	}

	// Oct 16, 2015 11:10:28 AM
	private void createInputOutputLayers() {
		int inputLayerSize = dataSet.trainingInputs.get(0).length;
		int outputLayerSize = dataSet.targets.get(0).length;
		inputLayer = new ArtificialNeuralNetLayer(inputLayerSize, MAKE_INPUT_LAYER);
		outputLayer = new ArtificialNeuralNetLayer(outputLayerSize);
	}

	// Oct 16, 2015 11:35:33 AM
	private void buildAllNeuralNetConnections() {
		buildConnectionsBetweenLayers(inputLayer, hiddenLayers.get(0));
		for (int i = 0; i < hiddenLayers.size(); i++) {
			ArtificialNeuralNetLayer layer1 = hiddenLayers.get(i);

			ArtificialNeuralNetLayer layer2;
			// size of 1 >0+1=false
			// 2>0+1=true
			// 2>1+1 = false
			if (hiddenLayers.size() > i + 1)
				layer2 = hiddenLayers.get(i + 1);
			else
				layer2 = outputLayer;
			buildConnectionsBetweenLayers(layer1, layer2);

		}
	}

	private void buildConnectionsBetweenLayers(ArtificialNeuralNetLayer layer1, ArtificialNeuralNetLayer layer2) {
		// Oct 16, 2015 11:38:22 AM
		for (ArtificialNeuron fromNeuron : layer1.neuronsInLayer) {
			for (ArtificialNeuron toNeuron : layer2.neuronsInLayer) {

				ArtificialNeuralNetConnection connection = new ArtificialNeuralNetConnection(fromNeuron, toNeuron);

				toNeuron.inputConnections.add(connection);
				fromNeuron.outputConnections.add(connection);
			}

		}
	}

	private void buildHiddenLayers(int[] hiddenLayerNeuronCounts) {
		// Oct 16, 2015 11:19:42 AM
		for (int layerCount : hiddenLayerNeuronCounts)
			if (layerCount <= 0)
				continue;
			else
				addHiddenLayer(layerCount);
	}

	// Oct 16, 2015 11:23:43 AM
	private void addHiddenLayer(int layerNeuronCount) {

		ArtificialNeuralNetLayer hiddenLayer = new ArtificialNeuralNetLayer(layerNeuronCount);
		hiddenLayers.add(hiddenLayer);
	}
}
