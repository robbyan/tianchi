import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import pojo.ItemRecord;
import au.com.bytecode.opencsv.CSVWriter;

public class SplitDataset {

	private static Set<String> set = new HashSet<String>();

	private static Map<String, Map<String, ItemRecord>> categoryBuyed = new HashMap<String, Map<String, ItemRecord>>();

	public static void loadUserIdBuySth(String filePath) throws Exception {
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		int count = 0;
		// header
		String s = br.readLine();
		String[] columns = s.split(",");
		while ((s = br.readLine()) != null) {
			count++;
			columns = s.split(",");
			String userId = columns[0];
			String behaviorType = columns[2];
			ItemRecord ir = new ItemRecord(columns);
			if (behaviorType.equals("4")) {
				set.add(userId);
				if (!categoryBuyed.containsKey(userId)) {
					categoryBuyed
							.put(userId, new HashMap<String, ItemRecord>());
				}
				ItemRecord currentItem = categoryBuyed.get(userId).get(
						ir.getItemCategory());
				if (currentItem == null
						|| currentItem.getDate().before(ir.getDate())) {
					categoryBuyed.get(userId).put(ir.getItemCategory(), ir);
				}
			}
		}
		fr.close();
		System.out.println("Total count: " + count);
	}

	private static void splitData(String srcFile, String trainFile,
			String testFile) throws Exception {

		// Map<String, List<ItemRecord>> sortedMap = new TreeMap<String,
		// List<ItemRecord>>();
		Map<String, List<ItemRecord>> sortedMap2 = new TreeMap<String, List<ItemRecord>>();
		// CSVWriter writer = null;
		// try {
		// writer = new CSVWriter(new FileWriter(trainFile));
		// } catch (IOException e) {
		// }
		CSVWriter writer2 = null;
		try {
			writer2 = new CSVWriter(new FileWriter(testFile));
		} catch (IOException e) {
		}
		FileReader fr = new FileReader(srcFile);
		BufferedReader br = new BufferedReader(fr);

		// header
		String s = br.readLine();
		String[] columns = s.split(",");
		// writer.writeNext(columns);
		writer2.writeNext(columns);
		int count1 = 0;
		int count2 = 0;
		while ((s = br.readLine()) != null) {
			columns = s.split(",");
			ItemRecord record = new ItemRecord(columns);
			String userId = columns[0];
			String itemCategory = columns[4];

			if (set.contains(userId)) {
				ItemRecord buyedItem = categoryBuyed.get(userId).get(
						itemCategory);
				// add to training set
				if (buyedItem != null
						&& !buyedItem.getDate().before(record.getDate())) {
					// if (!sortedMap.containsKey(userId)) {
					// sortedMap.put(userId, new ArrayList<ItemRecord>());
					// }
					// sortedMap.get(userId).add(record);
					// count1++;
				}
				// add to predict set
				else {
					writer2.writeNext(columns);
					// if (!sortedMap2.containsKey(userId)) {
					// sortedMap2.put(userId, new ArrayList<ItemRecord>());
					// }
					// sortedMap2.get(userId).add(record);
					// count2++;
				}

			} else {
				writer2.writeNext(columns);
				// if (!sortedMap2.containsKey(userId)) {
				// sortedMap2.put(userId, new ArrayList<ItemRecord>());
				// }
				// sortedMap2.get(userId).add(record);
				// count2++;
			}
		}
		fr.close();
		count1 = 0;
		count2 = 0;
		// for (Entry<String, List<ItemRecord>> entry : sortedMap.entrySet()) {
		// List<ItemRecord> list = entry.getValue();
		// for (ItemRecord row : list) {
		// writer.writeNext(new String[] { row.getUserId(),
		// row.getItemId(), row.getBehaviorType(),
		// row.getUserGeohash(), row.getItemCategory(),
		// row.getTime() });
		// count1++;
		// }
		// }
		// for (Entry<String, List<ItemRecord>> entry : sortedMap2.entrySet()) {
		// List<ItemRecord> list = entry.getValue();
		// for (ItemRecord row : list) {
		// writer2.writeNext(new String[] { row.getUserId(),
		// row.getItemId(), row.getBehaviorType(),
		// row.getUserGeohash(), row.getItemCategory(),
		// row.getTime() });
		// count2++;
		// }
		// }
		// writer.close();
		writer2.close();
		System.out.println("Training count: " + count1);
		System.out.println("Test count: " + count2);
		System.out.println(toMemoryInfo());
	}

	public static String toMemoryInfo() {

		Runtime currRuntime = Runtime.getRuntime();

		int nFreeMemory = (int) (currRuntime.freeMemory() / 1024 / 1024);

		int nTotalMemory = (int) (currRuntime.totalMemory() / 1024 / 1024);

		return nFreeMemory + "M/" + nTotalMemory + "M(free/total)";

	}

	public static void splitToTrainAndTest(String srcFile, String trainFile,
			String testFile) throws Exception {
		loadUserIdBuySth(srcFile);
		splitData(srcFile, trainFile, testFile);
	}
}
