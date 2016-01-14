import harlequinmettle.investmentadviserengine.util.NetworkDownloadTool;
import harlequinmettle.investmentadviserengine.util.NumberTool;
import harlequinmettle.utils.debugtools.InstanceCounter;
import harlequinmettle.utils.filetools.FileTools;
import harlequinmettle.utils.filetools.SerializationTool;
import harlequinmettle.utils.finance.updatedtickerset.DatabaseCore;
import harlequinmettle.utils.stringtools.StringTool;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

// Nov 8, 2015 12:13:03 PM

public class KeyStatsDatabaseDownloadAndTableExtractor {
	public static void main(String[] args) {
		// Oct 29, 2015 9:27:19 AM

		// new KeyStatsDatabaseBuilder().downloadAll(1500);

		new KeyStatsDatabaseBuilder().extractTableData();
		extractDataFromTables();
		buildDatabaseFromExtractedTables();
		// testDatabaseValues();
		buildInputDataFromKeyStats();

	}

	static String alternateFiles = "/home/andrew/Desktop/programming";

	static TreeMap<String, HashMap<String, Float>> database = new TreeMap<String, HashMap<String, Float>>();
	static TreeMap<String, float[]> inputs = new TreeMap<String, float[]>();
	static TreeMap<String, int[]> inputsQuality = new TreeMap<String, int[]>();

	private static void buildInputDataFromKeyStats() {
		database = SerializationTool.deserializeObject(database.getClass(), "obj_database_key_stats");

		int success = 0;
		for (Entry<String, HashMap<String, Float>> ent : database.entrySet()) {
			float[] input = new float[KeyStatsDatabaseBuilder.keysToUse.length];
			Arrays.fill(input, Float.NaN);
			String ticker = ent.getKey();
			HashMap<String, Float> data = ent.getValue();
			int i = 0;
			for (String key : KeyStatsDatabaseBuilder.keysToUse) {
				Float dataPoint = data.get(key);
				if (dataPoint == null)
					i++;
				else
					input[i++] = data.get(key);
			}
			float sum = 0;
			for (float f : input)
				sum += f;
			if (sum == sum)
				success++;
			inputs.put(ticker, input);
		}
		int i = 0;
		for (String key : KeyStatsDatabaseBuilder.keysToUse)
			System.out.println("\"" + key + "\",//      " + i++);
		System.out.println(success);
		System.out.println(inputs.size());

		SerializationTool.serializeObject(inputs, "obj_database_inputs_key_stats");
	}

	// Nov 7, 2015 9:24:33 AM
	private static void testDatabaseValues() {
		database = SerializationTool.deserializeObject(database.getClass(), "obj_database_key_stats");
		InstanceCounter dataQuantitycounter = new InstanceCounter();
		InstanceCounter dataQuaalitycounter = new InstanceCounter();
		for (Entry<String, HashMap<String, Float>> ent : database.entrySet()) {
			String ticker = ent.getKey();
			HashMap<String, Float> data = ent.getValue();
			dataQuantitycounter.add(data.size());
			for (Entry<String, Float> keystats : data.entrySet()) {
				dataQuantitycounter.add(keystats.getKey());
				dataQuaalitycounter.add(keystats.getValue());
			}
		}

		for (Entry<String, Integer> ent : dataQuantitycounter.STRING_COUNTER.entrySet()) {
			System.out.println("\"" + ent.getKey() + "\",//      " + ent.getValue());
		}
		System.out.println();
		System.out.println();
		System.out.println(" NaN " + dataQuaalitycounter.FLOAT_COUNTER.get(Float.NaN));
		System.out.println(" zero " + dataQuaalitycounter.FLOAT_COUNTER.get(0f));
		System.out.println(" +inf " + dataQuaalitycounter.FLOAT_COUNTER.get(Float.POSITIVE_INFINITY));
		System.out.println(" -inf " + dataQuaalitycounter.FLOAT_COUNTER.get(Float.NEGATIVE_INFINITY));
		// for (Entry<Float, Integer> ent :
		// dataQuaalitycounter.FLOAT_COUNTER.entrySet()) {
		// System.out.println(ent.getValue() +
		// "                                   " + ent.getKey());
		// }
	}

	// Nov 7, 2015 8:59:55 AM
	protected static void buildDatabaseFromExtractedTables() {

		DatabaseCore tickerData = DatabaseCore.getDataCoreSingleton();
		TreeSet<String> keys = new TreeSet<String>();
		int counter = 0;
		for (String ticker : tickerData.tickers.values()) {
			System.out.println(counter++ + "    " + ticker);
			File keyStatsFileExtract = new File(alternateFiles, "extracted" + File.separatorChar + ticker + "tables.html");
			String htmlTale = FileTools.tryToReadFileToString(keyStatsFileExtract, null);
			if (htmlTale == null)
				continue;
			String[] trs = htmlTale.split("<tr.*?>");
			for (String tr : trs) {
				String[] tds = tr.split("</td>");
				if (tds.length < 3) {
					continue;
				}
				String key = StringTool.replaceAllRegex(tds[0], "<.*?>", "");
				if (key.length() > 45)
					continue;
				key = key.replaceAll("\\(.*?,.*?\\)", "");
				key = StringTool.replaceAllRegex(key, ":", "").trim();
				key = StringTool.replaceAllRegex(key, "\\)", "").trim();
				key = StringTool.replaceAllRegex(key, "\\(", "").trim();
				key = StringTool.replaceAllRegex(key, "\\s+", " ").trim();
				keys.add(key);
				String value = StringTool.replaceAllRegex(tds[1], "<.*?>", "");
				float f = NumberTool.stringNumberWithMBKtoFloat(value);
				if (f != f) {
					continue;
				}
				float dataValue = (float) (NumberTool.roundToSignificantFigures3(f, f));
				addData(ticker, key, dataValue);
			}
		}
		System.out.println(keys);
		SerializationTool.serializeObject(database, "obj_database_key_stats");
	}

	private static void addData(String ticker, String key, float value) {
		// Nov 7, 2015 9:18:10 AM
		HashMap<String, Float> keyStats = database.get(ticker);
		if (keyStats == null)
			keyStats = new HashMap<String, Float>();
		keyStats.put(key, value);
		database.put(ticker, keyStats);
		// System.out.println(ticker + "  :    " + key +
		// "                          " + value);
	}

	protected static void extractDataFromTables() {
		int tdsuccess = 0;
		int tdfail = 0;
		TreeSet<String> keys = new TreeSet<String>();
		TreeSet<Float> values = new TreeSet<Float>();
		TreeSet<String> failures = new TreeSet<String>();
		TreeSet<String> numberfailures = new TreeSet<String>();
		DatabaseCore tickerData = DatabaseCore.getDataCoreSingleton();
		int counter = 0;
		for (String ticker : tickerData.tickers.values()) {
			System.out.println(counter++ + "    " + ticker);
			File keyStatsFileExtract = new File(alternateFiles, "extracted" + File.separatorChar + ticker + "tables.html");
			String htmlTale = FileTools.tryToReadFileToString(keyStatsFileExtract, null);
			if (htmlTale == null)
				continue;
			String[] trs = htmlTale.split("<tr.*?>");
			for (String tr : trs) {
				String[] tds = tr.split("</td>");
				if (tds.length < 3) {
					tdfail++;
					failures.add(StringTool.replaceAllRegex(Arrays.toString(tds), "<.*?>", ""));
					continue;
				}
				String key = StringTool.replaceAllRegex(tds[0], "<.*?>", "");
				key = StringTool.replaceAllRegex(key, ":", "").trim();
				if (key.length() > 45)
					continue;
				key = key.replaceAll("\\(.*?,.*?\\)", "");
				String value = StringTool.replaceAllRegex(tds[1], "<.*?>", "");
				float f = NumberTool.stringNumberWithMBKtoFloat(value);
				if (f != f) {
					numberfailures.add(value);
					continue;
				}
				keys.add(key);
				values.add(NumberTool.round2(f));
				tdsuccess++;
			}
		}
		FileTools.tryToWriteCollectionToFileLines(new File(alternateFiles, "dataextract_keys.txt"), keys);
		FileTools.tryToWriteCollectionToFileLines(new File(alternateFiles, "dataextract_values.txt"), values);
		FileTools.tryToWriteCollectionToFileLines(new File(alternateFiles, "dataextract_failures.txt"), failures);
		FileTools.tryToWriteCollectionToFileLines(new File(alternateFiles, "dataextract_failuresnumber.txt"), numberfailures);
		System.out.println(" success " + tdsuccess);
		System.out.println("     fail " + tdfail);
	}

	// Oct 29, 2015 10:42:43 AM
	protected void extractTableData() {
		DatabaseCore tickerData = DatabaseCore.getDataCoreSingleton();
		int counter = 0;
		for (String ticker : tickerData.tickers.values()) {
			System.out.println(counter++ + "    " + ticker);
			File keyStatsFile = new File(alternateFiles, "pages" + File.separatorChar + ticker + "_key_stats.html");
			if (!keyStatsFile.exists())
				continue;
			File keyStatsFileExtract = new File(alternateFiles, "extracted" + File.separatorChar + ticker + "tables.html");
			String fileText = FileTools.tryToReadFileToString(keyStatsFile, " ");
			if (fileText.contains("Get Quotes Results for") || fileText.contains("There are no results for the given search term")
					|| fileText.contains("There is no Key Statistics data available for")) {
				// keyStatsFile.delete();
				continue;
			}
			String extract = extractTable(fileText);

			FileTools.tryToWriteStringToFile(keyStatsFileExtract, extract);
		}
	}

	private String extractTable(String htmlText) {

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

	// Oct 29, 2015 9:27:42 AM
	protected void downloadAll(int batch) {
		DatabaseCore tickerData = DatabaseCore.getDataCoreSingleton();
		int counter = 0;
		for (String ticker : tickerData.tickers.values()) {
			System.out.println(counter++ + "    " + ticker);
			File keyStatsFile = new File(alternateFiles, "pages" + File.separatorChar + ticker + "_key_stats.html");
			if (keyStatsFile.exists())
				continue;
			// if (counter > batch)
			// return;
			String url = "http://finance.yahoo.com/q/ks?s=" + ticker;
			System.out.println(url);
			ArrayList<String> lines = NetworkDownloadTool.getNetworkDownloadToolSingletonReference().getLines(url);

			FileTools.tryToWriteCollectionToFileLines(keyStatsFile, lines);
		}
	}

	// "% Held by Insiders",// 4611
	// "% Held by Institutions",// 4624
	// "5 Year Average Dividend Yield",// 8
	// "Current Ratio mrq",// 4623
	// "Dividend Date",// 3021
	// "EBITDA ttm",// 4148
	// "Enterprise Value/EBITDA ttm",// 4125
	// "Ex-Dividend Date",// 3046
	// "Forward Annual Dividend Rate",// 2618
	// "Forward Annual Dividend Yield",// 2618
	// "Forward P/E",// 3174
	// "Forward P/E fye",// 2
	// "Gross Profit ttm",// 4190
	// "Last Split Date",// 2293
	// "Last Split Factor new per old",// 2293
	// "Levered Free Cash Flow ttm",// 3893
	// "PEG Ratio 5 yr expected",// 3366
	// "Payout Ratio",// 1978
	// "Qtrly Earnings Growth yoy",// 3179
	// "Short % of Float",// 4216
	// "Total Debt/Equity mrq",// 3709
	// "Trailing Annual Dividend Yield",// 2738
	// "Trailing P/E",// 3595
}
