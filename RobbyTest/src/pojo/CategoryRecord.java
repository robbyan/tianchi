package pojo;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecord {
	private String categoryId;

	private List<ItemRecord> items = new ArrayList<ItemRecord>();

	public List<ItemRecord> getItems() {
		return items;
	}

	public void setItems(List<ItemRecord> items) {
		this.items = items;
	}

	public void addItem(ItemRecord item) {
		this.items.add(item);
	}

	public void removeItem(ItemRecord item) {
		this.items.remove(item);
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the percentage of the items buyed in cart
	 **/
	public double getConvertRatioByCategory() {
		return listBuyedItemWithAddingToCart().size() / getTotalBuyedCount();
	}

	/**
	 *   
	 **/
	public boolean isLastestCartItemGetBuyed() {
		ItemRecord item = getLatestItemInCart();
		if(item  == null){
			return false;
		}
		for (ItemRecord buyedItem : listTotalBuyedItem()) {
			if (buyedItem.getItemId().equalsIgnoreCase(item.getItemId())) {
				return true;
			}
		}

		return false;
	}

	public ItemRecord getLatestItemInCart() {
		List<ItemRecord> list = listTotalAddToChartItem();
		if (list.isEmpty()) {
			return null;
		}
		return list.get(list.size() - 1);
	}

	public int getTotalBuyedCount() {
		return listTotalBuyedItem().size();
	}

	public int getTotalBuyedCountInCart() {
		return listBuyedItemWithAddingToCart().size();
	}

	public List<ItemRecord> listTotalBuyedItem() {
		return listItemsByBehavior("4");
	}

	public List<ItemRecord> listItemsByBehavior(String behavior) {
		List<ItemRecord> list = new ArrayList<ItemRecord>();
		for (ItemRecord item : items) {
			if (item.getBehaviorType().trim().equalsIgnoreCase(behavior)) {
				if(!isItemExistInList(item,list)){
					list.add(item);
				}
			}
		}
		return list;
	}
	
	public boolean isItemExistInList(ItemRecord item,List<ItemRecord> list){
		for(ItemRecord ir:list){
			if(ir.getItemId().equalsIgnoreCase(item.getItemId())){
				return true;
			}
		}
		
		return false;
	}

	public List<ItemRecord> listTotalAddToChartItem() {
		return listItemsByBehavior("3");
	}

	public List<ItemRecord> listBuyedItemWithAddingToCart() {
		List<ItemRecord> result = new ArrayList<ItemRecord>();
		List<ItemRecord> buyedItems = listTotalBuyedItem();
		List<ItemRecord> cartItems = listTotalAddToChartItem();
		for (ItemRecord buyedItem : buyedItems) {
			for (ItemRecord cartItem : cartItems) {
				if (buyedItem.getItemId().equalsIgnoreCase(cartItem.getItemId())) {
					result.add(buyedItem);
				}
			}
		}
		return result;
	}

}
