// Nov 16, 2015 9:22:59 AM
package harlequinmettle.investmentadviserengine.neuralnet.util.gui;

import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetConnection;
import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuron;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSet;
import harlequinmettle.investmentadviserengine.neuralnet.data.SimpleSelfOrganizingMapData;
import harlequinmettle.investmentadviserengine.neuralnet.selforganizingmap.SelfOrganizingMap;
import harlequinmettle.investmentadviserengine.util.SystemTool;
import harlequinmettle.utils.guitools.JFrameFactory;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SOMViewer {

	SOFM2DViewer dataDisplayer;
	DataSet dataToMap;
	SelfOrganizingMap map;
	AtomicBoolean displayThreadStopRequested = new AtomicBoolean(false);
	String inputPoints = "input points";
	String weights = "weights";

	public SOMViewer() {
		reset();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				showGui();
			}
		});
		startGuiThread();
		map.startSelfOrganizationThread();
	}

	private void startGuiThread() {
		if (displayThreadStopRequested.get())
			SystemTool.takeABreak(500);
		displayThreadStopRequested.set(false);
		new Thread(new Runnable() {
			public void run() {
				countinuousGuiUpdates();
			}

		}).start();
	}

	private void countinuousGuiUpdates() {
		while (dataDisplayer == null)
			SystemTool.takeABreak(100);
		dataDisplayer.colors.clear();

		dataDisplayer.addData(inputPoints, dataToMap.trainingInputs);
		dataDisplayer.addData(weights, getAllOutputNeuronsInputConnectionWeights());
		float lastError = Float.NEGATIVE_INFINITY;
		while (true) {
			if (displayThreadStopRequested.get())
				break;
			SystemTool.takeABreak(300);
			dataDisplayer.addData(weights, getAllOutputNeuronsInputConnectionWeights());
			if (dataToMap.avgError == dataToMap.avgError)
				if (dataToMap.avgError != lastError) {
					dataDisplayer.addDisplayText("error: ", "" + String.format("%1$-10.2f", dataToMap.avgError));

				}
			dataDisplayer.addDisplayText("learn rt: ", "" + String.format("%1$-10.2f", map.learningRate));
			lastError = dataToMap.avgError;
			dataDisplayer.repaint();
		}
	}

	private ArrayList<float[]> getAllOutputNeuronsInputConnectionWeights() {
		ArrayList<float[]> weights = new ArrayList<float[]>();
		for (ArtificialNeuron neuron : map.outputLayer.neuronsInLayer) {
			float[] inputConnectionWeights = new float[neuron.inputConnections.size()];
			weights.add(inputConnectionWeights);
			int i = 0;
			for (ArtificialNeuralNetConnection connection : neuron.inputConnections) {
				inputConnectionWeights[i++] = connection.weight.weight;
			}
		}
		return weights;
	}

	private void reset() {
		dataToMap = new SimpleSelfOrganizingMapData();
		map = new SelfOrganizingMap(dataToMap, 4);
	}

	private void showGui() {

		JFrame fullScreen = JFrameFactory.displayFullScreenPrimaryApplicationJFrame("self orgainized feature map");

		JPanel annRunner = new JPanel(new BorderLayout());
		fullScreen.add(annRunner);

		// SOFMRunnerControlsPanel runnerControls = new
		// SOFMRunnerControlsPanel(this);
		// annRunner.add(runnerControls, BorderLayout.WEST);

		dataDisplayer = new SOFM2DViewer();
		annRunner.add(dataDisplayer, BorderLayout.CENTER);

		fullScreen.pack();
	}

	public static void main(String[] args) {
		// Nov 16, 2015 9:22:59 AM
		new SOMViewer();
	}
	// Nov 16, 2015 9:22:59 AM
}
