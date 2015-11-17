// Nov 15, 2015 10:49:39 AM
package harlequinmettle.investmentadviserengine.neuralnet.selforganizingmap;

import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetConnection;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetLayer;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuron;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSet;
import harlequinmettle.investmentadviserengine.neuralnet.data.SimpleSelfOrganizingMapData;
import harlequinmettle.investmentadviserengine.neuralnet.feedforwardwithbackprop.MinError;
import harlequinmettle.investmentadviserengine.neuralnet.util.Global;
import harlequinmettle.investmentadviserengine.util.SystemTool;
import harlequinmettle.investmentadviserengine.util.TimeDateTool;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SelfOrganizingMap {

	AtomicBoolean stopRequested = new AtomicBoolean(false);
	public Thread nnTrainingThread;

	DataSet dataToMap;
	public static final boolean MAKE_INPUT_LAYER = true;

	ArtificialNeuralNetLayer inputLayer;
	public ArtificialNeuralNetLayer outputLayer;
	private long trainingSleepMilliseconds = 1000;
	int iterations = 0;

	AtomicBoolean isErrorTooLargeToStop = new AtomicBoolean(true);
	MinError minError = new MinError();

	ConcurrentSkipListMap<Integer, Float> avgError = new ConcurrentSkipListMap<Integer, Float>();
	ConcurrentSkipListMap<Double, ArtificialNeuron> distanceMap = new ConcurrentSkipListMap<Double, ArtificialNeuron>();
	public float learningRate = 0.96f;
	float learningRateDecayFactor = 0.999f;

	public SelfOrganizingMap(DataSet dataToMap, int neuronMappingCount) {
		// Nov 15, 2015 11:03:54 AM
		Global.momentum = 0;
		this.dataToMap = dataToMap;
		if (dataToMap != null && dataToMap.numberDataSets > 0)
			createInputLayer();
		if (neuronMappingCount > 0)
			outputLayer = new ArtificialNeuralNetLayer(neuronMappingCount);
		connectLayers();
	}

	private void connectLayers() {
		if (inputLayer == null)
			return;
		if (outputLayer == null)
			return;
		for (ArtificialNeuron fromNeuron : inputLayer.neuronsInLayer) {
			for (ArtificialNeuron toNeuron : outputLayer.neuronsInLayer) {

				ArtificialNeuralNetConnection connection = new ArtificialNeuralNetConnection(fromNeuron, toNeuron, inputLayer.neuronsInLayer.size());

				toNeuron.inputConnections.add(connection);
				fromNeuron.outputConnections.add(connection);
			}

		}
	}

	private void createInputLayer() {
		int inputLayerSize = dataToMap.trainingInputs.get(0).length;
		inputLayer = new ArtificialNeuralNetLayer(inputLayerSize, MAKE_INPUT_LAYER);
	}

	private void selfOrganize() {
		displayUpdate();
		while (isErrorTooLargeToStop.get()) {

			if (stopRequested.get())
				break;
			iterations++;
			// if (iterations > 9)
			// break;
			SystemTool.takeABreak(trainingSleepMilliseconds);
			for (float[] input : dataToMap.trainingInputs) {
				establishInput(input);
				establishOutput();// pass through input
				prioritizeEuclideanDistances();
				adjustWeights();
			}
			checkAvgError();
		}
	}

	// Nov 16, 2015 8:41:34 AM
	private void checkAvgError() {
		float sum = 0;
		for (Entry<Integer, Float> ent : avgError.entrySet()) {
			sum += ent.getValue();
		}
		float avg = sum / avgError.size();
		if (minError.isSetMinError(avg))
			displayUpdate();
	}

	// Nov 16, 2015 8:48:27 AM
	private void displayUpdate() {
		System.out.println("-------------------------------");
		System.out.println("avg error: " + minError.getMinError());
		System.out.println("iteration: " + iterations);
		for (ArtificialNeuron classificationNeuron : outputLayer.neuronsInLayer) {
			String weights = "";
			for (ArtificialNeuralNetConnection connection : classificationNeuron.inputConnections) {
				weights += " [" + connection.weight.weight + "] ";
			}
			System.out.println("weights:  " + weights);
		}
		System.out.println("iteration: " + iterations);
	}

	// Nov 16, 2015 8:07:22 AM
	private void adjustWeights() {

		int farther = 0;
		for (Entry<Double, ArtificialNeuron> ent : distanceMap.entrySet()) {
			ArtificialNeuron classificationNeuron = ent.getValue();
			farther++;
			float distanceFactor = (float) Math.pow(Math.E, -(farther / 3));
			if (farther > distanceMap.size() / 2)
				break;
			for (ArtificialNeuralNetConnection connection : classificationNeuron.inputConnections) {

				float input = connection.fromNeuron.getEstablishedOutputValue();
				float weightChange = distanceFactor * learningRate * (input - connection.weight.weight);
				connection.weight.setWeightChange(weightChange);
				connection.weight.applyWeightChange();
			}

		}
		learningRate = learningRateDecayFactor * learningRate;
	}

	// Nov 16, 2015 7:58:44 AM
	private void establishOutput() {

		for (ArtificialNeuron neuron : inputLayer.neuronsInLayer) {
			neuron.establishNeuronOutputFromConnections();
		}
	}

	// Nov 16, 2015 7:48:00 AM
	private void prioritizeEuclideanDistances() {
		distanceMap.clear();
		for (ArtificialNeuron neuron : outputLayer.neuronsInLayer) {
			double euclidianDistance = 0d;
			int i = 0;
			for (ArtificialNeuralNetConnection connect : neuron.inputConnections) {
				float connectionWeight = connect.weight.weight;
				float inputComponent = connect.fromNeuron.getEstablishedOutputValue();
				euclidianDistance += Math.pow((connectionWeight - inputComponent), 2);
				avgError.put(i++, (float) euclidianDistance);

			}
			distanceMap.put(euclidianDistance + Math.random() * 0.0001 * euclidianDistance, neuron);
		}
	}

	private void establishInput(float[] input) {
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

	public void startSelfOrganizationThread() {

		stopRequested.set(true);
		try {
			nnTrainingThread.join();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		stopRequested.set(false);
		nnTrainingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				long time = System.currentTimeMillis();
				selfOrganize();
				System.out.println(TimeDateTool.timeSince(time));
			}

		});
		nnTrainingThread.start();

	}

	public static void main(String[] args) {

		DataSet dataToMap = new SimpleSelfOrganizingMapData();
		SelfOrganizingMap map = new SelfOrganizingMap(dataToMap, 20);
		map.startSelfOrganizationThread();
	}
}
