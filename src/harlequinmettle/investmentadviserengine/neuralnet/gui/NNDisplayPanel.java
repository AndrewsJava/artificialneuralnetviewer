// Oct 31, 2015 10:48:53 AM
package harlequinmettle.investmentadviserengine.neuralnet.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class NNDisplayPanel extends JPanel {
	float width = 1000;
	float height = 500;

	@Override
	public void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		// setBackground(new Color(128, 40, 228));
		if (width == 0 || height == 0)
			return;
		BufferedImage canvas = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = canvas.createGraphics();
		drawData(g2d);
		g1.drawImage(canvas, 0, 0, null);
	}

	// Oct 31, 2015 10:58:45 AM
	private void drawData(Graphics2D g2d) {

	}
}
