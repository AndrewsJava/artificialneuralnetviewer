// Oct 16, 2015 11:55:26 AM
package harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet;

import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetConnection;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetLayer;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuron;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSet;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSetXOR;
import harlequinmettle.investmentadviserengine.util.SystemTool;
import harlequinmettle.investmentadviserengine.util.TimeDateTool;
import harlequinmettle.utils.reflection.RuntimeDetails;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class FeedForwardWithBackPropagation extends ArtificailNeuralNet implements Serializable {

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

	AtomicBoolean errorIsTooLargeToStop = new AtomicBoolean(true);

	private int fullDataSetTrainingIterations = 0;

	public int trainingSleepMilliseconds = 1;

	public float learningDamper = 1;

	private float acceptibleAverageError = 0.00001f;

	public Thread nnTrainingThread;

	public FeedForwardWithBackPropagation(DataSet data, int... hiddenLayerNeuronCounts) {
		super(data, hiddenLayerNeuronCounts);
		establishInitialOutputs();
		if (ArtificailNeuralNet.debugObjectConstructionWithReflection)
			RuntimeDetails.getPrintClassInfo(this);
	}

	public FeedForwardWithBackPropagation(DataSet data) {
		super(data);
		establishInitialOutputs();
		if (ArtificailNeuralNet.debugObjectConstructionWithReflection)
			RuntimeDetails.getPrintClassInfo(this);
	}

	// Oct 28, 2015 1:58:32 PM
	private void establishInitialOutputs() {
		for (int i = 0; i < dataSet.numberDataSets; i++) {

			float[] inputPattern = dataSet.inputs.get(i);
			feedforward(inputPattern);
			dataSet.outputs.put(i, getCurrentOutputArray());
		}
	}

	public void trainArtificialNeuralNet() {

		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();

		while (errorIsTooLargeToStop.get()) {

			if (stopRequested.get())
				break;

			SystemTool.takeABreak(trainingSleepMilliseconds);

			fullDataSetTrainingIterations++;
			trainOneFullIteration();
			ArtificialNeuron.learningRate *= learningDamper;
			checkTotalError();

		}
		System.out.println(this.toString());
	}

	public void trainOneFullIteration() {
		// Oct 28, 2015 10:39:49 AM
		for (int i = 0; i < dataSet.numberDataSets; i++) {
			trainPattern(i);
		}
	}

	// Oct 19, 2015 11:56:19 AM
	private void storeCurrentIterationOutputError_s_(int i) {

		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
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
			e.printStackTrace();
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

		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
		float totalError = new SumSquare().calculateSumSquare(currentFullTrainingSetOutputErrors.values());
		if (totalError != totalError)
			return;
		dataSet.ssqError = totalError;
		float avgError = totalError / currentFullTrainingSetOutputErrors.size();
		minError.checkMinError(avgError);
		if (minError.wasLastCheckMinError) {
			display(avgError);
		}
		// if (totalError < 0.1 && totalError > 0.02)
		// ArtificialNeuron.learningRate *= 0.995;
		errorIsTooLargeToStop.set(avgError > acceptibleAverageError || fullDataSetTrainingIterations < 2);
	}

	private void display(float error) {
		// Oct 19, 2015 1:11:58 PM
		if (overrideOutput) {
			System.out.println("AVG ERROR: " + error);
			System.out.println(fullDataSetTrainingIterations + "\n----------------\n ");
			return;
		}
		for (int i = 0; i < dataSet.numberDataSets; i++) {
			float[] inputPattern = dataSet.inputs.get(i);
			float[] targetOutput = dataSet.targets.get(i);
			float[] actualOutput = dataSet.outputs.get(i);
			String STRING_NUMBER_FORMAT = "%1$-15s|  %2$-10.2f\n ";
			// for (float f : inputPattern)
			// System.out.format(STRING_NUMBER_FORMAT, "input: ", f);
			System.out.println("input: " + Arrays.toString(inputPattern));
			for (float f : targetOutput)
				System.out.format(STRING_NUMBER_FORMAT, "target:                                    ", f);
			for (float f : actualOutput)
				System.out.format(STRING_NUMBER_FORMAT, "actual:                                    ", f);
		}
		System.out.println("AVG ERROR: " + error);
		System.out.println("errors: ");
		for (float[] errors : currentFullTrainingSetOutputErrors.values())
			System.out.print(" : " + Arrays.toString(errors));
		System.out.println();
		System.out.println(fullDataSetTrainingIterations + "\n----------------\n ");

	}

	// Oct 19, 2015 9:33:39 AM
	public void trainPattern(int i) {
		i = i % dataSet.numberDataSets;
		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
		float[] inputPattern = dataSet.inputs.get(i);
		float[] targetOutput = dataSet.targets.get(i);
		// set inputs and establish outputs
		feedforward(inputPattern);
		dataSet.outputs.put(i, getCurrentOutputArray());
		backProagate(targetOutput);
		storeCurrentIterationOutputError_s_(i);
		applyWeightChanges();
		setCurrentNNState();
	}

	private void setCurrentNNState() {
		// Oct 28, 2015 11:22:30 AM

		float avgError = dataSet.ssqError / currentFullTrainingSetOutputErrors.size();
		nnState.put("avgError", "" + avgError);
	}

	// Oct 19, 2015 1:23:56 PM
	public float[] getCurrentOutputArray() {

		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();

		float[] currentOutput = new float[outputLayer.neuronsInLayer.size()];
		int i = 0;
		for (ArtificialNeuron neuron : outputLayer.neuronsInLayer)
			currentOutput[i++] = neuron.getEstablishedOutputValue();
		return currentOutput;
	}

	// Oct 18, 2015 11:49:32 AM
	public void backProagate(float[] targets) {
		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();

		backpropagateOutoputLayer(targets);
		backpropagateHidden();

	}

	// ErrorB = OutputB (1-OutputB)(TargetB – OutputB)
	// ErrorA = OutputA (1 - OutputA)(ErrorB WAB + ErrorC WAC)
	// Oct 18, 2015 1:49:01 PM
	private void backpropagateHidden() {
		// ErrorA = OutputA (1 - OutputA)(ErrorB WAB + ErrorC WAC)

		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
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
		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
		int i = 0;
		for (ArtificialNeuron neuron : outputLayer.neuronsInLayer) {
			neuron.establishOutputNeuronError(targets[i]);
			neuron.establishWeightChangesByErrorBackpropagation();
			i++;
		}
	}

	public void randomizeAllWeights() {
		errorIsTooLargeToStop.set(true);
		for (ArtificialNeuron neuron : outputLayer.neuronsInLayer) {
			for (ArtificialNeuralNetConnection connection : neuron.inputConnections) {
				connection.weight.randomize();
			}
		}

		for (ArtificialNeuralNetLayer layer : hiddenLayers) {
			for (ArtificialNeuron neuron : layer.neuronsInLayer) {
				for (ArtificialNeuralNetConnection connection : neuron.inputConnections) {
					connection.weight.randomize();
				}
			}
		}
	}

	// Oct 18, 2015 1:49:53 PM
	private void applyWeightChanges() {
		if (ArtificailNeuralNet.debugMethodsWithReflection)
			RuntimeDetails.getPrintMethodInfo();
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

	public ConcurrentSkipListMap<String, String> getState() {
		// Oct 28, 2015 11:15:58 AM
		return nnState;
	}

}
