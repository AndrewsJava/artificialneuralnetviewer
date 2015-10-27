// Oct 27, 2015 10:48:27 AM
package harlequinmettle.investmentadviserengine.neuralnet.gui;

import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuron;
import harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet.FeedForwardWithBackPropagation;
import harlequinmettle.utils.guitools.VerticalJPanel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class AnnRunnerControlsPanel extends VerticalJPanel {
	FeedForwardWithBackPropagation nn;

	public AnnRunnerControlsPanel(FeedForwardWithBackPropagation nn) {
		// Oct 27, 2015 10:48:38 AM
		this.nn = nn;

		add(generateStartButton());
		add(generateStopButton());
		add(generateLearningRateBumperButton());
		add(generateRandomizeWeightsButton());
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
				if (nn.stopRequested.getAndSet(false))
					nn.nnTrainingThread.start();
			}

		};
	}

	private ActionListener getStopActionListner() {
		// Oct 27, 2015 12:12:39 PM
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				// Oct 27, 2015 12:13:00 PM
				nn.stopRequested.set(true);
				;
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
				nn.randomizeAllWeights();
			}

		};
	}
}
