// Oct 16, 2015 11:55:26 AM
package harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet;

import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetConnection;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetLayer;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuron;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSet;
import harlequinmettle.investmentadviserengine.util.SystemTool;
import harlequinmettle.investmentadviserengine.util.TimeDateTool;

import java.io.Serializable;
import java.util.Arrays;
import java.util.TreeMap;

public class FeedForwardWithBackPropagation extends ArtificailNeuralNet implements Serializable {

	private static final long serialVersionUID = 2712275468608016163L;

	public static void main(String[] args) {
		defaultHiddenLayerNeuronCount = 14;
		DataSet testData = null;
		// testData = new DataSetXOR();
		testData = new DataSetNoisySin();
		System.out.println(testData);
		FeedForwardWithBackPropagation nn = new FeedForwardWithBackPropagation(testData);
		nn.trainNN();
	}

	TreeMap<Integer, Float> epochError = new TreeMap<Integer, Float>();

	boolean errorIsTooLargeToStop = true;

	private int epochCounter = 0;

	public int trainingSpeedDamper = 1;

	private boolean overrideOutput = false;

	public FeedForwardWithBackPropagation(DataSet data, int... hiddenLayerNeuronCounts) {
		super(data, hiddenLayerNeuronCounts);
	}

	public FeedForwardWithBackPropagation(DataSet data) {
		super(data);
	}

	// Oct 16, 2015 11:57:00 AM
	public void trainNN() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// Oct 17, 2015 10:34:49 AM
				long time = System.currentTimeMillis();
				trainArtificialNeuralNet();
				System.out.println(TimeDateTool.timeSince(time));
			}
		}).start();
	}

	public void trainArtificialNeuralNet() {
		while (checkError()) {
			epochCounter++;
			for (int i = 0; i < dataSet.numberDataSets; i++) {
				trainPattern(i);
			}
			checkTotalError();

		}
	}

	private void storeEpochError(int i) {
		// Oct 19, 2015 11:56:19 AM

		float outputLayerError = 0f;
		for (ArtificialNeuron ouput : outputLayer.neuronsInLayer)
			outputLayerError += ouput.getError();
		epochError.put(i, outputLayerError);
	}

	public void checkTotalError() {
		// Oct 19, 2015 11:56:12 AM
		System.out.println("checking sum sq: " + epochError.values());
		float totalError = new SumSquare().calculateSumSquare(epochError.values());
		dataSet.ssqError = totalError;
		minError.checkMinError(totalError);
		if (minError.wasLastCheckMinError) {
			display(totalError);
		}
		errorIsTooLargeToStop = totalError > 0.0000001f || epochCounter < 2;
	}

	private void display(float totalError) {
		// Oct 19, 2015 1:11:58 PM
		if (overrideOutput)
			return;
		for (int i = 0; i < dataSet.numberDataSets; i++) {
			float[] inputPattern = dataSet.inputs.get(i);
			float[] targetOutput = dataSet.targets.get(i);
			float[] actualOutput = dataSet.outputs.get(i);
			System.out.println("input: " + Arrays.toString(inputPattern));
			System.out.println("target: " + Arrays.toString(targetOutput));
			System.out.println("actual: " + Arrays.toString(actualOutput));
		}
		System.out.println("TOTAL ERROR: " + totalError);
		System.out.println("errors: " + epochError);
		System.out.println(epochCounter + "\n----------------\n ");

	}

	// Oct 19, 2015 9:33:39 AM
	private void trainPattern(int i) {
		float[] inputPattern = dataSet.inputs.get(i);
		float[] targetOutput = dataSet.targets.get(i);

		feedforward(inputPattern);

		storeEpochError(i);
		dataSet.outputs.put(i, getCurrentOutputArray());
		trainNetwork(targetOutput);

	}

	private float[] getCurrentOutputArray() {
		// Oct 19, 2015 1:23:56 PM
		float[] currentOutput = new float[outputLayer.neuronsInLayer.size()];
		int i = 0;
		for (ArtificialNeuron neuron : outputLayer.neuronsInLayer)
			currentOutput[i++] = neuron.getEstablishedOutputValue();
		return currentOutput;
	}

	// Oct 18, 2015 11:49:32 AM
	public void trainNetwork(float[] targets) {
		backpropagateOutoputLayer(targets);
		backpropagateHidden();
		applyWeightChanges();

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

	private boolean checkError() {
		// Oct 17, 2015 10:47:08 AM
		SystemTool.takeABreak(trainingSpeedDamper);
		return errorIsTooLargeToStop;
	}

}
