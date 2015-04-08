import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pojo.CategoryRecord;
import pojo.ItemComparator;
import pojo.ItemRecord;
import pojo.UserRecord;
import au.com.bytecode.opencsv.CSVWriter;

public class DataLoader {
	
	public static Map<String, Map<String, List<ItemRecord>>> loadData(String srcFile) throws Exception {
		Map<String, Map<String, List<ItemRecord>>> map = new HashMap();
		FileReader fr = new FileReader(srcFile);
		BufferedReader br = new BufferedReader(fr);
		String s = "";
		String[] columns = s.split(",");
		while ((s = br.readLine()) != null) {
			columns = s.replaceAll("\"", "").split(",");
			ItemRecord record = new ItemRecord(columns);
			if (!map.containsKey(record.getUserId())) {
				map.put(record.getUserId(), new HashMap<String, List<ItemRecord>>());
			}
			if(!map.get(record.getUserId()).containsKey(record.getItemCategory())){
				map.get(record.getUserId()).put(record.getItemCategory(), new ArrayList<ItemRecord>());
			}
			map.get(record.getUserId()).get(record.getItemCategory()).add(record);
		}
		return map;
	}
	
	public static Map<String, Map<String, List<ItemRecord>>> loadData(String srcFile, boolean containsHeader) throws Exception {
		Map<String, Map<String, List<ItemRecord>>> map = new HashMap();
		FileReader fr = new FileReader(srcFile);
		BufferedReader br = new BufferedReader(fr);
		String s = "";
		if(containsHeader){
			s = br.readLine();
		}
		String[] columns = s.split(",");
		while ((s = br.readLine()) != null) {
			columns = s.replaceAll("\"", "").split(",");
			ItemRecord record = new ItemRecord(columns);
			if (!map.containsKey(record.getUserId())) {
				map.put(record.getUserId(), new HashMap<String, List<ItemRecord>>());
			}
			if(!map.get(record.getUserId()).containsKey(record.getItemCategory())){
				map.get(record.getUserId()).put(record.getItemCategory(), new ArrayList<ItemRecord>());
			}
			map.get(record.getUserId()).get(record.getItemCategory()).add(record);
		}
		return map;
	}
	
	public static Map<String, String> loadItemData(String file, boolean containsHeader) throws Exception{
		Map<String, String> map = new HashMap<>();
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String s = "";
		if(containsHeader){
			s = br.readLine();
		}
		String[] columns = s.split(",");
		while ((s = br.readLine()) != null) {
			columns = s.replaceAll("\"", "").split(",");
			String itemId= columns[0];
			String categoryId= columns[2];			
			map.put(itemId, categoryId);
		}
		return map;
	}
	
	public static Map<String, Map<String, List<ItemRecord>>> loadData(String srcFile, List<String> behaviors) throws Exception {
		Map<String, Map<String, List<ItemRecord>>> map = new HashMap();
		FileReader fr = new FileReader(srcFile);
		BufferedReader br = new BufferedReader(fr);
		String s = "";
		String[] columns = s.split(",");
		while ((s = br.readLine()) != null) {
			columns = s.replaceAll("\"", "").split(",");
			ItemRecord record = new ItemRecord(columns);
			if(!behaviors.contains(record.getBehaviorType())){
				continue;
			}
			if (!map.containsKey(record.getUserId())) {
				map.put(record.getUserId(), new HashMap<String, List<ItemRecord>>());
			}
			if(!map.get(record.getUserId()).containsKey(record.getItemCategory())){
				map.get(record.getUserId()).put(record.getItemCategory(), new ArrayList<ItemRecord>());
			}
			map.get(record.getUserId()).get(record.getItemCategory()).add(record);
		}
		return map;
	}
	
	public static List<UserRecord> convertMapToList(Map<String, Map<String, List<ItemRecord>>> map){
		List<UserRecord> result = new ArrayList<UserRecord>();
		for(Entry<String, Map<String, List<ItemRecord>>> entry:map.entrySet()){
			String userId = entry.getKey();
			UserRecord  ur= new UserRecord();
			ur.setUserId(userId);
			result.add(ur);
			Map<String, List<ItemRecord>> categoryMap = entry.getValue();
			
			for(Entry<String, List<ItemRecord>> categoryEntry:categoryMap.entrySet()){
				String categoryId=categoryEntry.getKey();
				CategoryRecord  cr= new  CategoryRecord();
				cr.setCategoryId(categoryId);
				List<ItemRecord> list = categoryEntry.getValue();
				Collections.sort(list,new ItemComparator());
				cr.setItems(list);
				ur.getCategaries().add(cr);
			}
			
		}
		
		return result;
	}
	
	
	@SuppressWarnings("unused")
	public static void refineDataFromBehavior(String srcFile,List behavior, String targetFile) throws Exception {
		Map<String, Map<String, List<ItemRecord>>> map = loadData(srcFile, behavior);
		writeFile(map, targetFile);
	}

	public static void writeFile(Map<String, Map<String, List<ItemRecord>>> map, String targetFile) throws Exception {
		int count=0;
		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new FileWriter(targetFile));
		} catch (IOException e) {
		}
		for (Entry<String, Map<String, List<ItemRecord>>> entry : map.entrySet()) {
			String userId= entry.getKey();
			for (Entry<String, List<ItemRecord>> cateEntry : entry.getValue().entrySet()) {
				for (ItemRecord row : cateEntry.getValue()) {
					count++;
					writer.writeNext(new String[]{row.getUserId(),row.getItemId(),row.getBehaviorType(),row.getUserGeohash(),row.getItemCategory(),row.getTime()});
				}
			}
		}
		writer.close();
		System.out.println(targetFile + " contains: " + count +" records.");
	}
	
	public static void writeFileWithArray(Map<String, Map<String, List<String[]>>> map, String targetFile) throws Exception {
		int count=0;
		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new FileWriter(targetFile));
		} catch (IOException e) {
		}
		for (Entry<String, Map<String, List<String[]>>> entry : map.entrySet()) {
			String userId= entry.getKey();
			for (Entry<String, List<String[]>> cateEntry : entry.getValue().entrySet()) {
				for (String[] row : cateEntry.getValue()) {
					count++;
					writer.writeNext(row);
				}
			}
		}
		writer.close();
		System.out.println(targetFile + " contains: " + count +" records.");
	}
	
	public static void writeFileWithArray2(List<String[]> list, String targetFile) throws Exception {
		int count=0;
		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new FileWriter(targetFile));
		} catch (IOException e) {
		}
	
		for (String[] row : list) {
			count++;
			writer.writeNext(row);
		}
		writer.close();
		System.out.println(targetFile + " contains: " + count +" records.");
	}

}
