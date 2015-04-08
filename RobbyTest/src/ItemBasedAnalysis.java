import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pojo.ItemRecord;


public class ItemBasedAnalysis {

	public static void main(String args[]) throws Exception{
//		findUnExistItem();
	}
	
	public static void itemUsage(){
		Map<String, String> map = new HashMap<>();
		
		
	}
	
	public static void findUnExistItem() throws Exception{	
		String srcFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\tianchi_mobile_recommend_train_user.csv";
		String itemFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\tianchi_mobile_recommend_train_item.csv";
		Map<String, Map<String, List<ItemRecord>>> map = DataLoader.loadData(srcFile, true);
		Map<String, String> subMap  = DataLoader.loadItemData(itemFile, true);
		for (Entry<String, Map<String, List<ItemRecord>>> entry : map.entrySet()) {
			String userId = entry.getKey();
			Map<String, List<ItemRecord>> categoryMap = entry.getValue();

			for (Entry<String, List<ItemRecord>> categoryEntry : categoryMap.entrySet()) {
				String categoryId = categoryEntry.getKey();
				List<ItemRecord> list = categoryEntry.getValue();
				for(ItemRecord ir:list){
					if(!subMap.containsKey(ir.getItemId())){
						//TODO
//						System.out.println(ir.getItemId());
					}
				}

			}
		}
		
	}
	
	
}
