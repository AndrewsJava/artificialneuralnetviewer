// Oct 25, 2015 8:45:09 AM
package harlequinmettle.investmentadviserengine.neuralnet.gui;

import harlequinmettle.utils.guitools.VerticalJPanel;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class ArtificialneuralNetBuilderPanel extends JPanel {
	float width, height;
	NeuralNetViewer neuralNetViewer;
	NNBuilderInterfacePanel controlls = new NNBuilderInterfacePanel();
	NNDisplayPanel nnview = new NNDisplayPanel();
	NNDisplayPanel nnDATAview = new NNDisplayPanel();

	// Oct 25, 2015 8:45:32 AM
	public ArtificialneuralNetBuilderPanel(NeuralNetViewer neuralNetViewer) {
		this.neuralNetViewer = neuralNetViewer;
		controlls.setNNViewer(this);
		// setLayout(new BorderLayout());
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setDimensions();
		controlls.view.setPreferredSize(new Dimension((int) (width * 0.15f), (int) (height)));
		nnview.setPreferredSize(new Dimension((int) (width * 0.9f), (int) (height / 2)));
		nnDATAview.setPreferredSize(new Dimension((int) (width * 0.9f), (int) (height / 2)));
		VerticalJPanel graphicals = new VerticalJPanel();
		graphicals.add(nnview);
		graphicals.add(nnDATAview);
		// add(controlls.view);
		add(graphicals);
	}

	private void setDimensions() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (float) screenSize.getWidth();
		height = (float) screenSize.getHeight();
		if (getParent() != null) {
			screenSize = getParent().getSize();
			width = (float) screenSize.getWidth();
			height = (float) screenSize.getHeight();
		}

	}
}
