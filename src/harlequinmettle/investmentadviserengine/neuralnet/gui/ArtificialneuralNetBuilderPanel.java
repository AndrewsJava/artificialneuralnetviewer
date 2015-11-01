// Oct 25, 2015 8:45:09 AM
package harlequinmettle.investmentadviserengine.neuralnet.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class ArtificialneuralNetBuilderPanel extends JPanel {
	float width, height;
	NeuralNetViewer neuralNetViewer;
	NNBuilderInterfacePanel controlls = new NNBuilderInterfacePanel();
	NNDisplayPanel nnview = new NNDisplayPanel();

	// Oct 25, 2015 8:45:32 AM
	public ArtificialneuralNetBuilderPanel(NeuralNetViewer neuralNetViewer) {
		this.neuralNetViewer = neuralNetViewer;
		controlls.setNNViewer(this);
		setLayout(new BorderLayout());
		setDimensions();
		controlls.view.setPreferredSize(new Dimension((int) (width * 0.15f), (int) (height)));
		nnview.setPreferredSize(new Dimension((int) (width * 0.9f), (int) (height)));
		add(controlls.view, BorderLayout.WEST);
		add(nnview, BorderLayout.CENTER);
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
