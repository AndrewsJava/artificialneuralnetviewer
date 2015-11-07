import harlequinmettle.investmentadviserengine.util.NetworkDownloadTool;
import harlequinmettle.investmentadviserengine.util.NumberTool;
import harlequinmettle.utils.debugtools.InstanceCounter;
import harlequinmettle.utils.filetools.FileTools;
import harlequinmettle.utils.filetools.SerializationTool;
import harlequinmettle.utils.finance.updatedtickerset.DatabaseCore;
import harlequinmettle.utils.stringtools.StringTools;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

// Oct 29, 2015 9:27:19 AM

public class KeyStatsDatabaseBuilder {

	static String alternateFiles = "/home/andrew/Desktop/programming";

	static TreeMap<String, HashMap<String, Float>> database = new TreeMap<String, HashMap<String, Float>>();

	public static void main(String[] args) {
		// Oct 29, 2015 9:27:19 AM

		// new DownloadKeyStats().downloadAll(1500);

		// new DownloadKeyStats().extractTableData();
		// extractDataFromTables();
		buildDatabaseFromExtractedTables();
		testDatabaseValues();
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
		for (Entry<Float, Integer> ent : dataQuantitycounter.FLOAT_COUNTER.entrySet()) {
			System.out.println(ent.getKey() + "      " + ent.getValue());
		}
		for (Entry<String, Integer> ent : dataQuantitycounter.STRING_COUNTER.entrySet()) {
			System.out.println(ent.getKey() + "      " + ent.getValue());
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
	private static void buildDatabaseFromExtractedTables() {

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
					continue;
				}
				String key = StringTools.replaceAllRegex(tds[0], "<.*?>", "");
				if (key.length() > 45)
					continue;
				key = key.replaceAll("\\(.*?,.*?\\)", "");
				String value = StringTools.replaceAllRegex(tds[1], "<.*?>", "");
				float f = NumberTool.stringNumberWithMBKtoFloat(value);
				if (f != f) {
					continue;
				}
				float dataValue = (float) (NumberTool.roundToSignificantFigures2(f, f));
				addData(ticker, key, dataValue);
			}
		}

		SerializationTool.serializeObject(database, "obj_database_key_stats");
	}

	private static void addData(String ticker, String key, float value) {
		// Nov 7, 2015 9:18:10 AM
		HashMap<String, Float> keyStats = database.get(ticker);
		if (keyStats == null)
			keyStats = new HashMap<String, Float>();
		keyStats.put(key, value);
		database.put(ticker, keyStats);

	}

	private static void extractDataFromTables() {
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
					failures.add(StringTools.replaceAllRegex(Arrays.toString(tds), "<.*?>", ""));
					continue;
				}
				String key = StringTools.replaceAllRegex(tds[0], "<.*?>", "");
				if (key.length() > 45)
					continue;
				key = key.replaceAll("\\(.*?,.*?\\)", "");
				String value = StringTools.replaceAllRegex(tds[1], "<.*?>", "");
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
	private void extractTableData() {
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

		htmlText = StringTools.removeJavascript(htmlText);
		htmlText = StringTools.clip(htmlText, "Get Key Statistics for:", "Currency in USD.", htmlText);
		htmlText = StringTools.replaceAllRegex(htmlText, "<font.*?>", "");
		htmlText = StringTools.replaceAllRegex(htmlText, "</font>", "");
		htmlText = StringTools.replaceAllRegex(htmlText, "<span.*?>", "");
		htmlText = StringTools.replaceAllRegex(htmlText, "</span>", "");
		htmlText = StringTools.replaceAllRegex(htmlText, "<div.*?>", "");
		htmlText = StringTools.replaceAllRegex(htmlText, "</div>", "");
		htmlText = StringTools.replaceAllRegex(htmlText, "<!--.*?-->", "");
		htmlText = StringTools.replaceAllRegex(htmlText, "<p.*?>", "");
		htmlText = StringTools.replaceAllRegex(htmlText, "</p>", "");
		htmlText = StringTools.replaceAllRegex(htmlText, "<a.*?>", "");
		htmlText = StringTools.replaceAllRegex(htmlText, "</a>", "");
		htmlText = StringTools.replaceAllRegex(htmlText, "<b.*?>", "");
		htmlText = StringTools.replaceAllRegex(htmlText, "</b>", "");

		htmlText = StringTools.replaceAllRegex(htmlText, "&.*?;", " ");
		htmlText = StringTools.replaceAllRegex(htmlText, "\\s+", " ");

		htmlText = StringTools.replaceAllRegex(htmlText, "<td.*?>", "<td>");

		htmlText = StringTools.replaceAllRegex(htmlText, "<sup.*?sup>", " ");
		htmlText = StringTools.replaceAllRegex(htmlText, "<tr.*?>", "<tr>");
		htmlText = StringTools.replaceAllRegex(htmlText, "\\$", "");
		return htmlText;
	}

	// Oct 29, 2015 9:27:42 AM
	private void downloadAll(int batch) {
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
}
