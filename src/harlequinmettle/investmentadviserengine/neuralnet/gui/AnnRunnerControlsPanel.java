// Oct 27, 2015 10:48:27 AM
package harlequinmettle.investmentadviserengine.neuralnet.gui;

import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuron;
import harlequinmettle.utils.guitools.JLabelFactory;
import harlequinmettle.utils.guitools.VerticalJPanel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.swing.JButton;

public class AnnRunnerControlsPanel extends VerticalJPanel {
	NeuralNetViewer nnView;

	public AnnRunnerControlsPanel(NeuralNetViewer nnView) {
		// Oct 27, 2015 10:48:38 AM
		this.nnView = nnView;

		add(generateResetButton());
		add(generateSingleTrainingSetIterationButton());
		add(generateStartButton());
		add(generateStopButton());
		add(generateLearningRateBumperButton());
		add(generateRandomizeWeightsButton());
		add(generatePrintNNButton());
		// startStateLabelUpdateThread();
	}

	private Component generateResetButton() {

		JButton button = new JButton("Reset");
		button.addActionListener(getResetActionListner());
		return button;
	}

	private Component generateSingleTrainingSetIterationButton() {

		JButton button = new JButton("Single Full Patter Training");
		button.addActionListener(getSingleIterationActionListner());
		return button;
	}

	private Component generatePrintNNButton() {

		JButton button = new JButton("Display Neural Net");
		button.addActionListener(getPrintNNActionListner());
		return button;
	}

	private Component generateRandomizeWeightsButton() {

		JButton button = new JButton("Randomize Weights");
		button.addActionListener(getWeightRandomizerActionListner());
		return button;
	}

	// Oct 27, 2015 10:48:27 AM
	private Component generateLearningRateBumperButton() {

		JButton button = new JButton("Boost Learning Rate");
		button.addActionListener(getLearningRateBoosterActionListner());
		return button;
	}

	private Component generateStartButton() {
		// Oct 27, 2015 10:50:19 AM

		JButton button = new JButton("Start Training");
		button.addActionListener(getStartActionListner());
		return button;
	}

	// Oct 27, 2015 10:48:27 AM
	private Component generateStopButton() {
		// Oct 27, 2015 10:50:19 AM

		JButton button = new JButton("Stop Training");
		button.addActionListener(getStopActionListner());
		return button;
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

	private ActionListener getSingleIterationActionListner() {

		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {

				nnView.nn.trainOneFullIteration();
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

	private void startStateLabelUpdateThread() {
		// Oct 28, 2015 11:24:41 AM
		new Thread(new Runnable() {

			@Override
			public void run() {
				// Oct 28, 2015 11:25:27 AM

				ConcurrentSkipListMap<String, String> nnState = nnView.nn.getState();
				for (Entry<String, String> ent : nnState.entrySet())
					// TODO: display state
					add(JLabelFactory.doBluishJLabel("label1"));
			}

		}).start();
	}

}
