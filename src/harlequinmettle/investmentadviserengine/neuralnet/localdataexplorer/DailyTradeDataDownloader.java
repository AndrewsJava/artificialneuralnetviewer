// Jan 17, 2016 11:43:29 AM
package harlequinmettle.investmentadviserengine.neuralnet.localdataexplorer;

import harlequinmettle.investmentadviserengine.servlet.actions.task.intraday.PriceVolTradeRecord;
import harlequinmettle.investmentadviserengine.util.CSVTool;
import harlequinmettle.investmentadviserengine.util.NetworkDownloadTool;
import harlequinmettle.utils.filetools.SerializationTool;

import java.io.File;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class DailyTradeDataDownloader extends IntradayTradeDataDownloader {

	public static void main(String[] args) {
		// new File("DailyTradeDataLocal").mkdir();
		downloadDailyTradeData();
	}

	private static void downloadDailyTradeData() {

		ConcurrentSkipListSet<String> tickers = getTickers("TickersQueueDailyTradeDataCollectionProgressObject");
		int counter = 0;

		for (String ticker : tickers) {

			storeDailyDataIfAvailable(ticker);
			System.out.println(ticker + " : " + counter);
			workingTickerSet.remove(ticker);
			SerializationTool.serializeObject(workingTickerSet, "TickersQueueDailyTradeDataCollectionProgressObject");
			// if (counter > 15)
			// break;
			counter++;
		}

	}

	private static void storeDailyDataIfAvailable(String ticker) {

		String technicalsText = getRESTfullDailyTechnicalData(ticker);

		if (technicalsText == null)
			return;

		TreeMap<Long, Float> volVal = CSVTool.getMultidayNumbersFromCSVString(technicalsText, 5);

		TreeMap<Long, Float> priceVal = CSVTool.getMultidayNumbersFromCSVString(technicalsText, 6);
		buildDailyTradeMapAndStoreInDatastoreForNN(ticker, volVal, priceVal);
	}

	private static ConcurrentSkipListMap<Long, PriceVolTradeRecord> buildDailyTradeMapAndStoreInDatastoreForNN(String ticker,
			TreeMap<Long, Float> volVal, TreeMap<Long, Float> priceVal) {
		ConcurrentSkipListMap<Long, PriceVolTradeRecord> dataMap = new ConcurrentSkipListMap<Long, PriceVolTradeRecord>();
		for (Entry<Long, Float> entry : volVal.entrySet()) {
			Long time = entry.getKey();
			Float vol = entry.getValue();
			Float price = priceVal.get(time);
			if (vol == null)
				continue;
			if (price == null)
				continue;
			if (vol.floatValue() != vol.floatValue())
				continue;
			if (price.floatValue() != price.floatValue())
				continue;
			PriceVolTradeRecord tradeData = new PriceVolTradeRecord(price, vol);
			dataMap.put(time, tradeData);
		}
		String uniqueTickerKey = getUniqueKeyFor(ticker);
		SerializationTool.serializeObject(dataMap, "DailyTradeDataLocal" + File.separator + uniqueTickerKey);
		// CORE.DATASTORE_BLOB_DB_KEY_NN_PER_TICKER_INTRADAY_TRADE_DATA_DATABASE.lockMergeMapWithDatastore(uniqueTickerKey,
		// dataMap);
		return dataMap;
	}

	private static String getRESTfullDailyTechnicalData(String ticker) {
		// Date Open High Low Close Volume Adj Close
		// 2016-01-12 13.03 13.04 12.58 12.85 48327400 12.85

		String nearTime = getUrlForDailyData(ticker);
		String csv = NetworkDownloadTool.getNetworkDownloadToolSingletonReference().getData(null, nearTime);
		return csv;
	}

	private static String getUrlForDailyData(String ticker) {
		return "http://real-chart.finance.yahoo.com/table.csv?s=" + ticker + "&g=d";
	}
}
