package msggenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellAddress;
import org.junit.Test;

import util.ExcelUtil;

public class ReadReplay {
	ExcelUtil eu=new ExcelUtil();
	
	
	public static HSSFWorkbook readReplay(String replayFile){
		POIFSFileSystem fs=null;
		HSSFWorkbook hb=null;
		try {
			fs=new POIFSFileSystem(new FileInputStream(replayFile));
			hb=new HSSFWorkbook(fs);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hb;
	}
	public long readStamp(HSSFWorkbook hbk,int sheetIndex,CellAddress ca){
		//读取时间并转为时间戳
		String timeStr=eu.readCellContent(sheetIndex, ca, hbk);
		//System.out.println(timeStr);
		long timeStp=0;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			timeStp=sdf.parse(timeStr).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeStp/1000;
	}
	@Test
	public void readStampTest(){
		HSSFWorkbook hwk=readReplay("D:"+File.separator+"replay.xls");
		//System.out.println(readStamp(hwk,0,new CellAddress("A3")));
	}
	public List<String> readDevicesStatus(HSSFWorkbook hbk,int sheetIndex,CellAddress ca){
		String devicesStatus=eu.readCellContent(sheetIndex, ca, hbk);
		
		return Arrays.asList(devicesStatus.split("\n"));
		
	}
	@Test
	public void readDevicesStatusTest(){
		HSSFWorkbook hwk=readReplay("D:"+File.separator+"replay.xls");
		//System.out.println(readDevicesStatus(hwk,0,new CellAddress("D2")));
	}
}
	
