// Oct 21, 2015 10:56:51 AM
package harlequinmettle.investmentadviserengine.neuralnet.util.gui;

import harlequinmettle.investmentadviserengine.util.SystemTool;
import harlequinmettle.utils.guitools.DataGrapher;
import harlequinmettle.utils.guitools.JFrameFactory;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import neuralnet.data.DataSet;
import neuralnet.data.DataSetNoisyInputsNoisyTargetsSin;
import neuralnet.feedforwardwithbackprop.FeedForwardWithBackPropagation;
import neuralnet.util.Global;

public class NeuralNetViewer {
	// / TODO: ADAPTIVE NEURONS AND LAYERS
	// / TODO: NOT FULLY CONNECTED IE MERGED SMALL NN
	// / TODO: variable connections
	// / TODO: RECURSIVE INPUT AS OUTPUT ESP TIME SERIES
	// / TODO: batch vs online learning
	// / TODO: SAVE/restore NN
	// / TODO: purge connections; purge neurons
	// / TODO: spawn neurons
	// / TODO:
	// / TODO:
	private String appTitle = "Neural Net Training";

	int defaultHiddenLayerNeuronCount = 4;
	DataSet nnData;
	FeedForwardWithBackPropagation nn;
	DataGrapher dataDisplayer;
	TreeSet<String> lineOptions = new TreeSet<String>();

	String testingPointsTitle = "testing";
	String targetTitle = "target";
	String outputTitle = "output";
	String errorLine = "error";
	AtomicBoolean displayThreadStopRequested = new AtomicBoolean(false);

	public NeuralNetViewer() {

		resetNN();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				showGui();
			}
		});
		startGuiThread();
	}

	// Oct 21, 2015 12:07:07 PM
	private void startGuiThread() {
		if (displayThreadStopRequested.get())
			SystemTool.takeABreak(500);
		displayThreadStopRequested.set(false);
		new Thread(new Runnable() {
			public void run() {
				countinuousNeuralNetDataUpdater();
			}
		}).start();
	}

	private void countinuousNeuralNetDataUpdater() {

		lineOptions.add(testingPointsTitle);
		lineOptions.add(targetTitle);
		lineOptions.add(outputTitle);
		ArrayList<Float> inputs = getInputPointsAsArray();
		ArrayList<Float> testingInputs = getTestingPointsInputAsArray();
		ArrayList<Float> targets = getTargetPointsAsArray();
		while (dataDisplayer == null)
			SystemTool.takeABreak(100);
		dataDisplayer.colors.clear();
		dataDisplayer.addData(targetTitle, inputs, targets);
		dataDisplayer.addData(outputTitle, inputs, getOutputPointsAsArray());
		dataDisplayer.addData(testingPointsTitle, testingInputs, getTestingDataOutputPointsAsArray());
		dataDisplayer.addDisplayText("mtm: ", "" + String.format("%1$-10.2f", Global.momentum));
		float lastError = Float.NEGATIVE_INFINITY;
		while (true) {
			if (displayThreadStopRequested.get())
				break;
			SystemTool.takeABreak(300);
			dataDisplayer.addData(outputTitle, inputs, getOutputPointsAsArray());
			dataDisplayer.addData(testingPointsTitle, testingInputs, getTestingDataOutputPointsAsArray());
			if (nnData.avgError == nnData.avgError)
				if (nnData.avgError != lastError) {
					dataDisplayer.addDisplayText("error: ", "" + String.format("%1$-10.2f", nnData.avgError));

				}
			dataDisplayer.addDisplayText("learn rt: ", "" + String.format("%1$-10.2f", Global.learningRate));
			dataDisplayer.addDisplayText("mtm: ", "" + String.format("%1$-10.2f", Global.momentum));
			lastError = nnData.avgError;
			dataDisplayer.repaint();
		}
	}

	private ArrayList<Float> getTestingPointsInputAsArray() {
		// TODO: USE ALL INPUT POINTS
		ArrayList<Float> inputs = new ArrayList<Float>();

		for (float[] data : nnData.testingInputs)
			inputs.add(data[0]);

		return inputs;
	}

	private ArrayList<Float> getInputPointsAsArray() {
		// TODO: USE ALL INPUT POINTS
		ArrayList<Float> inputs = new ArrayList<Float>();

		for (float[] data : nnData.trainingInputs)
			inputs.add(data[0]);

		return inputs;
	}

	// Oct 22, 2015 8:33:21 AM
	private ArrayList<Float> getOutputPointsAsArray() {
		// TODO: USE ALL OUTPUTS
		ArrayList<Float> outputs = new ArrayList<Float>();

		for (float[] data : nnData.trainingOutputs.values())
			outputs.add(data[0]);

		return outputs;
	}

	private ArrayList<Float> getTestingDataOutputPointsAsArray() {
		// TODO: USE ALL OUTPUTS
		ArrayList<Float> outputs = new ArrayList<Float>();

		for (float[] data : nnData.testingOutputs.values())
			outputs.add(data[0]);

		return outputs;
	}

	// Oct 22, 2015 8:33:15 AM
	private ArrayList<Float> getTargetPointsAsArray() {

		ArrayList<Float> targets = new ArrayList<Float>();

		for (float[] data : nnData.targets)
			targets.add(data[0]);

		return targets;
	}

	// Oct 21, 2015 11:48:20 AM
	private void showGui() {

		JFrame fullScreen = JFrameFactory.displayFullScreenPrimaryApplicationJFrame(appTitle);

		JPanel annRunner = new JPanel(new BorderLayout());
		fullScreen.add(annRunner);

		NNBuilderInterfacePanel nnBuilder = new NNBuilderInterfacePanel(this);
		annRunner.add(nnBuilder.vpanel, BorderLayout.NORTH);

		AnnRunnerControlsPanel runnerControls = new AnnRunnerControlsPanel(this);
		annRunner.add(runnerControls, BorderLayout.WEST);

		dataDisplayer = new DataGrapher();
		annRunner.add(dataDisplayer, BorderLayout.CENTER);

		fullScreen.pack();
	}

	public static void main(String[] args) {
		// Oct 21, 2015 10:56:51 AM
		NeuralNetViewer view = new NeuralNetViewer();
	}

	// Oct 21, 2015 10:56:51 AM

	public void resetNN(DataSet dataSet, FeedForwardWithBackPropagation nn) {
		displayThreadStopRequested.set(true);
		Global.resetGlobals();
		this.nnData = dataSet;
		this.nn = nn;
		startGuiThread();

	}

	public void resetNN() {
		DataSetNoisyInputsNoisyTargetsSin dataSet = new DataSetNoisyInputsNoisyTargetsSin(-10, 10, 60);
		FeedForwardWithBackPropagation nn = new FeedForwardWithBackPropagation(dataSet, 8, 4);
		nn.isProgramRunningInAppEngineEnvironment = false;
		resetNN(dataSet, nn);
	}
}
