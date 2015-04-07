package pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemRecord {
	private final static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH");
	private final static SimpleDateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd");

	private String userId;
	private String itemId;
	// 1-click, 2-add to favorite, 3-add to cart ,4- buy
	private String behaviorType;
	private String userGeohash;
	private String itemCategory;
	private String time;

	public ItemRecord(String[] columns) {
		this.userId = columns[0];
		this.itemId = columns[1];
		this.behaviorType = columns[2];
		this.userGeohash = columns[3];
		this.itemCategory = columns[4];
		this.time = columns[5];
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getBehaviorType() {
		return behaviorType;
	}

	public void setBehaviorType(String behaviorType) {
		this.behaviorType = behaviorType;
	}

	public String getUserGeohash() {
		return userGeohash;
	}

	public void setUserGeohash(String userGeohash) {
		this.userGeohash = userGeohash;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Date getDate() {
		try {
			return dateTimeFormatter.parse(time);
		} catch (ParseException e) {

		}
		return null;
	}

	public String getDateAsString(){
		return this.time.substring(0, 10);
	}
}
