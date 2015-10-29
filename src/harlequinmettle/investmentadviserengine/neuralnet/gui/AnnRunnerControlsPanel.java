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
		add(generateSingleTrainingIterationButton());
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

	private Component generateSingleTrainingIterationButton() {

		JButton button = new JButton("Single Patter Training");
		button.addActionListener(getTrainOneDataPointActionListner());
		return button;
	}

	private Component generateSingleTrainingSetIterationButton() {

		JButton button = new JButton("Single Full Set Training");
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

	private Component generateLearningRateBumperButton() {

		JButton button = new JButton("Boost Learning Rate");
		button.addActionListener(getLearningRateBoosterActionListner());
		return button;
	}

	private Component generateStartButton() {

		JButton button = new JButton("Start Training");
		button.addActionListener(getStartActionListner());
		return button;
	}

	private Component generateStopButton() {

		JButton button = new JButton("Stop Training");
		button.addActionListener(getStopActionListner());
		return button;
	}

	private ActionListener getStartActionListner() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				nnView.nn.startNNTrainingThread();
			}

		};
	}

	private ActionListener getStopActionListner() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
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
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				ArtificialNeuron.learningRate += 0.05;
			}

		};
	}

	private ActionListener getWeightRandomizerActionListner() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
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
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {

				System.out.println(nnView.nn);
			}

		};
	}

	private ActionListener getTrainOneDataPointActionListner() {

		return new ActionListener() {
			int trainingPatternIndex = 0;

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {

				nnView.nn.trainPattern(trainingPatternIndex++);
			}

		};
	}

	private void startStateLabelUpdateThread() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				ConcurrentSkipListMap<String, String> nnState = nnView.nn.getState();
				for (Entry<String, String> ent : nnState.entrySet())
					// TODO: display state
					add(JLabelFactory.doBluishJLabel("label1"));
			}

		}).start();
	}

}
