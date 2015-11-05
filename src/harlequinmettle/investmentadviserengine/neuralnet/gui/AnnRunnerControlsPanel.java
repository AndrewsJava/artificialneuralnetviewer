// Oct 27, 2015 10:48:27 AM
package harlequinmettle.investmentadviserengine.neuralnet.gui;

import harlequinmettle.investmentadviserengine.neuralnet.Global;
import harlequinmettle.utils.guitools.VerticalJPanel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;

public class AnnRunnerControlsPanel extends VerticalJPanel {
	NeuralNetViewer nnView;

	public AnnRunnerControlsPanel(NeuralNetViewer nnView) {
		// Oct 27, 2015 10:48:38 AM
		this.nnView = nnView;

		add(generateResetButton());
		addCheckboxesForDrawTypes();
		add(generateSingleTrainingIterationButton());
		add(generateSingleTrainingSetIterationButton());
		add(generateStartStopButton());
		// add(generateStopButton());
		add(generateLearningRateBumperButton());
		add(generateRandomizeWeightsButton());
		add(generatePrintNNButton());
		// startStateLabelUpdateThread();
	}

	private void addCheckboxesForDrawTypes() {
		// Oct 31, 2015 11:30:29 AM
		for (String title : nnView.lineOptions)
			add(generateCheckbox(title, true));
	}

	// Oct 31, 2015 11:34:01 AM
	private Component generateCheckbox(String type, Boolean value) {
		JCheckBox drawline = new JCheckBox(type, value.booleanValue());
		drawline.addItemListener(getLinePreferencesDrawingItemlistener());
		return drawline;
	}

	private ItemListener getLinePreferencesDrawingItemlistener() {
		return new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// Oct 31, 2015 11:37:54 AM
				JCheckBox source = ((JCheckBox) (arg0.getItem()));
				String key = source.getActionCommand();
				boolean value = source.isSelected();
				nnView.dataDisplayer.drawLines.put(key, value);
			}

		};
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

	private Component generateStartStopButton() {

		JButton button = new JButton("Start/Stop Training");
		button.addActionListener(getStartStopActionListner());
		return button;
	}

	private ActionListener getStartStopActionListner() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				if (nnView.nn.nnTrainingThread != null && nnView.nn.nnTrainingThread.isAlive())
					nnView.nn.stopRequested.set(true);
				else
					nnView.nn.startNNTrainingThread();
			}

		};
	}

	private ActionListener getResetActionListner() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				nnView.resetNN();
				nnView.dataDisplayer.colors.clear();
			}

		};
	}

	private ActionListener getLearningRateBoosterActionListner() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				Global.boostLearningRate();
			}

		};
	}

	private ActionListener getWeightRandomizerActionListner() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				nnView.nn.randomizeAllWeights(0.5f);
				nnView.nn.establishTestingOuputs();
				nnView.nn.establishTrainingOutputs();
			}

		};
	}

	private ActionListener getSingleIterationActionListner() {

		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {

				nnView.nn.trainOneFullIteration();
				nnView.nn.establishTestingOuputs();
				nnView.nn.establishTrainingOutputs();
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
				nnView.nn.establishTestingOuputs();
				nnView.nn.establishTrainingOutputs();
			}

		};
	}

}
