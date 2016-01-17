// Jan 15, 2016 9:24:34 AM
package harlequinmettle.investmentadviserengine.neuralnet.localdataexplorer;

import harlequinmettle.investmentadviserengine.servlet.actions.task.intraday.PriceVolTradeRecord;
import harlequinmettle.investmentadviserengine.util.CSVTool;
import harlequinmettle.investmentadviserengine.util.NetworkDownloadTool;
import harlequinmettle.utils.filetools.SerializationTool;
import harlequinmettle.utils.finance.updatedtickerset.DatabaseCore;

import java.io.File;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class IntradayTradeDataDownloader {

	static ConcurrentSkipListSet<String> workingTickerSet;

	public static void main(String[] args) {
		// new File("IntradayTradeDataLocal").mkdir();
		downloadIntradayTradeData();
	}

	private static void downloadIntradayTradeData() {

		ConcurrentSkipListSet<String> tickers = getTickers("TickersQueueIntradayTradeDataCollectionProgressObject");
		int counter = 0;

		for (String ticker : tickers) {

			storeDetailsIfAvailable(ticker);
			System.out.println(ticker + " : " + counter);
			workingTickerSet.remove(ticker);
			SerializationTool.serializeObject(workingTickerSet, "TickersQueueIntradayTradeDataCollectionProgressObject");
			// if (counter++ > 1500)
			// break;
		}

	}

	public static String getRESTfullIntraDayTechnicalData(String ticker) {
		// 1408109645,576.1398,576.2000,575.7000,575.7490,4400
		String nearTime = getUrlForMinuteData(ticker);
		String csv = NetworkDownloadTool.getNetworkDownloadToolSingletonReference().getData(null, nearTime);
		return csv;

	}

	public static String getUrlForMinuteData(String ticker) {

		return "http://chartapi.finance.yahoo.com/instrument/1.0/" + ticker + "/chartdata;type=quote;range=1d/csv";

	}

	public static ConcurrentSkipListMap<Long, PriceVolTradeRecord> storeDetailsIfAvailable(String ticker) {

		String technicalsText = getRESTfullIntraDayTechnicalData(ticker);

		if (technicalsText == null)
			return new ConcurrentSkipListMap<Long, PriceVolTradeRecord>();

		TreeMap<Long, Float> volVal = CSVTool.getIntradayNumbersFromCSVString_milliseconds(technicalsText, 5);

		TreeMap<Long, Float> priceVal = CSVTool.getIntradayNumbersFromCSVString_milliseconds(technicalsText, 1);
		return buildIntradayTradeMapAndStoreInDatastoreForNN(ticker, volVal, priceVal);
	}

	static ConcurrentSkipListMap<Long, PriceVolTradeRecord> buildIntradayTradeMapAndStoreInDatastoreForNN(String ticker, TreeMap<Long, Float> volVal,
			TreeMap<Long, Float> priceVal) {
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
		SerializationTool.serializeObject(dataMap, "IntradayTradeDataLocal" + File.separator + uniqueTickerKey);
		// CORE.DATASTORE_BLOB_DB_KEY_NN_PER_TICKER_INTRADAY_TRADE_DATA_DATABASE.lockMergeMapWithDatastore(uniqueTickerKey,
		// dataMap);
		return dataMap;
	}

	public static String getUniqueKeyFor(String ticker) {
		// Nov 28, 2015 10:15:20 AM
		return ticker + "_" + "intradaypricevolumedatamap" + "_";

	}

	protected static ConcurrentSkipListSet<String> getTickers(String queueObjectName) {
		workingTickerSet = SerializationTool.deserializeObject(ConcurrentSkipListSet.class, queueObjectName);
		if (workingTickerSet == null)
			workingTickerSet = new ConcurrentSkipListSet(DatabaseCore.getDataCoreSingleton().tickers.values());
		return workingTickerSet;
	}
}
