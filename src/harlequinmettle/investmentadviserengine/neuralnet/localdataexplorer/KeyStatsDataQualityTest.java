// Jan 15, 2016 12:19:18 PM
package harlequinmettle.investmentadviserengine.neuralnet.localdataexplorer;

import harlequinmettle.investmentadviserengine.datamanagers.TickerTimeEconomicIndicator;
import harlequinmettle.investmentadviserengine.util.ConcurrentIncrementingHackMap;
import harlequinmettle.utils.filetools.SerializationTool;
import harlequinmettle.utils.finance.updatedtickerset.DatabaseCore;

import java.io.File;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class KeyStatsDataQualityTest {

	public static void main(String[] args) {
		testKeyStatsDataQuality();
	}

	private static void testKeyStatsDataQuality() {
		ConcurrentIncrementingHackMap dataQuality = new ConcurrentIncrementingHackMap();
		ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, Float>> allData = constructEmptyMapsWithNan();
		for (String indicator : KeyStatsDataDownloader.KEYS) {
			ConcurrentSkipListSet<TickerTimeEconomicIndicator> data = SerializationTool.deserializeObject(ConcurrentSkipListSet.class,
					"KeyStatsDataLocal" + File.separator + indicator);
			if (data == null)
				continue;
			int validData = 0;
			for (TickerTimeEconomicIndicator dataPoint : data) {
				if (dataPoint.indicatorValue != dataPoint.indicatorValue)
					continue;
				String ticker = DatabaseCore.getDataCoreSingleton().tickers.get(dataPoint.tickerHash);
				allData.get(ticker).put(indicator, dataPoint.indicatorValue);
				dataQuality.countString("" + dataPoint.tickerHash);
				validData++;
			}
			System.out.println(System.lineSeparator() + indicator);
			System.out.println(data.size() + " : " + validData);
		}
		countDataVectorQuality(allData);
		// for (Entry<String, Integer> ent :
		// dataQuality.countingStringMap.entrySet())
		// System.out.println(ent.getKey() + "  : " + ent.getValue());
	}

	private static void countDataVectorQuality(ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, Float>> allData) {
		// Jan 15, 2016 1:50:31 PM
		int success = 0;
		int[] indexSkip = { 2, 7, 16, 17, 18, 19, 20, 21, 27 };
		for (Entry<String, ConcurrentSkipListMap<String, Float>> ent : allData.entrySet()) {
			int index = 0;
			boolean completeData = true;
			for (Entry<String, Float> entry : ent.getValue().entrySet()) {
				float val = entry.getValue();
				for (int skip : indexSkip)
					if (index == skip) {
						val = Float.NaN;
						// System.out.println(entry.getKey());
					}

				index++;
				if (val != val)
					completeData = false;
			}
			if (completeData)
				success++;
		}
		System.out.println("data complete: " + success);
	}

	private static ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, Float>> constructEmptyMapsWithNan() {
		ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, Float>> allData = new ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, Float>>();

		ConcurrentSkipListSet<String> workingTickerSet = new ConcurrentSkipListSet(DatabaseCore.getDataCoreSingleton().tickers.values());
		for (String ticker : workingTickerSet) {
			ConcurrentSkipListMap<String, Float> tickersData = new ConcurrentSkipListMap<String, Float>();
			for (String indicator : KeyStatsDataDownloader.KEYS) {
				tickersData.put(indicator, Float.NaN);
			}
			allData.put(ticker, tickersData);
		}
		return allData;
	}
}
