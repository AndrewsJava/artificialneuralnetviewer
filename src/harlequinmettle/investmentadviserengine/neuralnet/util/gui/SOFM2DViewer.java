// Nov 16, 2015 9:28:40 AM
package harlequinmettle.investmentadviserengine.neuralnet.util.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
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
	private static final int POINT_SIZE_ADDING_RANGE = 12;
	// private TreeMap<Integer, ArrayList<Float>> dataSets = new
	// TreeMap<Integer, ArrayList<Float>>();
	volatile private TreeMap<String, ArrayList<DataDrawPoint>> titledDataSets = new TreeMap<String, ArrayList<DataDrawPoint>>();
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
	private double minSize = Double.POSITIVE_INFINITY;
	private double maxSize = Double.NEGATIVE_INFINITY;

	public TreeMap<String, Color> colors = new TreeMap<String, Color>();

	public void addDisplayText(String key, String value) {
		displayText.put(key, value);
	}

	public void addData(String index, Collection<float[]> threeDPoints) {
		ArrayList<DataDrawPoint> dataset = pairData(new ArrayList<float[]>(threeDPoints));
		titledDataSets.put("" + index, dataset);
		rescaleAllLines();
		// int colorCode = index.hashCode();
		if (!colors.containsKey(index))
			colors.put(index, new Color((int) (Integer.MAX_VALUE * Math.random()) >> 8));
		if (!drawLines.containsKey(index))
			drawLines.put(index, true);
	}

	private void rescaleAllLines() {
		// Oct 30, 2015 9:20:17 AM
		for (Entry<String, ArrayList<DataDrawPoint>> ent : titledDataSets.entrySet()) {
			ArrayList<DataDrawPoint> scaleSet = scalePoints(ent.getValue());
			scaledDataPoints.put("" + ent.getKey(), scaleSet);
		}
	}

	// Oct 22, 2015 9:05:16 AM
	private ArrayList<DataDrawPoint> scalePoints(ArrayList<DataDrawPoint> arrayList) {

		ArrayList<DataDrawPoint> scaleSet = new ArrayList<DataDrawPoint>();
		if (arrayList.isEmpty())
			return scaleSet;
		float sizeRange = (float) (maxSize - minSize);
		float sizeFactor = (float) (POINT_SIZE_ADDING_RANGE / sizeRange);
		float horizontalRange = (float) (maxX - minX);
		float horizontalFactor = (float) (0.6 * width / horizontalRange);

		float vertRange = (float) (maxY - minY);
		float verticalFactor = (float) -(0.8 * height / vertRange);

		for (DataDrawPoint originalData : arrayList) {
			DataDrawPoint scaledPoint = new DataDrawPoint();
			scaledPoint.x = (float) (0.1 * width + horizontalFactor * (originalData.x - minX));

			scaledPoint.y = (float) (0.05 * height + verticalFactor * (minY + originalData.y));
			scaledPoint.size = (float) (SMALL_POINT_SIZE + sizeFactor * (originalData.size - minSize));

			scaleSet.add(scaledPoint);
		}
		return scaleSet;
	}

	// Oct 21, 2015 1:12:14 PM
	private ArrayList<DataDrawPoint> pairData(ArrayList<float[]> data) {

		setMinMaxX(extractAllIndex(data, 0));
		setMinMaxY(extractAllIndex(data, 1));
		setMinMaxSize(extractAllIndex(data, 2));
		int datasize = data.size();
		ArrayList<DataDrawPoint> dataset = new ArrayList<DataDrawPoint>();
		for (int i = 0; i < datasize; i++) {
			float x = data.get(i)[0];
			float y = data.get(i)[1];
			float size = 1;
			if (data.get(i).length >= 3)
				size = data.get(i)[2];
			DataDrawPoint d = new DataDrawPoint();
			d.x = x;
			d.y = y;
			d.size = size;
			dataset.add(d);
		}
		return dataset;
	}

	private ArrayList<Float> extractAllIndex(ArrayList<float[]> data1, int i) {
		ArrayList<Float> allInIndex = new ArrayList<Float>();
		for (float[] f : data1) {
			if (i >= f.length)
				break;
			allInIndex.add(f[i]);
		}
		return allInIndex;
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

	private boolean setMinMaxSize(ArrayList<Float> dataset) {
		// Oct 21, 2015 12:48:42 PM
		if (dataset.isEmpty())
			return false;
		double maxSize = Collections.max(dataset);
		boolean minMaxChange = false;
		if (maxSize > this.maxSize) {
			this.maxSize = maxSize;
			minMaxChange = true;
		}
		double minSize = Collections.min(dataset);
		if (minSize < this.minSize) {
			this.minSize = minSize;
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
		for (Entry<String, ArrayList<DataDrawPoint>> ent : scaledDataPoints.entrySet()) {
			ArrayList<DataDrawPoint> pointsArray = ent.getValue();
			if (pointsArray.isEmpty())
				continue;

			String index = ent.getKey();
			Boolean lineDrawOption = drawLines.get(index);
			if (lineDrawOption == null || !lineDrawOption)
				continue;

			g2d.setColor(colors.get(ent.getKey()));
			for (DataDrawPoint points : pointsArray) {

				float scaledX = points.x;
				float scaledY = points.y;
				int drawSize = (int) points.size;
				if (!index.contains("input")) {
					g2d.fillOval((int) scaledX - drawSize / 2, (int) scaledY - drawSize / 2, drawSize, drawSize);
				} else {
					g2d.fillRect((int) scaledX - drawSize / 2, (int) scaledY - drawSize / 2, drawSize + 2, drawSize + 2);
				}
			}
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
