// Oct 16, 2015 11:55:26 AM
package harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet;

import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetConnection;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetLayer;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuron;
import harlequinmettle.investmentadviserengine.neuralnet.Global;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSet;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSetXOR;
import harlequinmettle.investmentadviserengine.util.SystemTool;
import harlequinmettle.investmentadviserengine.util.TimeDateTool;

import java.io.Serializable;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class FeedForwardWithBackPropagation extends ArtificailNeuralNetExtensions implements Serializable {

	private static final long serialVersionUID = 2712275468608016163L;
	public AtomicBoolean stopRequested = new AtomicBoolean(true);

	public static void main(String[] args) {
		defaultHiddenLayerNeuronCount = 2;
		DataSet testData = null;
		testData = new DataSetXOR();
		overrideOutput = false;
		// testData = new DataSetNoisySin();
		System.out.println(testData);
		FeedForwardWithBackPropagation nn = new FeedForwardWithBackPropagation(testData);
		System.out.println(nn.toString());
		nn.startNNTrainingThread();
	}

	ConcurrentSkipListMap<Integer, float[]> currentFullTrainingSetOutputErrors = new ConcurrentSkipListMap<Integer, float[]>();

	public ConcurrentSkipListMap<String, String> nnState = new ConcurrentSkipListMap<String, String>();

	AtomicBoolean isErrorTooLargeToStop = new AtomicBoolean(true);

	private int fullDataSetTrainingIterations = 0;

	public int trainingSleepMilliseconds = 1;

	private float acceptibleAverageError = 0.00001f;

	public Thread nnTrainingThread;

	public FeedForwardWithBackPropagation(DataSet data, int... hiddenLayerNeuronCounts) {
		super(data, hiddenLayerNeuronCounts);
		establishTrainingOutputs();
		establishTestingOuputs();

	}

	public FeedForwardWithBackPropagation(DataSet data) {
		super(data);
		establishTrainingOutputs();
		establishTestingOuputs();
	}

	// Oct 28, 2015 1:58:32 PM
	public void establishTrainingOutputs() {
		for (int i = 0; i < dataSet.numberDataSets; i++) {

			float[] inputPattern = dataSet.trainingInputs.get(i);
			feedforward(inputPattern);
			dataSet.trainingOutputs.put(i, getCurrentOutputArray());
		}
	}

	public void establishTestingOuputs() {
		for (int i = 0; i < dataSet.numberTestDataSets; i++) {

			float[] inputPattern = dataSet.testingInputs.get(i);
			feedforward(inputPattern);
			dataSet.testingOutputs.put(i, getCurrentOutputArray());
		}
	}

	public void trainArtificialNeuralNet() {

		while (isErrorTooLargeToStop.get()) {

			if (stopRequested.get())
				break;

			SystemTool.takeABreak(trainingSleepMilliseconds);

			fullDataSetTrainingIterations++;
			trainOneFullIteration();
			establishTestingOuputs();
			Global.reduceLearningRate();
			Global.reduceMomentum();

			checkTotalError();

		}
		System.out.println(this.toString());
	}

	public void trainOneFullIteration() {
		// Oct 28, 2015 10:39:49 AM
		for (int i = 0; i < dataSet.numberDataSets; i++) {
			trainPattern(i);
		}
		establishTrainingOutputs();
	}

	// Oct 19, 2015 11:56:19 AM
	private void storeCurrentIterationOutputError_s_(int i) {

		// float outputLayerError = 0f;
		int j = 0;
		float[] outputErrors = new float[outputLayer.neuronsInLayer.size()];
		for (ArtificialNeuron ouput : outputLayer.neuronsInLayer)
			outputErrors[j++] = ouput.getError();
		currentFullTrainingSetOutputErrors.put(i, outputErrors);
	}

	public void startNNTrainingThread() {
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
				trainArtificialNeuralNet();
				System.out.println(TimeDateTool.timeSince(time));
			}
		});
		nnTrainingThread.start();
	}

	// Oct 19, 2015 11:56:12 AM
	public void checkTotalError() {

		float totalError = new SumSquare().calculateSumSquare(currentFullTrainingSetOutputErrors.values());
		if (totalError != totalError)
			return;
		dataSet.ssqError = totalError;
		float avgError = totalError / currentFullTrainingSetOutputErrors.size();

		dataSet.avgError = avgError;
		if (Global.learningRate < 0.01 && dataSet.avgError > 0.1)
			Global.resetGlobals();
		if (minError.isSetMinError(avgError)) {
			display(avgError);
		}
		// if (totalError < 0.1 && totalError > 0.02)
		// ArtificialNeuron.learningRate *= 0.995;
		isErrorTooLargeToStop.set(avgError > acceptibleAverageError || fullDataSetTrainingIterations < 2);
	}

	public void trainBatch(int i) {

		i = i % dataSet.numberDataSets;

		float[] inputPattern = dataSet.trainingInputs.get(i);
		// set inputs and establish outputs
		feedforward(inputPattern);
		dataSet.trainingOutputs.put(i, getCurrentOutputArray());
		storeCurrentIterationOutputError_s_(i);
	}

	// Oct 19, 2015 9:33:39 AM
	public void trainPattern(int i) {
		i = i % dataSet.numberDataSets;

		float[] inputPattern = dataSet.trainingInputs.get(i);
		float[] targetOutput = dataSet.targets.get(i);
		// set inputs and establish outputs
		feedforward(inputPattern);
		dataSet.trainingOutputs.put(i, getCurrentOutputArray());

		backProagate(targetOutput);
		storeCurrentIterationOutputError_s_(i);
		applyWeightChanges();
		commitWeightChanges();

	}

	// Oct 19, 2015 1:23:56 PM
	public float[] getCurrentOutputArray() {

		float[] currentOutput = new float[outputLayer.neuronsInLayer.size()];
		int i = 0;
		for (ArtificialNeuron neuron : outputLayer.neuronsInLayer)
			currentOutput[i++] = neuron.getEstablishedOutputValue();
		return currentOutput;
	}

	// Oct 18, 2015 11:49:32 AM
	public void backProagate(float[] targets) {

		backpropagateOutoputLayer(targets);
		backpropagateHidden();

	}

	// ErrorB = OutputB (1-OutputB)(TargetB – OutputB)
	// ErrorA = OutputA (1 - OutputA)(ErrorB WAB + ErrorC WAC)
	// Oct 18, 2015 1:49:01 PM
	private void backpropagateHidden() {
		// ErrorA = OutputA (1 - OutputA)(ErrorB WAB + ErrorC WAC)

		for (int i = hiddenLayers.size() - 1; i >= 0; i--) {
			ArtificialNeuralNetLayer hiddenLayer = hiddenLayers.get(i);

			for (ArtificialNeuron neuron : hiddenLayer.neuronsInLayer) {

				neuron.establishHiddenNeuronError();

				neuron.establishWeightChangesByErrorBackpropagation();
			}
		}
	}

	// Oct 18, 2015 1:48:20 PM
	private void backpropagateOutoputLayer(float[] targets) {

		int i = 0;
		for (ArtificialNeuron neuron : outputLayer.neuronsInLayer) {
			neuron.establishOutputNeuronError(targets[i]);
			neuron.establishWeightChangesByErrorBackpropagation();
			i++;
		}
	}

	public void randomizeAllWeights(float magnitude) {
		isErrorTooLargeToStop.set(true);
		for (ArtificialNeuron neuron : outputLayer.neuronsInLayer) {
			for (ArtificialNeuralNetConnection connection : neuron.inputConnections) {
				connection.weight.randomizeRelativeToExisting(magnitude);
			}
		}

		for (ArtificialNeuralNetLayer layer : hiddenLayers) {
			for (ArtificialNeuron neuron : layer.neuronsInLayer) {
				for (ArtificialNeuralNetConnection connection : neuron.inputConnections) {
					connection.weight.randomizeRelativeToExisting(magnitude);
				}
			}
		}
	}

	// Oct 18, 2015 1:49:53 PM
	private void applyWeightChanges() {

		for (ArtificialNeuron neuron : outputLayer.neuronsInLayer) {
			for (ArtificialNeuralNetConnection connection : neuron.inputConnections) {
				connection.weight.applyWeightChange();
			}
		}
		for (ArtificialNeuralNetLayer layer : hiddenLayers) {
			for (ArtificialNeuron neuron : layer.neuronsInLayer) {
				for (ArtificialNeuralNetConnection connection : neuron.inputConnections) {
					connection.weight.applyWeightChange();
				}
			}
		}
	}

	private void commitWeightChanges() {

		for (ArtificialNeuron neuron : outputLayer.neuronsInLayer) {
			for (ArtificialNeuralNetConnection connection : neuron.inputConnections) {

				connection.weight.commitWeightChange();
			}
		}
		for (ArtificialNeuralNetLayer layer : hiddenLayers) {
			for (ArtificialNeuron neuron : layer.neuronsInLayer) {
				for (ArtificialNeuralNetConnection connection : neuron.inputConnections) {

					connection.weight.commitWeightChange();
				}
			}
		}
	}

	private void revertWeightChanges() {

		for (ArtificialNeuron neuron : outputLayer.neuronsInLayer) {
			for (ArtificialNeuralNetConnection connection : neuron.inputConnections) {

				connection.weight.revertWeightChange();
			}
		}
		for (ArtificialNeuralNetLayer layer : hiddenLayers) {
			for (ArtificialNeuron neuron : layer.neuronsInLayer) {
				for (ArtificialNeuralNetConnection connection : neuron.inputConnections) {

					connection.weight.revertWeightChange();
				}
			}
		}
	}

}
