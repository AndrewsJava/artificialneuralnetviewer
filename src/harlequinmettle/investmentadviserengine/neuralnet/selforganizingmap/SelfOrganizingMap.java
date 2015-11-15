// Nov 15, 2015 10:49:39 AM
package harlequinmettle.investmentadviserengine.neuralnet.selforganizingmap;

import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetConnection;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetLayer;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuron;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSet;
import harlequinmettle.investmentadviserengine.neuralnet.data.SimpleSelfOrganizingMapData;
import harlequinmettle.investmentadviserengine.util.SystemTool;
import harlequinmettle.investmentadviserengine.util.TimeDateTool;

import java.util.concurrent.atomic.AtomicBoolean;

public class SelfOrganizingMap {

	AtomicBoolean stopRequested = new AtomicBoolean(false);
	public Thread nnTrainingThread;

	DataSet dataToMap;
	public static final boolean MAKE_INPUT_LAYER = true;

	ArtificialNeuralNetLayer inputLayer;
	ArtificialNeuralNetLayer outputLayer;
	private long trainingSleepMilliseconds = 10;

	AtomicBoolean isErrorTooLargeToStop = new AtomicBoolean(true);

	public SelfOrganizingMap(DataSet dataToMap, int neuronMappingCount) {
		// Nov 15, 2015 11:03:54 AM
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
		while (isErrorTooLargeToStop.get()) {

			if (stopRequested.get())
				break;

			SystemTool.takeABreak(trainingSleepMilliseconds);
			for (float[] input : dataToMap.trainingInputs) {
				establishInput(input);
				establishOutputs();
				prioritizeEuclideanDistances();
				adjustWeights();
			}
		}
	}

	private void startSelfOrganizationThread() {

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
				// Oct 17, 2015 10:34:49 AM
				long time = System.currentTimeMillis();
				selfOrganize();
				System.out.println(TimeDateTool.timeSince(time));
			}

		});
		nnTrainingThread.start();

	}

	public static void main(String[] args) {

		DataSet dataToMap = new SimpleSelfOrganizingMapData();
		SelfOrganizingMap map = new SelfOrganizingMap(dataToMap, 10);
		map.startSelfOrganizationThread();
	}
}
