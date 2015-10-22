// Oct 21, 2015 10:56:51 AM
package harlequinmettle.investmentadviserengine.neuralnet.gui;

import harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet.DataSetNoisySin;
import harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet.FeedForwardWithBackPropagation;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSet;
import harlequinmettle.investmentadviserengine.util.SystemTool;
import harlequinmettle.utils.guitools.DataGrapher;
import harlequinmettle.utils.guitools.JFrameFactory;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class NeuralNetViewer {
	private String appTitle = "Neural Net Training";

	int defaultHiddenLayerNeuronCount = 8;
	// DataSettestData = new DataSetXOR();
	DataSet testData = new DataSetNoisySin();
	FeedForwardWithBackPropagation nn = new FeedForwardWithBackPropagation(testData, defaultHiddenLayerNeuronCount);
	DataGrapher dataDisplayer;

	public NeuralNetViewer() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				showGui();
			}
		});
		startGuiThread();
		nn.trainNN();
	}

	private void startGuiThread() {
		// Oct 21, 2015 12:07:07 PM

		new Thread(new Runnable() {
			public void run() {
				countinuousNeuralNetDataUpdater();
			}
		}).start();
	}

	private void countinuousNeuralNetDataUpdater() {
		// String inputTitle = "inputs";
		String targetTitle = "target";
		ArrayList<Float> inputs = getInputPointsAsArray();
		ArrayList<Float> targets = getTargetPointsAsArray();
		if (dataDisplayer == null)
			SystemTool.takeABreak(2500);
		dataDisplayer.addData(targetTitle, inputs, targets);
		while (true) {
			SystemTool.takeABreak(300);
			String outputTitle = "output";
			ArrayList<Float> output = getOutputPointsAsArray();
			if (dataDisplayer == null)
				SystemTool.takeABreak(500);
			dataDisplayer.addData(outputTitle, inputs, output);

			dataDisplayer.repaint();
		}
	}

	private ArrayList<Float> getInputPointsAsArray() {

		ArrayList<Float> inputs = new ArrayList<Float>();
		for (float[] data : testData.inputs)
			inputs.add(data[0]);
		return inputs;
	}

	// Oct 22, 2015 8:33:21 AM
	private ArrayList<Float> getOutputPointsAsArray() {
		ArrayList<Float> outputs = new ArrayList<Float>();

		for (float[] data : testData.outputs.values())
			outputs.add(data[0]);
		return outputs;
	}

	// Oct 22, 2015 8:33:15 AM
	private ArrayList<Float> getTargetPointsAsArray() {
		ArrayList<Float> targets = new ArrayList<Float>();

		for (float[] data : testData.targets)
			targets.add(data[0]);
		return targets;
	}

	// Oct 21, 2015 11:48:20 AM
	private void showGui() {
		// JFrame fullScreen = JFrameFactory.displayFullScreenJFrame(appTitle);
		JFrame fullScreen = JFrameFactory.displayFullScreenPrimaryApplicationJFrame(appTitle);
		dataDisplayer = new DataGrapher();
		fullScreen.add(dataDisplayer);
	}

	public static void main(String[] args) {
		// Oct 21, 2015 10:56:51 AM
		NeuralNetViewer view = new NeuralNetViewer();
	}
	// Oct 21, 2015 10:56:51 AM
}
