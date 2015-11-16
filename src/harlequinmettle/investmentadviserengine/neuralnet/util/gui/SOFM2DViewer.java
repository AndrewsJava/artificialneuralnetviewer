// Nov 16, 2015 9:28:40 AM
package harlequinmettle.investmentadviserengine.neuralnet.util.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.swing.JPanel;

public class SOFM2DViewer extends JPanel {

	private static final int POINT_SIZE = 5;
	private static final int SMALL_POINT_SIZE = 2;
	// private TreeMap<Integer, ArrayList<Float>> dataSets = new
	// TreeMap<Integer, ArrayList<Float>>();
	volatile private TreeMap<String, ArrayList<float[]>> titledDataSets = new TreeMap<String, ArrayList<float[]>>();
	volatile private TreeMap<String, ArrayList<DataDrawPoint>> scaledDataPoints = new TreeMap<String, ArrayList<DataDrawPoint>>();

	public final ConcurrentSkipListMap<String, Boolean> drawLines = new ConcurrentSkipListMap<String, Boolean>();
	public final ConcurrentSkipListMap<String, String> displayText = new ConcurrentSkipListMap<String, String>();
	Font displayFont = new Font("Arail", Font.PLAIN, 24);
	private double width;
	private double height;
	private double minX = Double.POSITIVE_INFINITY;
	private double maxX = Double.NEGATIVE_INFINITY;
	private double minY = Double.POSITIVE_INFINITY;
	private double maxY = Double.NEGATIVE_INFINITY;

	public TreeMap<String, Color> colors = new TreeMap<String, Color>();

	public void addDisplayText(String key, String value) {

		displayText.put(key, value);
	}

	public void addData(String index, ArrayList<Float> xs, ArrayList<Float> ys) {
		ArrayList<DataDrawPoint> dataset = pairData(xs, ys);
		titledDataSets.put("" + index, dataset);
		rescaleAllLines();
		int colorCode = index.hashCode();
		if (!colors.containsKey(index))
			colors.put(index, new Color((int) (Integer.MAX_VALUE * Math.random()) >> 8));
		if (!drawLines.containsKey(index))
			drawLines.put(index, true);
	}

	// private Color getRandomColor(int colorCode) {
	// // Oct 22, 2015 10:00:12 AM
	// Color randomColor = new Color(colorCode);
	// return null;
	// }

	private void rescaleAllLines() {
		// Oct 30, 2015 9:20:17 AM
		for (Entry<String, ArrayList<Point2D.Float>> ent : titledDataSets.entrySet()) {
			ArrayList<Point2D.Float> scaleSet = scalePoints(ent.getValue());
			scaledDataPoints.put("" + ent.getKey(), scaleSet);
		}
	}

	// Oct 22, 2015 9:05:16 AM
	private ArrayList<Point2D.Float> scalePoints(ArrayList<Point2D.Float> dataset) {

		ArrayList<Point2D.Float> scaleSet = new ArrayList<Point2D.Float>();
		if (dataset.isEmpty())
			return scaleSet;
		float horizontalRange = (float) (maxX - minX);
		float horizontalFactor = (float) (0.6 * width / horizontalRange);

		float vertRange = (float) (maxY - minY);
		float verticalFactor = (float) -(0.8 * height / vertRange);

		for (Point2D.Float originalData : dataset) {
			Point2D.Float scaledPoint = new Point2D.Float();
			scaledPoint.x = (float) (0.1 * width + horizontalFactor * (originalData.x - minX));

			scaledPoint.y = (float) (0.05 * height + verticalFactor * (minY + originalData.y));
			scaleSet.add(scaledPoint);
		}
		return scaleSet;
	}

	// Oct 21, 2015 1:12:14 PM
	private ArrayList<Point2D.Float> pairData(ArrayList<Float> data1, ArrayList<Float> data2) {
		setMinMaxX(data1);
		setMinMaxY(data2);
		int size = data1.size() < data2.size() ? data1.size() : data2.size();
		ArrayList<Point2D.Float> dataset = new ArrayList<Point2D.Float>();
		for (int i = 0; i < size; i++) {
			float x = data1.get(i);
			float y = data2.get(i);
			Point2D.Float d = new Point2D.Float(x, y);
			dataset.add(d);
		}
		return dataset;
	}

	public void addData(String title, Collection<float[]> dataset) {
		titledDataSets.put(title, dataset);
	}

	private boolean setMinMaxY(ArrayList<Float> dataset) {
		// Oct 21, 2015 12:48:42 PM
		if (dataset.isEmpty())
			return false;
		double maxY = Collections.max(dataset);
		boolean minMaxChange = false;
		if (maxY > this.maxY) {
			this.maxY = maxY;
			minMaxChange = true;
		}
		double minY = Collections.min(dataset);
		if (minY < this.minY) {
			this.minY = minY;
			minMaxChange = true;
		}
		return minMaxChange;
	}

	private boolean setMinMaxX(ArrayList<Float> dataset) {
		// Oct 21, 2015 12:48:42 PM
		if (dataset.isEmpty())
			return false;
		double maxX = Collections.max(dataset);
		boolean minMaxChange = false;
		if (maxX > this.maxX) {
			this.maxX = maxX;
			minMaxChange = true;
		}
		double minX = Collections.min(dataset);
		if (minX < this.minX) {
			this.minX = minX;
			minMaxChange = true;
		}
		return minMaxChange;
	}

	public SOFM2DViewer() {

		setDimensions();
	}

	@Override
	public void repaint() {
		// Oct 22, 2015 9:23:43 AM
		setDimensions();
		super.repaint();
	}

	// Oct 22, 2015 8:13:36 AM
	private void setDimensions() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();
		if (getParent() != null) {
			screenSize = getParent().getSize();
			width = screenSize.getWidth();
			height = screenSize.getHeight();
		}

	}

	// Oct 21, 2015 12:12:29 PM
	@Override
	public void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		// setBackground(new Color(128, 40, 228));
		if (width == 0 || height == 0)
			return;
		BufferedImage canvas = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = canvas.createGraphics();
		drawData(g2d);

		// Graphics2D g = (Graphics2D) g1;
		g1.drawImage(canvas, 0, 0, null);
	}

	private void drawData(Graphics2D g2d) {
		// Oct 21, 2015 12:42:44 PM
		g2d.setBackground(Color.black);
		if (scaledDataPoints.isEmpty())
			return;
		int colorchooser = 0;
		for (Entry<String, ArrayList<DataDrawPoint>> ent : scaledDataPoints.entrySet()) {
			ArrayList<DataDrawPoint> pointsArray = ent.getValue();
			if (pointsArray.isEmpty())
				continue;
			String index = ent.getKey();
			Boolean lineDrawOption = drawLines.get(index);
			if (lineDrawOption != null && !lineDrawOption)
				continue;
			GeneralPath line = new GeneralPath();
			Point2D.Float firstPoint = pointsArray.get(0);
			boolean first = true;
			line.moveTo(firstPoint.x, firstPoint.y);
			// if (colorchooser++ == 0)
			// g2d.setColor(Color.white);
			// else
			// g2d.setColor(Color.blue);
			g2d.setColor(colors.get(ent.getKey()));
			for (Point2D.Float points : pointsArray) {
				if (first) {
					first = false;
					continue;
				}
				float scaledX = points.x;
				float scaledY = points.y;

				if (index.contains("testing"))
					g2d.fillOval((int) scaledX - SMALL_POINT_SIZE / 2, (int) scaledY - SMALL_POINT_SIZE / 2, SMALL_POINT_SIZE, SMALL_POINT_SIZE);
				else
					g2d.fillRect((int) scaledX - POINT_SIZE / 2, (int) scaledY - POINT_SIZE / 2, POINT_SIZE, POINT_SIZE);
			}
			colorchooser++;
			if (!index.contains("testing"))
				g2d.draw(line);
		}
		g2d.setColor(Color.green);
		g2d.setFont(displayFont);
		float x = (float) (width * 0.02f);
		float x2 = (float) x + (100);
		float y = (float) (height * 0.1f);

		for (Entry<String, String> ent : displayText.entrySet()) {
			g2d.drawString(ent.getKey(), x, y);
			g2d.drawString(ent.getValue(), x2, y);
			y += displayFont.getSize2D() + 10;
		}
	}

	public static void main(String[] args) {
		// Nov 16, 2015 9:28:40 AM

	}
	// Nov 16, 2015 9:28:40 AM
}
