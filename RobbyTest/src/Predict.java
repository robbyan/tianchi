import java.util.List;
import java.util.Map;

import pojo.ItemRecord;


public class Predict {
	public static void main(String args[]) throws Exception {
		
		
		String testFile = "D:\\dev\\source\\tianchi\\RobbyTest\\output\\testData.csv";
		Map<String, Map<String, List<ItemRecord>>> map = DataLoader.loadData(testFile, true);
	}
}
