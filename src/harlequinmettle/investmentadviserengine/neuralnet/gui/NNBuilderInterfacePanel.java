// Oct 31, 2015 10:45:16 AM
package harlequinmettle.investmentadviserengine.neuralnet.gui;

import harlequinmettle.investmentadviserengine.neuralnet.ArtificialNeuralNetLayer;
import harlequinmettle.investmentadviserengine.neuralnet.artificailneuralnet.FeedForwardWithBackPropagation;
import harlequinmettle.investmentadviserengine.neuralnet.data.DataSetNoisyInputsNoisyTargetsSin;
import harlequinmettle.utils.guitools.HorizontalJPanel;
import harlequinmettle.utils.guitools.JButtonWithEnterKeyAction;
import harlequinmettle.utils.guitools.VerticalJPanel;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class NNBuilderInterfacePanel {
	private static final int MAX_LAYERS = 5;
	VerticalJPanel vpanel = new VerticalJPanel();
	HorizontalJPanel nnDataSetPanel = new HorizontalJPanel();
	HorizontalJPanel nnLayerCountsPanel = new HorizontalJPanel();
	HorizontalJPanel graphOptions = new HorizontalJPanel();
	TreeMap<Integer, JTextField> layersNeuronCounts = new TreeMap<Integer, JTextField>();
	LinkedHashMap<JLabel, JComponent> labelDataOptionsMap = new LinkedHashMap<JLabel, JComponent>();

	String[] fnOptions = { "Sin" };
	JComboBox fnList = new JComboBox(fnOptions);
	String[] fnstart = { "-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "-9", "-10", "-11", "-12", "-13", "-14", "-15", "-25", "-35", };
	JComboBox fnstartList = new JComboBox(fnstart);
	String[] fnendOptions = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "25", "35", };
	JComboBox fnendList = new JComboBox(fnendOptions);
	String[] fnTrainPointsOptions = { "10", "20", "30", "40", "50", "60", "70", "80", "90", "100", "110", "120", "130", "140", "150", "200", "250",
			"450", };
	JComboBox fnTrainPointsList = new JComboBox(fnTrainPointsOptions);

	NeuralNetViewer neuralNetViewer;

	public NNBuilderInterfacePanel(NeuralNetViewer neuralNetViewer) {
		this.neuralNetViewer = neuralNetViewer;
		vpanel.add(nnDataSetPanel);
		vpanel.add(nnLayerCountsPanel);
		// addCheckboxesForDrawTypes();
		mapLabels();
		buildLayerCountsPanel();
		buildDataSetPanel();
	}

	// Nov 6, 2015 9:51:59 AM
	private void mapLabels() {
		fnList.setEnabled(false);
		labelDataOptionsMap.put(new JLabel("target function"), fnList);
		labelDataOptionsMap.put(new JLabel("input start"), fnstartList);
		labelDataOptionsMap.put(new JLabel("input end"), fnendList);
		labelDataOptionsMap.put(new JLabel("training points"), fnTrainPointsList);
	}

	// Nov 6, 2015 9:40:52 AM
	private void buildDataSetPanel() {
		for (Entry<JLabel, JComponent> ent : labelDataOptionsMap.entrySet()) {

			VerticalJPanel vpanel = new VerticalJPanel();
			vpanel.add(ent.getKey());
			vpanel.add(ent.getValue());
			nnDataSetPanel.add(vpanel);
		}

	}

	// Nov 1, 2015 11:23:36 AM
	private void buildLayerCountsPanel() {

		addNNCreateButton();
		addNNLayerNeuronCountsTextFields();

	}

	// Nov 1, 2015 11:44:12 AM
	private void addNNLayerNeuronCountsTextFields() {
		for (int i = 0; i < MAX_LAYERS; i++) {
			JTextField layersize = new JTextField();
			layersize.setFont(new Font("Arail", Font.PLAIN, 22));
			layersize.setToolTipText("add number other than zero for hidden layer " + (i + 1) + " node count");
			if (neuralNetViewer.nn.hiddenLayers.size() > i) {
				ArtificialNeuralNetLayer layer = neuralNetViewer.nn.hiddenLayers.get(i);
				String defaultLayer = "";
				if (layer != null)
					defaultLayer += layer.neuronsInLayer.size();

				layersize.setText(defaultLayer);
			}
			layersNeuronCounts.put(i, layersize);
			nnLayerCountsPanel.add(layersize);
		}
	}

	// Nov 1, 2015 11:43:22 AM
	private void addNNCreateButton() {
		JButtonWithEnterKeyAction create = new JButtonWithEnterKeyAction("Apply");
		create.addActionListener(getNNCreator());
		nnLayerCountsPanel.add(create);

	}

	private ActionListener getNNCreator() {

		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				int start = Integer.parseInt(fnstartList.getSelectedItem().toString());
				int end = Integer.parseInt(fnendList.getSelectedItem().toString());
				int points = Integer.parseInt(fnTrainPointsList.getSelectedItem().toString());
				DataSetNoisyInputsNoisyTargetsSin nnData = new DataSetNoisyInputsNoisyTargetsSin(start, end, points);
				FeedForwardWithBackPropagation nn = new FeedForwardWithBackPropagation(nnData, getLayerNeuronCounts());
				neuralNetViewer.resetNN(nnData, nn);
			}

		};
	}

	// Nov 1, 2015 11:57:40 AM
	protected int[] getLayerNeuronCounts() {
		int[] hiddenLayerNeuronCounts = new int[MAX_LAYERS];
		for (Entry<Integer, JTextField> ent : layersNeuronCounts.entrySet()) {
			String text = ent.getValue().getText();
			if (!text.trim().matches("\\d+"))
				continue;
			try {
				hiddenLayerNeuronCounts[ent.getKey()] = Integer.valueOf(text);
			} catch (Exception e) {

			}
		}
		return hiddenLayerNeuronCounts;
	}

	private void addCheckboxesForDrawTypes() {
		vpanel.add(graphOptions);
		for (String title : neuralNetViewer.lineOptions)
			graphOptions.add(generateCheckbox(title, true));
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
				neuralNetViewer.dataDisplayer.drawLines.put(key, value);
			}

		};
	}

}
