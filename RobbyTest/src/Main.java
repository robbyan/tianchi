import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pojo.ItemComparator;
import pojo.ItemRecord;
import pojo.UserRecord;
import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.base.Joiner;

public class Main {

	public static void main(String args[]) throws Exception {
//		dailyBuyedItems();
//		splitToTrainAndTestData();
//		 splitData(100);
//		 refineData();
//		 calculateConvertRatio();
//		 predict();
		finalResult();
	}
	
	public static void finalResult() throws Exception{
	
		String srcFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\predictData_after_2014_12_12.csv";
		String targetFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\final_result.csv";
		
		FileReader fr = new FileReader(srcFile);
		BufferedReader br = new BufferedReader(fr);
		String s = "";
		String[] columns = s.split(",");
		Map<String, List<String>> resultMap = new HashMap<>();
		while ((s = br.readLine()) != null) {
			columns = s.replaceAll("\"", "").split(",");
			String userId= columns[0];
			String itemId= columns[1];
			if(!resultMap.containsKey(userId)){
				resultMap.put(userId, new ArrayList<String>());
			}
			resultMap.get(userId).add(itemId);
		}
		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new FileWriter(targetFile));
		} catch (IOException e) {
		}
		for(Entry<String, List<String>> entry:resultMap.entrySet()){
			writer.writeNext(new String[]{entry.getKey(),Joiner.on(",").join(entry.getValue())});
		}
		writer.close();
	}
	
	public static void predict() throws Exception {
		String testFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\testData.csv";
		String predictFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\predictData.csv";
		Map<String, double[]> ratioMap=calculateConvertRatio();
		Map<String, Map<String, List<ItemRecord>>> map = DataLoader.loadData(testFile, Arrays.asList("3"));
		
		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new FileWriter(predictFile));
		} catch (IOException e) {
		}
		for(Entry<String, Map<String, List<ItemRecord>>> entry:map.entrySet()){
			String userId = entry.getKey();
			for(Entry<String, List<ItemRecord>> categoryEntry:entry.getValue().entrySet()){
				List<ItemRecord> list= categoryEntry.getValue();
				if(!list.isEmpty()){
					ItemRecord itemRecord = list.get(list.size()-1);
					if(ratioMap.containsKey(userId)){
						writer.writeNext(new String[]{userId,itemRecord.getItemId(),itemRecord.getTime(),ratioMap.get(userId)[0]+"",ratioMap.get(userId)[1]+""});
					}else{
						writer.writeNext(new String[]{userId,itemRecord.getItemId(),itemRecord.getTime(),"NAN","NAN"});
					}
				}
			}
			
		}
		writer.close();
	}

	public static Map<String, double[]> calculateConvertRatio() throws Exception {
		String srcFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\refined_Cart_Purchase.csv";
		String targetFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\convert_ratio.csv";
		Map<String, Map<String, List<ItemRecord>>> map = DataLoader.loadData(srcFile);
		List<UserRecord> list = DataLoader.convertMapToList(map);

		Map<String, double[]> ratioMap = new HashMap<String, double[]>();
		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new FileWriter(targetFile));
		} catch (IOException e) {
		}
	
		for (UserRecord ur : list) {
			writer.writeNext(new String[]{ur.getUserId(),ur.getConvertRatio()+"",ur.getConvertRatioByLatestCart()+""});
			ratioMap.put(ur.getUserId(), new double[]{ur.getConvertRatio(),ur.getConvertRatioByLatestCart()});
			System.out.println("User Id: " + ur.getUserId());
			System.out.println("	Convert Ratio: " + ur.getConvertRatio());
			System.out.println("	Convert Ratio of item in cart: " + ur.getConvertRatioByLatestCart());
		}
		writer.close();
		return ratioMap;
	}

	public static void refineData() throws Exception {
		String srcFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\trainData.csv";
		String targetFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\refined_Cart_Purchase.csv";
		DataLoader.refineDataFromBehavior(srcFile, Arrays.asList("3", "4"), targetFile);
	}

	public static void splitData(int numberOfUsers) throws Exception {
		String srcFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\tianchi_mobile_recommend_train_user.csv";
		Map<String, Map<String, List<ItemRecord>>> map = DataLoader.loadData(srcFile, true);
		String targetFilePrefix = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\data";
		int userCount = map.size();
		int fileCount = Math.round(userCount / 100) + 1;
		for (int i = 0; i < fileCount; i++) {
			String file = targetFilePrefix + i + ".csv";
			Map subMap = new HashMap<String, List<ItemRecord>>();
			while (!map.isEmpty() && subMap.size() < 100) {
				String key = map.keySet().iterator().next();
				Map<String, List<ItemRecord>> value = map.remove(key);
				subMap.put(key, value);
			}
			System.out.println(subMap.keySet());
			// DataLoader.writeFile(subMap, file);
		}

	}
	
	public static void dailyBuyedItems() throws Exception {
		
		String srcFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\refined_Cart_Purchase.csv";
		String targetFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\dailySales.csv";
		Map<String, Map<String, List<ItemRecord>>> map = DataLoader.loadData(srcFile, false);
		
		Map<String,List<ItemRecord>> dayMap = new HashMap<String,List<ItemRecord>>();
		for (Entry<String, Map<String, List<ItemRecord>>> entry : map.entrySet()) {
			String userId = entry.getKey();
			Map<String, List<ItemRecord>> categoryMap = entry.getValue();

			for (Entry<String, List<ItemRecord>> categoryEntry : categoryMap.entrySet()) {
				String categoryId = categoryEntry.getKey();
				List<ItemRecord> list = categoryEntry.getValue();
				
				for(ItemRecord itemRecord:list){
					String date = itemRecord.getDateAsString();
					if(!dayMap.containsKey(date)){
						dayMap.put(date, new ArrayList<ItemRecord>());
					}
					dayMap.get(date).add(itemRecord);	
				}
			}
		}
		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new FileWriter(targetFile));
		} catch (IOException e) {
		}
		for (Entry<String,List<ItemRecord>> entry : dayMap.entrySet()) {
			writer.writeNext(new String[]{entry.getKey(),entry.getValue().size()+""});
		}
		writer.close();
	}

public static void mostPopularItemsByCategory() throws Exception {
		
		String srcFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\tianchi_mobile_recommend_train_user.csv";
		String targetFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\dailySales.csv";
		Map<String, Map<String, List<ItemRecord>>> map = DataLoader.loadData(srcFile, true);
		
//		Map<String,Map<String, String>> dayMap = new HashMap<String,Map<String, String>>();
//		for (Entry<String, Map<String, List<ItemRecord>>> entry : map.entrySet()) {
//			String userId = entry.getKey();
//			Map<String, List<ItemRecord>> categoryMap = entry.getValue();
//
//			for (Entry<String, List<ItemRecord>> categoryEntry : categoryMap.entrySet()) {
//				String categoryId = categoryEntry.getKey();
//				List<ItemRecord> list = categoryEntry.getValue();
//				if(!dayMap.containsKey(categoryId)){
//					dayMap.put(categoryId, new ArrayList<ItemRecord>());
//				}
//				for(ItemRecord itemRecord:list){
//					
//					dayMap.get(categoryId).add(itemRecord);	
//				}
//			}
//		}
//		CSVWriter writer = null;
//		try {
//			writer = new CSVWriter(new FileWriter(targetFile));
//		} catch (IOException e) {
//		}
//		for (Entry<String,List<ItemRecord>> entry : dayMap.entrySet()) {
//			writer.writeNext(new String[]{entry.getKey(),entry.getValue().size()+""});
//		}
		
	}

	public static void splitToTrainAndTestData() throws Exception {
		String srcFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\tianchi_mobile_recommend_train_user.csv";
		String originTrainFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\originTrainData.csv";
		String originTestFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\originTestData.csv";
		SplitDataset.splitToTrainAndTest(srcFile, originTrainFile, originTestFile);
		
		String trainFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\trainData.csv";
		String testFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\testData.csv";
		Map<String, Map<String, List<ItemRecord>>> map = DataLoader.loadData(srcFile, true);
		Map<String, Map<String, List<ItemRecord>>> subMap = DataLoader.loadData(originTrainFile);

		for (Entry<String, Map<String, List<ItemRecord>>> entry : subMap.entrySet()) {
			String userId = entry.getKey();
			Map<String, List<ItemRecord>> categoryMap = entry.getValue();

			for (Entry<String, List<ItemRecord>> categoryEntry : categoryMap.entrySet()) {
				String categoryId = categoryEntry.getKey();
				List<ItemRecord> list = categoryEntry.getValue();
				Collections.sort(list, new ItemComparator());
				// remove the data where is not purchase from the training set
				for (int i = list.size() - 1; i > 0; i--) {
					if (!list.get(i).getBehaviorType().equals("4")) {
						list.remove(i);
					}else{
						break;
					}
				}
			}
		}

		DataLoader.writeFile(subMap, trainFile);

		for (Entry<String, Map<String, List<ItemRecord>>> entry : map.entrySet()) {
			String userId = entry.getKey();
			Map<String, List<ItemRecord>> categoryMap = entry.getValue();

			for (Entry<String, List<ItemRecord>> categoryEntry : categoryMap.entrySet()) {
				String categoryId = categoryEntry.getKey();
				List<ItemRecord> list = categoryEntry.getValue();

				List<ItemRecord> trainList = subMap.get(userId)==null?null:subMap.get(userId).get(categoryId);
				if (trainList != null) {

					list.removeAll(trainList);
				}

				Collections.sort(list, new ItemComparator());
			}
		}

		DataLoader.writeFile(map, testFile);
	}

}