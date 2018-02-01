package msggenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.junit.Test;

public class ReadStatusList {
	static ReadReplay rr=new ReadReplay();
	
	static Map<String ,List<String>> statusList=new HashMap<String,List<String>>();
	public static Map<String ,List<String>> readStatusList(HSSFWorkbook hwk){
		Sheet sheet=hwk.getSheetAt(4);
		int rowNum=sheet.getLastRowNum();
		for (int i=2;i<=rowNum;i++){
			//先读取状态类型
			String statusName=rr.eu.readCellContent(4, new CellAddress("A"+i) , hwk);
			//再讲状态类型对应的状态码写入List<String>
			Row row=sheet.getRow(i-1);
			List<String> list=new ArrayList<String>();
			for (int j=0;j<row.getLastCellNum()+1;j++){
				char colChar=(char)(j+66);
				String cellTitleStr=rr.eu.readCellContent(4, new CellAddress(String.valueOf(colChar)+"1"),hwk);
				String cellStr=rr.eu.readCellContent(4, new CellAddress(String.valueOf(colChar)+i),hwk).replaceAll("\\.[0-9]*", "");
				if(!cellStr.equals("") ){
					list.add(cellTitleStr+":"+cellStr);
				}else{
					j=row.getLastCellNum();
				}
			};
			statusList.put(statusName, list);
		}
		return statusList;
	}
	@Test
	public void readStatusListTest(){
		System.out.print(readStatusList(ReadActionByStamp.readReplay("D:" + File.separator + "replay.xls")));
	}
}
