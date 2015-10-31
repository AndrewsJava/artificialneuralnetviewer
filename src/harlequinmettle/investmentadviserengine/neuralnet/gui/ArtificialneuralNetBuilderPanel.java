// Oct 25, 2015 8:45:09 AM
package harlequinmettle.investmentadviserengine.neuralnet.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class ArtificialneuralNetBuilderPanel extends JPanel {
	NeuralNetViewer neuralNetViewer;
	NNBuilderInterfacePanel controlls = new NNBuilderInterfacePanel();
	NNDisplayPanel nnview = new NNDisplayPanel();

	// Oct 25, 2015 8:45:32 AM
	public ArtificialneuralNetBuilderPanel(NeuralNetViewer neuralNetViewer) {
		this.neuralNetViewer = neuralNetViewer;
		setLayout(new BorderLayout());

		add(controlls.vpanel, BorderLayout.WEST);
		add(nnview, BorderLayout.CENTER);
	}

}
