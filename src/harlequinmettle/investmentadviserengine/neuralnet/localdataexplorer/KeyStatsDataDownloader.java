// Jan 14, 2016 8:20:53 AM
package harlequinmettle.investmentadviserengine.neuralnet.localdataexplorer;

import harlequinmettle.investmentadviserengine.datamanagers.DatabaseKeyStats;
import harlequinmettle.investmentadviserengine.datamanagers.TickerTimeEconomicIndicator;
import harlequinmettle.investmentadviserengine.util.NetworkDownloadTool;
import harlequinmettle.investmentadviserengine.util.NumberTool;
import harlequinmettle.investorengine.util.StringTool;
import harlequinmettle.utils.filetools.SerializationTool;
import harlequinmettle.utils.finance.updatedtickerset.DatabaseCore;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class KeyStatsDataDownloader {

	static ConcurrentSkipListMap<String, ConcurrentSkipListSet<TickerTimeEconomicIndicator>> databaseKeyStats = new ConcurrentSkipListMap<String, ConcurrentSkipListSet<TickerTimeEconomicIndicator>>();

	static ConcurrentSkipListSet<String> workingTickerSet;
	static long timeOf = System.currentTimeMillis();

	public static void main(String[] args) {
		// new File("KeyStatsDataLocal").mkdir();
		if (false)
			collectKeyStats();

		System.out.println((System.currentTimeMillis() - timeOf) / 1000);
	}

	private static void collectKeyStats() {
		int counter = 0;
		Collection<String> tickers = getTickers();

		for (String ticker : tickers) {

			CharSequence htmlData = getYahooKeyStatsDataFor(ticker);

			if (htmlData == null)
				continue;
			ConcurrentSkipListMap<String, Float> keystats = extractDataAsMap(htmlData.toString());

			System.out.println(ticker + " : " + counter);
			addDataToKeyStatsMaps(ticker, keystats);
			workingTickerSet.remove(ticker);
			if (counter++ > 1500)
				break;
		}

		for (Entry<String, ConcurrentSkipListSet<TickerTimeEconomicIndicator>> entry : databaseKeyStats.entrySet()) {
			String statLabel = entry.getKey();
			ConcurrentSkipListSet<TickerTimeEconomicIndicator> update = entry.getValue();
			ConcurrentSkipListSet<TickerTimeEconomicIndicator> existing = SerializationTool.deserializeObject(ConcurrentSkipListSet.class, statLabel);
			if (existing != null)
				update.addAll(existing);
			SerializationTool.serializeObject(update, "KeyStatsDataLocal" + File.separator + statLabel);
			// CORE.DATASTORE_BLOB_DB_KEY_STATS_DATABASE.lockMergeSetWithDatastore(statLabel,
			// entry.getValue());
		}

		SerializationTool.serializeObject(workingTickerSet, "tickersObject");
	}

	private static Collection<String> getTickers() {
		// Jan 14, 2016 10:19:28 AM
		workingTickerSet = SerializationTool.deserializeObject(ConcurrentSkipListSet.class, "tickersObject");
		if (workingTickerSet == null)
			workingTickerSet = new ConcurrentSkipListSet(DatabaseCore.getDataCoreSingleton().tickers.values());
		return workingTickerSet;
	}

	private static void addDataToKeyStatsMaps(String ticker, ConcurrentSkipListMap<String, Float> keystats) {
		for (Entry<String, Float> entry : keystats.entrySet()) {
			String statLabel = entry.getKey();
			float statValue = entry.getValue();
			TickerTimeEconomicIndicator indicator = new TickerTimeEconomicIndicator(ticker.hashCode(),
					(byte) DatabaseKeyStats.KEYS.indexOf(statLabel), statValue, timeOf);

			if (databaseKeyStats.containsKey(statLabel)) {
				ConcurrentSkipListSet<TickerTimeEconomicIndicator> currentData = databaseKeyStats.get(statLabel);
				currentData.add(indicator);
			} else {
				ConcurrentSkipListSet<TickerTimeEconomicIndicator> newData = new ConcurrentSkipListSet<TickerTimeEconomicIndicator>();
				newData.add(indicator);
				databaseKeyStats.put(statLabel, newData);
			}
		}

	}

	private static ConcurrentSkipListMap<String, Float> extractDataAsMap(String htmlData) {
		ConcurrentSkipListMap<String, Float> dataMap = new ConcurrentSkipListMap<String, Float>();
		if (htmlData.contains("Get Quotes Results for") || htmlData.contains("There are no results for the given search term")
				|| htmlData.contains("There is no Key Statistics data available for")) {

			return dataMap;
		}
		String extract = extractTable(htmlData);

		return buildTableFromExtractData(extract);
	}

	private static ConcurrentSkipListMap<String, Float> buildTableFromExtractData(String htmlTableText) {

		ConcurrentSkipListMap<String, Float> dataMap = new ConcurrentSkipListMap<String, Float>();
		if (htmlTableText == null)
			return dataMap;
		String[] trs = htmlTableText.split("<tr.*?>");
		for (String tr : trs) {
			String[] tds = tr.split("</td>");
			if (tds.length < 3) {
				continue;
			}

			String key = StringTool.replaceAllRegex(tds[0], "<.*?>", "");
			if (key.length() > 45)
				continue;
			key = key.replaceAll("\\(.*?,.*?\\)", "");
			key = StringTool.replaceAllRegex(key, ":", "");
			key = StringTool.replaceAllRegex(key, "\\)", "");
			key = StringTool.replaceAllRegex(key, "\\(", "");
			key = StringTool.replaceAllRegex(key, "\\s+", " ").trim();
			if (!KEYS.contains(key))
				continue;
			// keys.add(key);
			String value = StringTool.replaceAllRegex(tds[1], "<.*?>", "");
			float f = NumberTool.stringNumberWithMBKtoFloat(value);
			if (f != f) {
				continue;
			}
			float dataValue = f;
			dataMap.put(key, f);
		}
		return dataMap;
	}

	private static String extractTable(String htmlText) {

		// htmlText = htmlText.toLowerCase();

		htmlText = StringTool.removeJavascript(htmlText);
		htmlText = StringTool.clip(htmlText, "Get Key Statistics for:", "Currency in USD.", htmlText);
		htmlText = StringTool.replaceAllRegex(htmlText, "<font.*?>", "");
		htmlText = StringTool.replaceAllRegex(htmlText, "</font>", "");
		htmlText = StringTool.replaceAllRegex(htmlText, "<span.*?>", "");
		htmlText = StringTool.replaceAllRegex(htmlText, "</span>", "");
		htmlText = StringTool.replaceAllRegex(htmlText, "<div.*?>", "");
		htmlText = StringTool.replaceAllRegex(htmlText, "</div>", "");
		htmlText = StringTool.replaceAllRegex(htmlText, "<!--.*?-->", "");
		htmlText = StringTool.replaceAllRegex(htmlText, "<p.*?>", "");
		htmlText = StringTool.replaceAllRegex(htmlText, "</p>", "");
		htmlText = StringTool.replaceAllRegex(htmlText, "<a.*?>", "");
		htmlText = StringTool.replaceAllRegex(htmlText, "</a>", "");
		htmlText = StringTool.replaceAllRegex(htmlText, "<b.*?>", "");
		htmlText = StringTool.replaceAllRegex(htmlText, "</b>", "");

		htmlText = StringTool.replaceAllRegex(htmlText, "&.*?;", " ");
		htmlText = StringTool.replaceAllRegex(htmlText, "\\s+", " ");

		htmlText = StringTool.replaceAllRegex(htmlText, "<td.*?>", "<td>");

		htmlText = StringTool.replaceAllRegex(htmlText, "<sup.*?sup>", " ");
		htmlText = StringTool.replaceAllRegex(htmlText, "<tr.*?>", "<tr>");
		htmlText = StringTool.replaceAllRegex(htmlText, "\\$", "");
		return htmlText;
	}

	public final static String[] KEYS_TO_USE = {//
	"200-Day Moving Average",// 5233 //0
			"50-Day Moving Average",// 5233//1
			"52-Week Change",// 4924//2
			"52-Week High",// 5241//3
			"52-Week Low",// 5241//4
			"Avg Vol 10 day",// 5233//5
			"Avg Vol 3 month",// 5233//6
			"Beta",// 4545//7
			"Book Value Per Share mrq",// 5254//8
			"Diluted EPS ttm",// 5211//9
			"Enterprise Value",// 5188//10
			"Enterprise Value/Revenue ttm",// 5024//11
			// "Fiscal Year Ends",// 5215
			"Float",// 4755//12
			"Market Cap intraday",// 5219//13
			// "Most Recent Quarter mrq",// 5223
			"Net Income Avl to Common ttm",// 5215//14
			// "Operating Cash Flow ttm",// 4348
			"Operating Margin ttm",// 5056//15
			"Price/Book mrq",// 4948//16
			"Price/Sales ttm",// 4991//17
			"Profit Margin ttm",// 4821//18
			"Qtrly Revenue Growth yoy",// 4918//19
			"Return on Assets ttm",// 4925//20
			"Return on Equity ttm",// 4798//21
			"Revenue Per Share ttm",// 5019//22
			"Revenue ttm",// 5060//23
			// "S P500 52-Week Change",// 5233
			"Shares Outstanding",// 5219//24
			"Shares Short",// 5088//25
			"Shares Short prior month",// 5074//26
			"Short Ratio",// 4786//27
			"Total Cash Per Share mrq",// 5054//28
			"Total Cash mrq",// 5076//29
			"Total Debt mrq",// 5208//30
	};
	public static final ArrayList<String> KEYS = new ArrayList(Arrays.asList(KEYS_TO_USE));

	public static CharSequence getYahooKeyStatsDataFor(String ticker) {
		String url = "http://finance.yahoo.com/q/ks?s=" + ticker;
		return NetworkDownloadTool.getNetworkDownloadToolSingletonReference().getData(null, url);
	}
}
