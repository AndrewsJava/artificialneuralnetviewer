// Oct 27, 2015 10:48:27 AM
package harlequinmettle.investmentadviserengine.neuralnet.gui;

import harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet.FeedForwardWithBackPropagation;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;

public class AnnRunnerControlsPanel extends JPanel {
	FeedForwardWithBackPropagation nn;

	public AnnRunnerControlsPanel(FeedForwardWithBackPropagation nn) {
		// Oct 27, 2015 10:48:38 AM
		this.nn = nn;

		add(generateStartButton());
	}

	// Oct 27, 2015 10:48:27 AM
	private Component generateStartButton() {
		// Oct 27, 2015 10:50:19 AM

		return new JButton("testbutton");
	}
}
