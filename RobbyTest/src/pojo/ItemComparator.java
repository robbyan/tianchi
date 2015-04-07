package pojo;

import java.util.Comparator;

public class ItemComparator implements Comparator<ItemRecord>{
	/**
	 * 
	 *  sort by userId, categoryId, date, itemId, behavior
	 * 
	 */
	
	@Override
	public int compare(ItemRecord arg0, ItemRecord arg1) {
		
		return itemToStr(arg0).compareTo(itemToStr(arg1));
	}

	private String itemToStr(ItemRecord record){
		StringBuilder sb = new StringBuilder();
		return sb.append(record.getUserId()).append(record.getItemCategory()).append(record.getTime()).append(record.getItemId()).append(record.getBehaviorType()).toString();
	}
	
}
