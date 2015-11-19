// Nov 15, 2015 10:53:24 AM
package harlequinmettle.investmentadviserengine.neuralnet.data;

public class SimpleSelfOrganizingMapData extends DataSet {

	private static final long serialVersionUID = -288520097661771137L;

	public SimpleSelfOrganizingMapData() {
		float[] input1 = { 1, 1, 1 };
		addInputForSelfOrgainizingMap(input1);
		float[] input2 = { -0.8f, -0.8f, -1 };
		addInputForSelfOrgainizingMap(input2);
		float[] input5 = { 0.8f, -0.8f, -1 };
		addInputForSelfOrgainizingMap(input5);
		float[] input6 = { -0.8f, 0.8f, -1 };
		addInputForSelfOrgainizingMap(input6);
		float[] input3 = { -1, 1, 1 };
		addInputForSelfOrgainizingMap(input3);
		float[] input4 = { -1, -1, 1 };
		addInputForSelfOrgainizingMap(input4);
	}

}
