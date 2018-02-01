package msggenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.junit.Test;

import util.ExcelUtil;

public class ReadActionByStamp {
	ExcelUtil eu = new ExcelUtil();

	public static HSSFWorkbook readReplay(String replayFile) {
		POIFSFileSystem fs = null;
		HSSFWorkbook hb = null;
		try {
			fs = new POIFSFileSystem(new FileInputStream(replayFile));
			hb = new HSSFWorkbook(fs);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hb;
	}

	public long readStamp(HSSFWorkbook hbk, int sheetIndex, CellAddress ca) {
		// 读取时间并转为时间戳
		String timeStr = eu.readCellContent(sheetIndex, ca, hbk);
		if(timeStr.equals("")){
			return -1;
		}
		// System.out.println(timeStr);
		long timeStp = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			timeStp = sdf.parse(timeStr).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 8*3600+timeStp / 1000;
	}

	@Test
	public void readStampTest() {
		HSSFWorkbook hwk = readReplay("D:" + File.separator + "replay.xls");
		// System.out.println(readStamp(hwk, 0, new CellAddress("A3")));
	}

	public Map<Long, List<String>> readDevicesStatus(HSSFWorkbook hbk, int sheetIndex) {
		Map<Long, List<String>> devicesStatus = new HashMap<Long, List<String>>();

		Sheet sheet = hbk.getSheetAt(sheetIndex);
		int rowNum = sheet.getLastRowNum();
		for (int i = 2; i <= rowNum + 1; i++) {
			long stamp = readStamp(hbk, sheetIndex, new CellAddress("A" + i));
			if(stamp==-1){
				
				break;
			}
			Row row = sheet.getRow(i - 1);
			List<String> list = new ArrayList<String>();
			for (int j = 0; j < row.getLastCellNum() + 1; j++) {
				char colChar = (char) (j + 66);

				String cellStr = eu.readCellContent(sheetIndex, new CellAddress(String.valueOf(colChar) + i), hbk);
				if (!cellStr.equals("")) {
					list.add(cellStr);
				}
			}
			devicesStatus.put(stamp, list);
		}

		return devicesStatus;

	}

	@Test
	public void readDevicesStatusTest() {
		HSSFWorkbook hwk = readReplay("D:" + File.separator + "replay.xls");
		System.out.println(readDevicesStatus(hwk, 0));
		// for(String str:readDevicesStatus(hwk,0).get((long)1512666005)){
		// System.out.println(str);
		// }

	}

	public Map<Long, List<AtsTrainInfo>> readDevicesStatusFor2(HSSFWorkbook hbk, int sheetIndex) {
		Map<Long, List<AtsTrainInfo>> devicesStatus = new HashMap<Long, List<AtsTrainInfo>>();

		Sheet sheet = hbk.getSheetAt(sheetIndex);
		int rowNum = sheet.getLastRowNum();
		
		for (int i = 2; i <= rowNum + 1; i++) {
			List<AtsTrainInfo> list = new ArrayList<AtsTrainInfo>();
			long stamp = readStamp(hbk, sheetIndex, new CellAddress("A" + i));
			if(stamp==-1){
				break;
			};
			Row row = sheet.getRow(i - 1);
			// 获取本行中的内容，填充为一个AtsTrainInfo实例
			AtsTrainInfo ati = new AtsTrainInfo();
			// 设备编号
			ati.DeviceNo = eu.readCellContent(sheetIndex, new CellAddress("B" + i), hbk);
			// 服务号
			ati.serverNO = eu.readCellContent(sheetIndex, new CellAddress("C" + i), hbk).split("\\.")[0];
			// 司机号
			ati.driverNO = Integer
					.valueOf(eu.readCellContent(sheetIndex, new CellAddress("D" + i), hbk).split("\\.")[0]);
			// 列车属性
			String property = eu.readCellContent(sheetIndex, new CellAddress("E" + i), hbk);
			if (property.equals("CBTC")) {
				ati.property = 0;
			} else if (property.equals("后备")) {
				ati.property = 1;
			}
			;
			// 时间戳
			ati.Stamp = stamp;
			// 车组号
			ati.GroupId = eu.readCellContent(sheetIndex, new CellAddress("F" + i), hbk).split("\\.")[0];
			// 车次号
			ati.TrainId = eu.readCellContent(sheetIndex, new CellAddress("G" + i), hbk).split("\\.")[0];
			// 表号
			ati.TableNo = eu.readCellContent(sheetIndex, new CellAddress("H" + i), hbk).split("\\.")[0];
			// 目的地
			ati.destination = eu.readCellContent(sheetIndex, new CellAddress("I" + i), hbk).split("\\.")[0];
			// 列车类型
			String trainType=eu.readCellContent(sheetIndex, new CellAddress("J" + i), hbk);
			if(trainType.equals("计划车")){
				ati.TrainType = "1";
			}else if (trainType.equals("头码车")){
				ati.TrainType = "2";
			}else if(trainType.equals("人工车")){
				ati.TrainType = "3";
			}else if(trainType.equals("特殊人工车")){
				ati.TrainType = "3";
			}
			
			// ATP切除
			String atpFlag = eu.readCellContent(sheetIndex, new CellAddress("K" + i), hbk);
			if (atpFlag.equals("是")) {
				ati.AtpFlag = 1;
			} else if (atpFlag.equals("否")) {
				ati.AtpFlag = 0;
			}
			;
			// 列车早晚点
			String onTime = eu.readCellContent(sheetIndex, new CellAddress("L" + i), hbk);
			if (onTime.equals("早点")) {
				ati.OnTime = 1;
			} else if (onTime.equals("正点")) {
				ati.OnTime = 2;
			} else if (onTime.equals("晚点")) {
				ati.OnTime = 3;
			} else if (onTime.equals("严重晚点")) {
				ati.OnTime = 4;
			}
			;
			// 是否跳停
			String skipStopFlag = eu.readCellContent(sheetIndex, new CellAddress("M" + i), hbk);
			if (skipStopFlag.equals("是")) {
				ati.SkipStopFlag = 0;
			} else if (skipStopFlag.equals("否")) {
				ati.SkipStopFlag = 1;
			}
			;
			// 闭塞方式
			ati.BlockMode = Long
					.parseLong(eu.readCellContent(sheetIndex, new CellAddress("N" + i), hbk).replaceAll("0x", ""), 16);
			// 驾驶模式
			String drivingMode = eu.readCellContent(sheetIndex, new CellAddress("O" + i), hbk);
			if (drivingMode.equals("未知")) {
				ati.DrivingMode = 0;
			} else if (drivingMode.equals("AM")) {
				ati.DrivingMode = 1;
			} else if (drivingMode.equals("CM")) {
				ati.DrivingMode = 2;
			} else if (drivingMode.equals("RM")) {
				ati.DrivingMode = 3;
			} else if (drivingMode.equals("NRM")) {
				ati.DrivingMode = 4;
			}
			// 方向
			String dir = eu.readCellContent(sheetIndex, new CellAddress("P" + i), hbk);
			if (dir.equals("未知")) {
				ati.Dir = 0;
			} else if (dir.equals("下行")) {
				ati.Dir = 1;
			} else if (dir.equals("上行")) {
				ati.Dir = 2;
			}
			;
			// 物理区段编号
			ati.PhySection = eu.readCellContent(sheetIndex, new CellAddress("Q" + i), hbk);
			// 逻辑区段编号
			ati.LogicSection = eu.readCellContent(sheetIndex, new CellAddress("R" + i), hbk);
			// 扣车
			String trainBuckle = eu.readCellContent(sheetIndex, new CellAddress("S" + i), hbk);
			if (trainBuckle.equals("扣车")) {
				ati.TrainBuckle = 0;
			} else if (trainBuckle.equals("无扣车")) {
				ati.TrainBuckle = 1;
			}
			;
			// 列车停稳
			String trainStay = eu.readCellContent(sheetIndex, new CellAddress("T" + i), hbk);
			if (trainStay.equals("未停稳")) {
				ati.TrainStay = 0;
			} else if (trainStay.equals("停稳")) {
				ati.TrainStay = 1;
			}
			;
			// 车门信息
			String trainDoor = eu.readCellContent(sheetIndex, new CellAddress("U" + i), hbk);
			if (trainDoor.equals("打开")) {
				ati.TrainDoor = 0;
			} else if (trainDoor.equals("关闭")) {
				ati.TrainDoor = 1;
			} else if (trainDoor.equals("未知")) {
				ati.TrainDoor = 2;
			}
			;
			// 列车冲突
			String trainConflict = eu.readCellContent(sheetIndex, new CellAddress("V" + i), hbk);
			if (trainConflict.equals("不冲突")) {
				ati.TrainConflict = 0;
			} else if (trainConflict.equals("冲突")) {
				ati.TrainConflict = 1;
			}
			;
			// 是否移除
			ati.Delete = Integer
					.parseInt(eu.readCellContent(sheetIndex, new CellAddress("W" + i), hbk).replaceAll("0x", ""), 16);
			// 列车在车次窗位置编号
			ati.TccWindowOffset = Integer
					.parseInt(eu.readCellContent(sheetIndex, new CellAddress("X" + i), hbk).replaceAll("0x", ""), 16);
			if(devicesStatus.containsKey(stamp)){
				list=devicesStatus.get(stamp);
				list.add(ati);
				devicesStatus.put(stamp, list);
			}else{
				list.add(ati);
				devicesStatus.put(stamp, list);
			}
			
			
		}
		return devicesStatus;
	}

	@Test
	public void readDevicesStatusFor2Test() {
		HSSFWorkbook hwk = readReplay("D:" + File.separator + "replay.xls");
		System.out.println(readDevicesStatusFor2(hwk, 2));
	}
	public Map<Long, List<AtsTrainInfo_type>> readDevicesStatusFor2_type(HSSFWorkbook hbk, int sheetIndex) {
		Map<Long, List<AtsTrainInfo_type>> devicesStatus = new HashMap<Long, List<AtsTrainInfo_type>>();

		Sheet sheet = hbk.getSheetAt(sheetIndex);
		int rowNum = sheet.getLastRowNum();
		
		
		for (int i = 2; i <= rowNum + 1; i++) {
			List<AtsTrainInfo_type> list = new ArrayList<AtsTrainInfo_type>();
			long stamp = readStamp(hbk, sheetIndex, new CellAddress("A" + i));
			if(stamp==-1){
				break;
			};
			Row row = sheet.getRow(i - 1);
			// 获取本行中的内容，填充为一个AtsTrainInfo实例
			AtsTrainInfo_type ati = new AtsTrainInfo_type();
			// 设备编号
			ati.DeviceNo = eu.readCellContent(sheetIndex, new CellAddress("B" + i), hbk);
			// 服务号
			ati.serverNO = eu.readCellContent(sheetIndex, new CellAddress("C" + i), hbk).split("\\.")[0];
			// 司机号
			ati.driverNO = eu.readCellContent(sheetIndex, new CellAddress("D" + i), hbk).split("\\.")[0];
			// 列车属性
			String property = eu.readCellContent(sheetIndex, new CellAddress("E" + i), hbk);
			if (property.equals("CBTC")) {
				ati.property = "0";
			} else if (property.equals("后备")) {
				ati.property = "1";
			}
			;
			// 时间戳
			ati.Stamp = stamp+"";
			// 车组号
			ati.GroupId = eu.readCellContent(sheetIndex, new CellAddress("F" + i), hbk).split("\\.")[0];
			// 车次号
			ati.TrainId = eu.readCellContent(sheetIndex, new CellAddress("G" + i), hbk).split("\\.")[0];
			// 表号
			ati.TableNo = eu.readCellContent(sheetIndex, new CellAddress("H" + i), hbk).split("\\.")[0];
			// 目的地
			ati.destination = eu.readCellContent(sheetIndex, new CellAddress("I" + i), hbk).split("\\.")[0];
			// 列车类型
			String trainType=eu.readCellContent(sheetIndex, new CellAddress("J" + i), hbk);
			if(trainType.equals("计划车")){
				ati.TrainType = "1";
			}else if (trainType.equals("头码车")){
				ati.TrainType = "2";
			}else if(trainType.equals("人工车")){
				ati.TrainType = "3";
			}else if(trainType.equals("特殊人工车")){
				ati.TrainType = "3";
			}
			
			// ATP切除
			String atpFlag = eu.readCellContent(sheetIndex, new CellAddress("K" + i), hbk);
			if (atpFlag.equals("是")) {
				ati.AtpFlag = "1";
			} else if (atpFlag.equals("否")) {
				ati.AtpFlag = "0";
			}
			;
			// 列车早晚点
			String onTime = eu.readCellContent(sheetIndex, new CellAddress("L" + i), hbk);
			if (onTime.equals("早点")) {
				ati.OnTime = "1";
			} else if (onTime.equals("正点")) {
				ati.OnTime = "2";
			} else if (onTime.equals("晚点")) {
				ati.OnTime = "3";
			} else if (onTime.equals("严重晚点")) {
				ati.OnTime = "4";
			}
			;
			// 是否跳停
			String skipStopFlag = eu.readCellContent(sheetIndex, new CellAddress("M" + i), hbk);
			if (skipStopFlag.equals("是")) {
				ati.SkipStopFlag = "0";
			} else if (skipStopFlag.equals("否")) {
				ati.SkipStopFlag = "1";
			}
			;
			// 闭塞方式
			ati.BlockMode = Long
					.parseLong(eu.readCellContent(sheetIndex, new CellAddress("N" + i), hbk).replaceAll("0x", ""), 16)+"";
			// 驾驶模式
			String drivingMode = eu.readCellContent(sheetIndex, new CellAddress("O" + i), hbk);
			if (drivingMode.equals("未知")) {
				ati.DrivingMode = "0";
			} else if (drivingMode.equals("AM")) {
				ati.DrivingMode = "1";
			} else if (drivingMode.equals("CM")) {
				ati.DrivingMode = "2";
			} else if (drivingMode.equals("RM")) {
				ati.DrivingMode = "3";
			} else if (drivingMode.equals("NRM")) {
				ati.DrivingMode = "4";
			}
			// 方向
			String dir = eu.readCellContent(sheetIndex, new CellAddress("P" + i), hbk);
			if (dir.equals("未知")) {
				ati.Dir = "0";
			} else if (dir.equals("下行")) {
				ati.Dir = "1";
			} else if (dir.equals("上行")) {
				ati.Dir = "2";
			}
			;
			// 物理区段编号
			ati.PhySection = eu.readCellContent(sheetIndex, new CellAddress("Q" + i), hbk);
			// 逻辑区段编号
			ati.LogicSection = eu.readCellContent(sheetIndex, new CellAddress("R" + i), hbk);
			// 扣车
			String trainBuckle = eu.readCellContent(sheetIndex, new CellAddress("S" + i), hbk);
			if (trainBuckle.equals("扣车")) {
				ati.TrainBuckle = "0";
			} else if (trainBuckle.equals("无扣车")) {
				ati.TrainBuckle = "1";
			}
			;
			// 列车停稳
			String trainStay = eu.readCellContent(sheetIndex, new CellAddress("T" + i), hbk);
			if (trainStay.equals("未停稳")) {
				ati.TrainStay = "0";
			} else if (trainStay.equals("停稳")) {
				ati.TrainStay = "1";
			}
			;
			// 车门信息
			String trainDoor = eu.readCellContent(sheetIndex, new CellAddress("U" + i), hbk);
			if (trainDoor.equals("打开")) {
				ati.TrainDoor = "0";
			} else if (trainDoor.equals("关闭")) {
				ati.TrainDoor = "1";
			} else if (trainDoor.equals("未知")) {
				ati.TrainDoor = "2";
			}
			;
			// 列车冲突
			String trainConflict = eu.readCellContent(sheetIndex, new CellAddress("V" + i), hbk);
			if (trainConflict.equals("不冲突")) {
				ati.TrainConflict = "0";
			} else if (trainConflict.equals("冲突")) {
				ati.TrainConflict = "1";
			}
			;
			// 是否移除
			ati.Delete = Integer
					.parseInt(eu.readCellContent(sheetIndex, new CellAddress("W" + i), hbk).replaceAll("0x", ""), 16)+"";
			// 列车在车次窗位置编号
			ati.TccWindowOffset = Integer
					.parseInt(eu.readCellContent(sheetIndex, new CellAddress("X" + i), hbk).replaceAll("0x", ""), 16)+"";
			if(devicesStatus.containsKey(stamp)){
				list=devicesStatus.get(stamp);
				list.add(ati);
				devicesStatus.put(stamp, list);
			}else{
				list.add(ati);
				devicesStatus.put(stamp, list);
			}
			
			
		}
		return devicesStatus;
	}
	@Test
	public void readDevicesStatusFor2_typeTest() {
		HSSFWorkbook hwk = readReplay("D:" + File.separator + "replay.xls");
		System.out.println(readDevicesStatusFor2_type(hwk, 2));
	}
}
