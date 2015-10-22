// Oct 16, 2015 10:39:02 AM
package harlequinmettle.investmentadviserengine.neuralnet.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class DataSet {
	// TODO : DATA NORMALIZATION
	public int numberDataSets = 0;
	public ArrayList<float[]> inputs = new ArrayList<float[]>();
	public ArrayList<float[]> targets = new ArrayList<float[]>();
	public TreeMap<Integer, float[]> outputs = new TreeMap<Integer, float[]>();
	public float ssqError = Float.NaN;

	// Oct 21, 2015 9:34:20 AM
	@Override
	public String toString() {
		String response = "";
		for (int i = 0; i < numberDataSets; i++) {
			response += " {" + Arrays.toString(inputs.get(i)) + " : " + Arrays.toString(targets.get(i)) + "} ";
		}
		return response;
	}

}
