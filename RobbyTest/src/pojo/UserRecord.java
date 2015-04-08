package pojo;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

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
	
	public double getAverageConvertTimeInHour(){
		int count=0;
		int totalHours=0;
		for(CategoryRecord cr:categaries){
			try{
				totalHours=totalHours+cr.getConvertTimeInHour();
				count++;
			}catch(Exception e){
				
			}
		}
		if(count>0){
			return totalHours/count;
		}
		return Double.NaN;
	}
	
	public String listConvertTimeInHour(){
		List<String> list=new ArrayList<String>();
		for(CategoryRecord cr:categaries){
			try{
				list.add(cr.getConvertTimeInHour()+"");
			}catch(Exception e){
				
			}
		}
		return Joiner.on(":").join(list);
	}
}
