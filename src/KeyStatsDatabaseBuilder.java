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
		TreeSet<Integer> useKeys = new TreeSet<Integer>();
		TreeMap<Double, Set<Integer>> optimum = new TreeMap<Double, Set<Integer>>();
		for (int i = 0; i < keysToUse.length; i++) {
			useKeys.add(i);
		}
		Set<Set<Integer>> powerSet = new TreeSet<Set<Integer>>();
		buildComboSet(powerSet, useKeys, 10);
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

	private static void buildComboSet(Set<Set<Integer>> powerSet, TreeSet<Integer> useKeys, int i) {
		// Nov 8, 2015 12:09:22 PM

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
}
