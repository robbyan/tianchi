package pojo;

import java.util.ArrayList;
import java.util.List;

public class UserRecord {
	private String userId;
	private List<CategoryRecord> categaries = new ArrayList<CategoryRecord>();

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<CategoryRecord> getCategaries() {
		return categaries;
	}

	public void setCategaries(List<CategoryRecord> categaries) {
		this.categaries = categaries;
	}

	public double getConvertRatio(){	
		int totalBuyedCount=0;
		int totalBuyedInCart=0;
		if(categaries.isEmpty()){
			return Double.NaN;
		}
		for(CategoryRecord cr:categaries){
			totalBuyedCount  = totalBuyedCount+cr.getTotalBuyedCount();
			totalBuyedInCart = totalBuyedInCart + cr.getTotalBuyedCountInCart();
//			System.out.println("The convertRatior for category: " + cr.getCategoryId() + " is: " + cr.getConvertRatioByCategory());
		}
		System.out.println("Total buyed count: " +totalBuyedCount );
		System.out.println("Total buyed count in cart: " +totalBuyedInCart );
		return totalBuyedInCart*100/totalBuyedCount;
	}
	
	public double getConvertRatioByLatestCart(){
		int buyedCount=0;
		if(categaries.isEmpty()){
			return Double.NaN;
		}
		for(CategoryRecord cr:categaries){
			if(cr.isLastestCartItemGetBuyed()){
				buyedCount++;
			}
		}
		return buyedCount*100/categaries.size();
	}
}
