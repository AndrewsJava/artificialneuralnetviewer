import harlequinmettle.utils.filetools.SerializationTool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

// Oct 29, 2015 9:27:19 AM

public class KeyStatsDatabaseBuilder extends KeyStatsDatabaseDownloadAndTableExtractor {

	public static void main(String[] args) {
		// Oct 29, 2015 9:27:19 AM

		// new DownloadKeyStats().downloadAll(1500);

		// new DownloadKeyStats().extractTableData();
		// extractDataFromTables();
		// buildDatabaseFromExtractedTables();
		// testDatabaseValues();
		// buildInputDataFromKeyStats();
		dataQuality();
	}

	private static void dataQuality() {
		inputs = SerializationTool.deserializeObject(inputs.getClass(), "obj_database_inputs_key_stats");

		System.out.println(keysToUse.length + " , ");
		HashSet<HashSet<Integer>> useKeys = new HashSet<HashSet<Integer>>();
		TreeMap<Double, Set<Integer>> optimum = new TreeMap<Double, Set<Integer>>();
		for (int i = 0; i < keysToUse.length - 1; i++) {
			for (int j = 1 + i; j < keysToUse.length; j++) {
				HashSet<Integer> subset = new HashSet<Integer>();
				subset.add(i);
				subset.add(j);
				useKeys.add(subset);
			}

		}
		for (Set<Integer> set : useKeys) {

			for (int i : set)
				System.out.print("[" + keysToUse[i] + "] ");
			System.out.println();
			TreeSet<String> quailityData = new TreeSet<String>();
			for (Entry<String, float[]> keystats : inputs.entrySet()) {
				float[] subset = buildSubset(set, keystats.getValue());

				if (Float.isNaN(sum(subset)))
					continue;
				quailityData.add(keystats.getKey());
			}
			Double key = quailityData.size() + Math.random() / 100;
			optimum.put(key, set);
		}
		for (Entry<Double, Set<Integer>> subset : optimum.entrySet()) {
			System.out.print("  " + subset.getKey().intValue());
			for (int i : subset.getValue())
				System.out.print("  [" + keysToUse[i] + "] ");
			System.out.println();
		}

	}

	private static void dataQualityPowerSet() {
		inputs = SerializationTool.deserializeObject(inputs.getClass(), "obj_database_inputs_key_stats");

		System.out.println(keysToUse.length + " , ");
		TreeSet<Integer> useKeys = new TreeSet<Integer>();
		TreeMap<Double, Set<Integer>> optimum = new TreeMap<Double, Set<Integer>>();
		for (int i = 0; i < keysToUse.length; i++) {
			useKeys.add(i);
		}
		Set<Set<Integer>> powerSet = powerSet(useKeys);
		for (Set<Integer> set : powerSet) {

			for (int i : set)
				System.out.print(keysToUse[i] + " , ");
			System.out.println();
			TreeSet<String> quailityData = new TreeSet<String>();
			for (Entry<String, float[]> keystats : inputs.entrySet()) {
				float[] subset = buildSubset(set, keystats.getValue());

				if (Float.isNaN(sum(subset)))
					continue;
				quailityData.add(keystats.getKey());
			}
			Double key = quailityData.size() + 5 * set.size() + Math.random() / 100;
			optimum.put(key, set);
		}
		for (Set<Integer> subset : optimum.values()) {
			for (int i : subset)
				System.out.print("  " + keysToUse[i]);
			System.out.println();
		}
	}

	private static float[] buildSubset(Set<Integer> set, float[] fullInputSet) {
		float[] subSet = new float[set.size()];
		int index = 0;
		for (int i : set) {
			subSet[index++] = fullInputSet[i];
		}
		return subSet;
	}

	private static float sum(float[] data) {
		float sum = 0;
		for (float f : data) {

			if (f != f)
				return Float.NaN;
			sum += f;
		}
		return sum;
	}

	private static float sum(Collection<Integer> useOnly, float[] data) {
		float sum = 0;
		for (int i : useOnly) {
			float f = data[i];
			if (f != f)
				return Float.NaN;
			sum += f;
		}
		return sum;
	}

	public static Set<Set<Integer>> powerSet(Set<Integer> originalSet) {
		Set<Set<Integer>> sets = new HashSet<Set<Integer>>();
		if (originalSet.isEmpty()) {
			sets.add(new HashSet<Integer>());
			return sets;
		}
		List<Integer> list = new ArrayList<Integer>(originalSet);
		Integer head = list.get(0);
		Set<Integer> rest = new HashSet<Integer>(list.subList(1, list.size()));
		for (Set<Integer> set : powerSet(rest)) {
			Set<Integer> newSet = new HashSet<Integer>();
			newSet.add(head);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}
		return sets;
	}

	private static float sum(float[] data, Collection<Integer> indexSkip) {
		float sum = 0;
		int i = 0;
		for (float f : data) {
			if (indexSkip.contains(i++))
				continue;

			if (f != f)
				return Float.NaN;
			sum += f;
		}
		return sum;
	}

	public final static String[] keysToUse = {//
	"200-Day Moving Average",// 5233
			"50-Day Moving Average",// 5233
			"52-Week Change",// 4924
			"52-Week High",// 5241
			"52-Week Low",// 5241
			"Avg Vol 10 day",// 5233
			"Avg Vol 3 month",// 5233
			"Beta",// 4545
			"Book Value Per Share mrq",// 5254
			"Diluted EPS ttm",// 5211
			"Enterprise Value",// 5188
			"Enterprise Value/Revenue ttm",// 5024
			// "Fiscal Year Ends",// 5215
			"Float",// 4755
			"Market Cap intraday",// 5219
			// "Most Recent Quarter mrq",// 5223
			"Net Income Avl to Common ttm",// 5215
			// "Operating Cash Flow ttm",// 4348
			"Operating Margin ttm",// 5056
			"Price/Book mrq",// 4948
			"Price/Sales ttm",// 4991
			"Profit Margin ttm",// 4821
			"Qtrly Revenue Growth yoy",// 4918
			"Return on Assets ttm",// 4925
			"Return on Equity ttm",// 4798
			"Revenue Per Share ttm",// 5019
			"Revenue ttm",// 5060
			"S P500 52-Week Change",// 5233
			"Shares Outstanding",// 5219
			"Shares Short",// 5088
			"Shares Short prior month",// 5074
			"Short Ratio",// 4786
			"Total Cash Per Share mrq",// 5054
			"Total Cash mrq",// 5076
			"Total Debt mrq",// 5208
	};
}
