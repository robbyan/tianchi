package pojo;

import java.util.ArrayList;
import java.util.List;

public class ItemGroup {
	private String itemId;
	private List<ItemRecord> items = new ArrayList<>();

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public double getDuration() {
		int size = this.getItems().size();
		if (size <= 1) {
			return 0;
		} else {
			ItemRecord firstItem = this.getItems().get(0);
			ItemRecord latestItem = this.getItems().get(size - 1);

			return (latestItem.getDate().getTime() - firstItem.getDate()
					.getTime()) / (3600 * 1000);
		}
	}

	public double getDurationFromBrowseToBuy() {
		ItemRecord browseItem = getRecordByBehavior("1");
		ItemRecord buyItem = getRecordByBehavior("4");
		if (browseItem == null || buyItem == null) {
			return Double.NaN;
		}
		return (int) ((buyItem.getDate().getTime() - browseItem.getDate()
				.getTime()) / (3600 * 1000));
	}

	public double getDurationFromBookmarkToBuy() {
		ItemRecord bookmarkItem = getRecordByBehavior("2");
		ItemRecord buyItem = getRecordByBehavior("4");
		if (bookmarkItem == null || buyItem == null) {
			return Double.NaN;
		}
		return (int) ((buyItem.getDate().getTime() - bookmarkItem.getDate()
				.getTime()) / (3600 * 1000));
	}

	public double getDurationFromCartToBuy() {
		ItemRecord cartItem = getRecordByBehavior("3");
		ItemRecord buyItem = getRecordByBehavior("4");
		if (cartItem == null || buyItem == null) {
			return Double.NaN;
		}
		return (int) ((buyItem.getDate().getTime() - cartItem.getDate()
				.getTime()) / (3600 * 1000));
	}

	public ItemRecord getRecordByBehavior(String behavior) {
		for (ItemRecord item : items) {
			if (item.getBehaviorType().equals(behavior)) {
				return item;

			}
		}
		return null;
	}

	public boolean isBuyedItemInCart() {
		ItemRecord cartItem = getRecordByBehavior("3");
		ItemRecord buyItem = getRecordByBehavior("4");
		return cartItem != null && buyItem != null;
	}

	public boolean isBuyedItemInBookmark() {
		ItemRecord cartItem = getRecordByBehavior("2");
		ItemRecord buyItem = getRecordByBehavior("4");
		return cartItem != null && buyItem != null;
	}

	public int getBrowsedCount() {
		int count = 0;
		for (ItemRecord item : items) {
			if (item.getBehaviorType().equals("1")) {
				count++;
			}
		}
		return count;
	}

	public List<ItemRecord> getItems() {
		return items;
	}

	public void setItems(List<ItemRecord> items) {
		this.items = items;
	}
}
