// Oct 21, 2015 10:56:51 AM
package harlequinmettle.investmentadviserengine.neuralnet.gui;

import harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet.FeedForwardWithBackPropagation;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSet;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSetNoisyInputsNoisyTargetsSin;
import harlequinmettle.investmentadviserengine.util.SystemTool;
import harlequinmettle.utils.guitools.DataGrapher;
import harlequinmettle.utils.guitools.JFrameFactory;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class NeuralNetViewer {

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

	public NeuralNetViewer() {

		resetNN();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				showGui();
			}
		});
		// setEstaablishedOutputInDataSet();
		nn.learningDamper = 0.9999f;
		startGuiThread();
	}

	// Oct 21, 2015 12:07:07 PM
	private void startGuiThread() {

		new Thread(new Runnable() {
			public void run() {
				countinuousNeuralNetDataUpdater();
			}
		}).start();
	}

	private void countinuousNeuralNetDataUpdater() {
		lineOptions.add(errorLine);
		lineOptions.add(testingPointsTitle);
		lineOptions.add(targetTitle);
		lineOptions.add(outputTitle);
		ArrayList<Float> inputs = getInputPointsAsArray();
		ArrayList<Float> testingInputs = getTestingPointsInputAsArray();
		ArrayList<Float> targets = getTargetPointsAsArray();
		while (dataDisplayer == null)
			SystemTool.takeABreak(100);
		dataDisplayer.addData(targetTitle, inputs, targets);
		dataDisplayer.addData(outputTitle, inputs, getOutputPointsAsArray());
		dataDisplayer.addData(testingPointsTitle, testingInputs, getTestingDataOutputPointsAsArray());
		float lastError = Float.NEGATIVE_INFINITY;
		while (true) {
			SystemTool.takeABreak(300);
			dataDisplayer.addData(outputTitle, inputs, getOutputPointsAsArray());
			dataDisplayer.addData(testingPointsTitle, testingInputs, getTestingDataOutputPointsAsArray());
			if (nnData.ssqError == nnData.ssqError)
				if (nnData.ssqError != lastError)
					dataDisplayer.addErrorPoint(nnData.ssqError);
			lastError = nnData.ssqError;
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
		// JFrame fullScreen = JFrameFactory.displayFullScreenJFrame(appTitle);
		JFrame fullScreen = JFrameFactory.displayFullScreenPrimaryApplicationJFrame(appTitle);
		JTabbedPane tabs = new JTabbedPane();
		dataDisplayer = new DataGrapher();
		JPanel annRunner = new JPanel(new BorderLayout());
		annRunner.add(dataDisplayer, BorderLayout.CENTER);
		annRunner.add(generateAnnRunnerPanel(), BorderLayout.WEST);
		tabs.addTab("ANN Runner", annRunner);
		ArtificialneuralNetBuilderPanel annControlTab = new ArtificialneuralNetBuilderPanel(this);
		// tabs.setTabPlacement(JTabbedPane.RIGHT);
		tabs.addTab("ANN Builder", annControlTab);
		fullScreen.add(tabs);
		fullScreen.pack();
	}

	private JPanel generateAnnRunnerPanel() {
		// Oct 27, 2015 10:45:39 AM
		JPanel annRunnerControlsPanel = new AnnRunnerControlsPanel(this);

		return annRunnerControlsPanel;
	}

	public static void main(String[] args) {
		// Oct 21, 2015 10:56:51 AM
		NeuralNetViewer view = new NeuralNetViewer();
	}

	// Oct 21, 2015 10:56:51 AM

	public void resetNN(DataSet dataSet, FeedForwardWithBackPropagation nn) {
		this.nnData = dataSet;
		this.nn = nn;

	}

	public void resetNN() {
		nnData = new DataSetNoisyInputsNoisyTargetsSin(-10, 10, 60);
		nn = new FeedForwardWithBackPropagation(nnData, 9);

	}
}
