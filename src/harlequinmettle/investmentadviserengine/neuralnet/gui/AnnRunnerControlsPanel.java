// Oct 27, 2015 10:48:27 AM
package harlequinmettle.investmentadviserengine.neuralnet.gui;

import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuron;
import harlequinmettle.utils.guitools.JLabelFactory;
import harlequinmettle.utils.guitools.VerticalJPanel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.swing.JButton;

public class AnnRunnerControlsPanel extends VerticalJPanel {
	NeuralNetViewer nnView;

	public AnnRunnerControlsPanel(NeuralNetViewer nnView) {
		// Oct 27, 2015 10:48:38 AM
		this.nnView = nnView;

		add(generateResetButton());
		add(generateStartButton());
		add(generateStopButton());
		add(generateLearningRateBumperButton());
		add(generateRandomizeWeightsButton());
		add(generatePrintNNButton());
		ConcurrentSkipListMap<String, String> nnState = nnView.nn.getState();
		add(JLabelFactory.doBluishJLabel("label1"));
	}

	private Component generateResetButton() {

		JButton trainStartButton = new JButton("Reset");
		trainStartButton.addActionListener(getResetActionListner());
		return trainStartButton;
	}

	private Component generatePrintNNButton() {

		JButton trainStartButton = new JButton("Display Neural Net");
		trainStartButton.addActionListener(getPrintNNActionListner());
		return trainStartButton;
	}

	private Component generateRandomizeWeightsButton() {

		JButton trainStartButton = new JButton("Randomize Weights");
		trainStartButton.addActionListener(getWeightRandomizerActionListner());
		return trainStartButton;
	}

	// Oct 27, 2015 10:48:27 AM
	private Component generateLearningRateBumperButton() {

		JButton trainStartButton = new JButton("Boost Learning Rate");
		trainStartButton.addActionListener(getLearningRateBoosterActionListner());
		return trainStartButton;
	}

	private Component generateStartButton() {
		// Oct 27, 2015 10:50:19 AM

		JButton trainStartButton = new JButton("Start Training");
		trainStartButton.addActionListener(getStartActionListner());
		return trainStartButton;
	}

	// Oct 27, 2015 10:48:27 AM
	private Component generateStopButton() {
		// Oct 27, 2015 10:50:19 AM

		JButton trainStartButton = new JButton("Stop Training");
		trainStartButton.addActionListener(getStopActionListner());
		return trainStartButton;
	}

	private ActionListener getStartActionListner() {
		// Oct 27, 2015 12:12:39 PM
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				// Oct 27, 2015 12:13:00 PM
				nnView.nn.startNNTrainingThread();
			}

		};
	}

	private ActionListener getStopActionListner() {
		// Oct 27, 2015 12:12:39 PM
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				// Oct 27, 2015 12:13:00 PM
				nnView.nn.stopRequested.set(true);

			}

		};
	}

	private ActionListener getResetActionListner() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				nnView.resetNN();
			}

		};
	}

	private ActionListener getLearningRateBoosterActionListner() {
		// Oct 27, 2015 12:12:39 PM
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				// Oct 27, 2015 12:13:00 PM
				ArtificialNeuron.learningRate += 0.15;
			}

		};
	}

	private ActionListener getWeightRandomizerActionListner() {
		// Oct 27, 2015 12:12:39 PM
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				// Oct 27, 2015 12:13:00 PM
				nnView.nn.randomizeAllWeights();
			}

		};
	}

	private ActionListener getPrintNNActionListner() {
		// Oct 27, 2015 12:12:39 PM
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				// Oct 27, 2015 12:13:00 PM
				System.out.println(nnView.nn);
			}

		};
	}
}
